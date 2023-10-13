package com.codex.codex_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_my_avatar")
public class MyAvatar extends RepresentationModel<MyAvatar> implements Serializable {

    private static final long serialVersionUID= 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idMyAvatar;

    @OneToOne
    @JoinColumn(name = "idUser")
    private Users user;

    @OneToOne()
    @JoinColumn(name = "idAvatarBase")
    private Avatar avatarBase;

    //Linkar intens tbn

}
