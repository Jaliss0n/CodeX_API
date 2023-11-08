package com.codex.codex_api.service;

import com.codex.codex_api.controllers.ItemController;
import com.codex.codex_api.controllers.UsersController;
import com.codex.codex_api.dtos.AccessUserDto;
import com.codex.codex_api.dtos.ItemDto;
import com.codex.codex_api.dtos.MyAvatarItemDTO;
import com.codex.codex_api.exceptions.*;
import com.codex.codex_api.external.api.CodebankApi;
import com.codex.codex_api.models.Avatar;
import com.codex.codex_api.models.Item;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.repositories.ItemRepository;
import com.codex.codex_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${api.moodle.token}")
    private String tokenMoodle;

    @Value("${api.url.codebank}")
    private String urlCb;

    @Value("${api.folder.item}")
    private String folderPatch;


    public ResponseEntity<Item> saveItem(ItemDto itemDto) {
        try {
            var item = new Item();
            BeanUtils.copyProperties(itemDto, item);
            Item savedItem = itemRepository.save(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (Exception e) {
            throw new NotCreated();
        }

    }

    public ResponseEntity<List<Item>> getAllItems() {
        try {
            List<Item> itemList = itemRepository.findAll();
            if(!itemList.isEmpty()) {
                for (Item item : itemList) {
                    UUID id = item.getIdItem();
                    item.add(linkTo(methodOn(ItemController.class).getOneItem(id)).withSelfRel());
                }
            }
            return ResponseEntity.ok().body(itemList);
        } catch (Exception e) {
            throw new NotFound();
        }
    }

    public ResponseEntity<Object> getOneItem(UUID id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            throw new NotFound();
        }
        itemOptional.get().add(linkTo(methodOn(ItemController.class).getAllItems()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(itemOptional.get());
    }

    public ResponseEntity<Object> updateItem(UUID id, ItemDto itemDto) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()){
            throw new NotFound();
        } else {
            var item = itemOptional.get();
            BeanUtils.copyProperties(itemDto, item);
            return ResponseEntity.status(HttpStatus.OK).body(itemRepository.save(item));
        }

    }

    public ResponseEntity<Object> deleteItem(UUID id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            throw new NotFound();
        }
        Item item = itemOptional.get();
        itemRepository.delete(item);
        return ResponseEntity.status(HttpStatus.OK).body("Item successfully deleted.");
    }

    public ResponseEntity<Object> buyitem(UUID id, MyAvatarItemDTO myAvatarItemDTO) {

        Optional<Users> myUserOptional = userRepository.findById(id);

        if (myUserOptional.isEmpty()) {
            throw new NotFound();
        } else {
            Users users = myUserOptional.get();
            Item item = itemRepository.findById(myAvatarItemDTO.idItem()).orElse(null);

            if (users != null && item != null) {
                if (!users.getItems().contains(item)) {

                    CodebankApi student =  getStudentCB(users.getIdMoodle());

                    if (student.getSaldo() >= item.getValue()) {
                        users.getItems().add(item);
                        item.getUsers().add(users);

                        userRepository.save(users);
                        itemRepository.save(item);

                        int valueUpdate = student.getSaldo() - item.getValue();

                        updateValue(student.getIdMoodle(), valueUpdate);

                        return ResponseEntity.ok().body("Item purchase successfully.");

                    } else {
                        throw new NoEnoughCB();
                    }

                } else {
                    throw new ItemAlreadyExists();
                }
            } else {
                throw new NotCreated();
            }
        }
    }

        public ResponseEntity<Object> sellItem(UUID id, MyAvatarItemDTO myAvatarItemDTO) {

        Optional<Users> usersOptional = userRepository.findById(id);
        Optional<Item> itemOptional = itemRepository.findById(myAvatarItemDTO.idItem());

        if(usersOptional.isEmpty() || itemOptional.isEmpty()) {
            throw new NotFound();
        }

        Users userSelect = usersOptional.get();
        Item item = itemOptional.get();

        List<Item> userItems = userSelect.getItems();

        for (Iterator<Item> iterator = userItems.iterator(); iterator.hasNext();) {
            Item userItem = iterator.next();
            if (userItem.getIdItem() == item.getIdItem()) {
                iterator.remove();
                item.getUsers().remove(userSelect);
                itemRepository.save(item);
                userRepository.save(userSelect);

                CodebankApi student =  getStudentCB(userSelect.getIdMoodle());

                int valueUpdate = student.getSaldo() + item.getResaleValue();
                updateValue(student.getIdMoodle(), valueUpdate);

                return ResponseEntity.status(HttpStatus.OK).body("Item successfully removed and codeCoins updated.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in user's inventory.");

    }

    public String uploadImageItem(MultipartFile file, UUID id) throws IOException {
        if (!isFileExtensionValid(file, "jpg", "png")) {
            return "Only .jpg and ..png files are allowed.";
        }

        long maxSizeBytes = 5 * 1024 * 1024;
        if (file.getSize() > maxSizeBytes) {
            return "File size exceeds the maximum allowed size (5 MB).";
        }

        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            throw new NotFound();
        }

        Item existingItem = itemOptional.get();

        if (existingItem.getUriImgItem() == null) {
            String filePatch = folderPatch + file.getOriginalFilename();

            existingItem.setUriImgItem(filePatch);
            itemRepository.save(existingItem);
            file.transferTo(new File(filePatch));
            return "File uploaded successfully: " + filePatch;

        } else {
            deleteImageItem(id);
            uploadImageItem(file, id);
        }
        return null;
    }

    public String deleteImageItem(UUID id) throws IOException {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            throw new NotFound();
        }

        Item existingItem = itemOptional.get();

        String existingImagePath = existingItem.getUriImgItem();

        File existingImageFile = new File(existingImagePath);

        if (existingImageFile.exists()) {
            if (existingImageFile.delete()) {
                existingItem.setUriImgItem(null);
                itemRepository.save(existingItem);
                return "File deleted successfully";
            } else {
                return "Failed to delete the file";
            }
        } else {
            return "File not found";
        }
    }

    public byte[] downloadImageItem(String fileName) throws IOException {
        Optional<Item> fileData = itemRepository.findByNameItem(fileName);
        String filePath = fileData.get().getUriImgItem();
        byte [] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    private boolean isFileExtensionValid(MultipartFile file, String... allowedExtensions) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            for (String allowedExtension : allowedExtensions) {
                if (originalFilename.endsWith("." + allowedExtension)) {
                    return true;
                }
            }
        }
        return false;
    }

    //API CODEBANCK
    public CodebankApi updateValue(int idMoodle, int saldo) {
        String cbUrl = urlCb + "/alunos/updateBalance/" + idMoodle;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Integer> requestBody = Collections.singletonMap("saldo", saldo);

        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<CodebankApi> responseEntity = restTemplate.exchange(cbUrl, HttpMethod.PUT, requestEntity, CodebankApi.class);

        CodebankApi responseBody = responseEntity.getBody();

        return responseBody;
    }

    public CodebankApi getStudentCB(int idMoodle){
        String cbUrl = urlCb +"/alunos/getIdsFild/" + idMoodle;

        ResponseEntity<CodebankApi[]> response = restTemplate.getForEntity(cbUrl, CodebankApi[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            CodebankApi[] codebankApi = response.getBody();
            return codebankApi[0];
        } else {
            throw new ErrorApi();
        }

    }

}


