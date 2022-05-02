package br.com.kazuo.dataprovider.product;

import br.com.kazuo.dataprovider.product.entity.ProductEntity;

public interface ProductCommandRepository {
    ProductEntity save(ProductEntity entity);
    void deleteById(Long id);
}
