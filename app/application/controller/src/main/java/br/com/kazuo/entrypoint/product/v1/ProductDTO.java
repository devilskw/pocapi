package br.com.kazuo.entrypoint.product.v1;

import br.com.kazuo.domain.entity.product.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO extends BaseProductDTO {

    @JsonProperty("codigo_produto")
    private Long id;

    public ProductDTO(Long id, String name) {
        super(name);
        this.id = id;
    }

    public ProductDTO(Product product) {
        super(product.getName());
        this.id = product.getId();
    }
}
