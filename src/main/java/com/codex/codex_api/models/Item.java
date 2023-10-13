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

}
