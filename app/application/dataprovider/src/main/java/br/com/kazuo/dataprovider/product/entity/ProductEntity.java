package br.com.kazuo.dataprovider.product.entity;

import br.com.kazuo.domain.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    protected Long id;
    protected String name;
    protected LocalDateTime created;

    public ProductEntity(Product product) {
        this.setId(product.getId());
        this.setName(product.getName());
    }

    public ProductEntity(Product product, LocalDateTime dhCreation) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setCreated(dhCreation);
    }

    public Product toProduct() {
        return new Product(this.getId(), this.getName());
    }
}
