package com.codex.codex_api.service;

import com.codex.codex_api.models.MyAvatar;
import com.codex.codex_api.repositories.MyAvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyAvatarService {


    private final MyAvatarRepository myAvatarRepository;

    public MyAvatar saveMyAvatar(MyAvatar myAvatar) {
        return myAvatarRepository.save(myAvatar);
    }


}
