package br.com.kazuo.dataprovider.product.persistence;

import br.com.kazuo.dataprovider.product.persistence.entity.ProductEntity;

public interface ProductCommandRepository {
    ProductEntity save(ProductEntity entity);

    Long deleteById(Long id);
}
