package com.codex.codex_api.service;

import com.codex.codex_api.controllers.UsersController;
import com.codex.codex_api.dtos.AccessUserDto;
import com.codex.codex_api.dtos.MyAvatarItemDTO;
import com.codex.codex_api.exceptions.*;
import com.codex.codex_api.external.api.CodebankApi;
import com.codex.codex_api.external.api.MoodleData;
import com.codex.codex_api.models.Item;
import com.codex.codex_api.models.MyAvatar;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.repositories.ItemRepository;
import com.codex.codex_api.repositories.MyAvatarRepository;
import com.codex.codex_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MyAvatarRepository myAvatarRepository;
    private final MyAvatarService myAvatarService;
    private final RestTemplate restTemplate;
    private final ItemRepository itemRepository;

    @Value("${api.moodle.token}")
    private String tokenMoodle;

    public ResponseEntity<Users> saveAdm(AccessUserDto accessUserDto) {
        try {
            var accessAdmModel = new Users();
            BeanUtils.copyProperties(accessUserDto, accessAdmModel);
            Users savedUser = userRepository.save(accessAdmModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            throw new NotCreated();
        }
    }

    public ResponseEntity<List<Users>> getAllAdm() {
        try{
            List<Users> userList = userRepository.findAll();
            if(!userList.isEmpty()) {
                for (Users user : userList) {
                    UUID id = user.getIdAccess();
                    user.add(linkTo(methodOn(UsersController.class).getOneAccessAdm(id)).withSelfRel());
                }
            }
            return ResponseEntity.ok().body(userList);
        } catch (Exception e) {
            throw new NotFound();
        }
    }

    public ResponseEntity<Object> getOneAccessAdm(UUID id) {
        Optional<Users> accessAdmO = userRepository.findById(id);
        if (accessAdmO.isEmpty()) {
            throw new NotFound();
        }
        accessAdmO.get().add(linkTo(methodOn(UsersController.class).getAllAccessAdm()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(accessAdmO.get());
    }

    public ResponseEntity<Object> updateUser(UUID id, AccessUserDto accessUserDto) {
        Optional<Users> user0 = userRepository.findById(id);
        if (user0.isEmpty()){
            throw new NotFound();
        } else {
            var userModel = user0.get();
            BeanUtils.copyProperties(accessUserDto, userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
        }
    }

    public ResponseEntity<Object> deleteUser(UUID id) {
        Optional<Users> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new NotFound();
        }
        Users user = userOptional.get();

        if (user.getMyAvatar() != null) {
            MyAvatar myAvatar = user.getMyAvatar();

            myAvatarService.removeItem(myAvatar.getIdMyAvatar(),null);

            myAvatarRepository.save(myAvatar);

            myAvatarRepository.delete(myAvatar);
        }
        userRepository.delete(user);

        return ResponseEntity.ok().body("User and related MyAvatar items unlinked successfully.");
    }

    public MoodleData getStudentAva(String naveganteId) {
        String moodleUrl = "https://ava.code8734.com.br/webservice/rest/server.php?wstoken=" + tokenMoodle + "&wsfunction=core_user_get_users&moodlewsrestformat=json&criteria[0][key]=username&criteria[0][value]=" + naveganteId;

        ResponseEntity<MoodleData> response = restTemplate.getForEntity(moodleUrl, MoodleData.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            MoodleData moodleData = response.getBody();
            return moodleData;
        } else {
            throw new ErrorApi();
        }
    }

}
