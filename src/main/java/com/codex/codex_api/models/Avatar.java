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
@Table(name = "tb_avatar")
public class Avatar extends RepresentationModel<Avatar> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idAvatar;

    private String nameAvatar;
    private Integer type;
    private String description;
    private Integer value;
    private Integer resaleValue;
    private String uriImgAvatar;

}
