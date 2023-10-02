package com.codex.codex_api.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_avatar")
public class AvatarModel extends RepresentationModel<AvatarModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idAvatar;
    private String nameAvatar;
    private String type;
    private String description;
    private Integer value;
    private Integer resaleValue;
    private String uriImgAvatar;

    public UUID getIdAvatar() {
        return idAvatar;
    }

    public void setIdAvatar(UUID idAvatar) {
        this.idAvatar = idAvatar;
    }

    public String getNameAvatar() {
        return nameAvatar;
    }

    public void setNameAvatar(String nameAvatar) {
        this.nameAvatar = nameAvatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getResaleValue() {
        return resaleValue;
    }

    public void setResaleValue(Integer resaleValue) {
        this.resaleValue = resaleValue;
    }

    public String getUriImgAvatar() {
        return uriImgAvatar;
    }

    public void setUriImgAvatar(String uriImgAvatar) {
        this.uriImgAvatar = uriImgAvatar;
    }



}
