package com.codex.codex_api.models;

import com.codex.codex_api.models.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tb_item")
public class Item extends RepresentationModel<Item> implements Serializable {

    private static final long serialVersionUID= 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idItem;

    private String nameItem;
    private Integer type; //propriedades para type:  0 - comun, 1- raro, 2 - epico
    private String description;
    private Integer value;
    private Integer resaleValue;
    private String uriImgItem;

    @ManyToMany
    @JoinTable(name = "items_myavatars",
            joinColumns = @JoinColumn(name = "item_fk"),
            inverseJoinColumns = @JoinColumn(name = "myavatar_fk")
    )
    @JsonIgnore
    private List<MyAvatar> myAvatars;



    public Item(String nameItem, Integer type, String description, Integer value, Integer resaleValue, String uriImgItem){
        this.nameItem = nameItem;
        this.type = type;
        this.description = description;
        this.value = value;
        this.resaleValue = resaleValue;
        this.uriImgItem = uriImgItem;
    }

    public Item(String idItemAsString) {
        this.idItem = UUID.fromString(idItemAsString);
    }



}
