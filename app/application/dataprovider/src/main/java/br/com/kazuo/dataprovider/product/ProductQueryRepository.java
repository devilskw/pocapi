package br.com.kazuo.dataprovider.product;

import br.com.kazuo.dataprovider.product.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductQueryRepository {
    Optional<ProductEntity> findById(Long id);
    List<ProductEntity> findByName(String name);
}
