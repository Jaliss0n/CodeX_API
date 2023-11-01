package com.codex.codex_api.service;

import com.codex.codex_api.controllers.AvatarController;
import com.codex.codex_api.dtos.AvatarDto;
import com.codex.codex_api.exceptions.NotCreated;
import com.codex.codex_api.exceptions.NotFound;
import com.codex.codex_api.models.Avatar;
import com.codex.codex_api.repositories.AvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final String folderPatch= "D:\\Jalisson Santos\\Documents\\Projedos\\codex_api\\src\\main\\java\\com\\codex\\codex_api\\images\\avatar\\";

    public ResponseEntity<Avatar> saveAvatar(AvatarDto avatarDto) {
        try {
            var avatar = new Avatar();
            BeanUtils.copyProperties(avatarDto, avatar);
            Avatar savedItem = avatarRepository.save(avatar);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (Exception e) {
            throw new NotCreated();
        }

    }

    public ResponseEntity<List<Avatar>> getAllAvatars() {
        try {
            List<Avatar> avatarList = avatarRepository.findAll();
            if(!avatarList.isEmpty()) {
                for (Avatar avatar : avatarList) {
                    UUID id = avatar.getIdAvatar();
                    avatar.add(linkTo(methodOn(AvatarController.class).getOneAvatar(id)).withSelfRel());
                }
            }
            return ResponseEntity.ok().body(avatarList);
        } catch (Exception e) {
            throw new NotFound();
        }
    }

    public ResponseEntity<Object> getOneAvatar(UUID id) {
        Optional<Avatar> avatarOptional = avatarRepository.findById(id);
        if (avatarOptional.isEmpty()) {
            throw new NotFound();
        }
        avatarOptional.get().add(linkTo(methodOn(AvatarController.class).getAllAvatar()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(avatarOptional.get());
    }

    public ResponseEntity<Object> updateAvatar(UUID id, AvatarDto avatarDto) {
        Optional<Avatar> avatarOptional = avatarRepository.findById(id);
        if (avatarOptional.isEmpty()){
            throw new NotFound();
        } else {
            var avatar = avatarOptional.get();
            BeanUtils.copyProperties(avatarDto, avatar);
            return ResponseEntity.status(HttpStatus.OK).body(avatarRepository.save(avatar));
        }

    }

    public ResponseEntity<Object> deleteAvatar(UUID id) throws IOException {
        Optional<Avatar> avatarOptional = avatarRepository.findById(id);

        if (avatarOptional.isEmpty()) {
            throw new NotFound();
        }
        Avatar avatar = avatarOptional.get();

        deleteImageAvatar(id);

        avatarRepository.delete(avatar);
        return ResponseEntity.status(HttpStatus.OK).body("Avatar successfully deleted.");
    }

    public String uploadImageAvatar(MultipartFile file, UUID id) throws IOException {
        if (!isFileExtensionValid(file, "jpg", "png")) {
            return "Only .jpg and ..png files are allowed.";
        }

        long maxSizeBytes = 5 * 1024 * 1024;
        if (file.getSize() > maxSizeBytes) {
            return "File size exceeds the maximum allowed size (5 MB).";
        }

        Optional<Avatar> avatarOptional = avatarRepository.findById(id);

        if (avatarOptional.isEmpty()) {
            throw new NotFound();
        }

        Avatar existingAvatar = avatarOptional.get();

        if (existingAvatar.getUriImgAvatar() == null) {
            String filePatch = folderPatch + file.getOriginalFilename();

            existingAvatar.setUriImgAvatar(filePatch);
            avatarRepository.save(existingAvatar);
            file.transferTo(new File(filePatch));
            return "File uploaded successfully: " + filePatch;

        } else {
           deleteImageAvatar(id);
           uploadImageAvatar(file, id);
        }
        return null;
    }

    public String deleteImageAvatar(UUID id) throws IOException {
        Optional<Avatar> avatarOptional = avatarRepository.findById(id);

        if (avatarOptional.isEmpty()) {
            throw new NotFound();
        }

        Avatar existingAvatar = avatarOptional.get();

        String existingImagePath = existingAvatar.getUriImgAvatar();

        File existingImageFile = new File(existingImagePath);

        if (existingImageFile.exists()) {
            if (existingImageFile.delete()) {
                existingAvatar.setUriImgAvatar(null);
                avatarRepository.save(existingAvatar);
                return "File deleted successfully";
            } else {
                return "Failed to delete the file";
            }
        } else {
            return "File not found";
        }
    }

    public byte[] downloadImageAvatar(String fileName) throws IOException {
        Optional<Avatar> fileData = avatarRepository.findByNameAvatar(fileName);
        String filePath = fileData.get().getUriImgAvatar();
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
}

