package com.codex.codex_api.external.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodebankApi {

    @JsonProperty("id")
    private int idCodebanck;

    @JsonProperty("id_aluno_moodle")
    private int idMoodle;

    @JsonProperty("saldo")
    private int saldo;

    @JsonProperty("nome_aluno")
    private String nomeAluno;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

}
