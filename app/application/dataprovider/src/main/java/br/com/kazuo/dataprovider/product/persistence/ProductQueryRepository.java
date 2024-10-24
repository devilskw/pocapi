package br.com.kazuo.dataprovider.product.persistence;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.kazuo.dataprovider.product.persistence.entity.ProductEntity;
import br.com.kazuo.domain.entity.exception.CustomException;

public interface ProductQueryRepository {
    Optional<ProductEntity> findById(Long id) throws CustomException;

    List<ProductEntity> findByName(String name) throws CustomException;

    List<ProductEntity> findAll(Integer page, Integer size, Map<String, Integer> order) throws CustomException;
}
