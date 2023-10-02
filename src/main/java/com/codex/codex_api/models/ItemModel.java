package com.codex.codex_api.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_item")
public class ItemModel extends RepresentationModel<ItemModel> implements Serializable {

    private static final long serialVersionUID= 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idItem;
    private String nameItem;
    //propriedades para type:  0 - comun, 1- raro, 2 - epico
    private Integer type;
    private String description;
    private Integer value;
    private Integer resaleValue;
    private String uriImgItem;

    public UUID getIdItem() {
        return idItem;
    }

    public void setIdItem(UUID idItem) {
        this.idItem = idItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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

    public String getUriImgItem() {
        return uriImgItem;
    }

    public void setUriImgItem(String uriImgItem) {
        this.uriImgItem = uriImgItem;
    }
}
