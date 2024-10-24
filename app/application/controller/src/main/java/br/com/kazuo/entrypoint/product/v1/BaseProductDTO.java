package br.com.kazuo.entrypoint.product.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseProductDTO {
    @NotNull
    @Size(min = 2, max = 50)
    @JsonProperty("nome")
    private String name;
}
