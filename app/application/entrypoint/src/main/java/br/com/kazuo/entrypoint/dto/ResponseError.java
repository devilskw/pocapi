package br.com.kazuo.entrypoint.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "mensagem_excecao"})
public class ResponseError {
    @JsonProperty("codigo_erro")
    Integer code;
    @JsonProperty("mensagem")
    String message;
    @JsonProperty("mensagem_excecao")
    String exceptionMessage;
}
