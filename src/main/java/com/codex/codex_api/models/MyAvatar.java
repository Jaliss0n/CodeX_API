package com.codex.codex_api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @ManyToMany(mappedBy = "myAvatars", cascade = CascadeType.REMOVE)
    private List<Item> items;

}
