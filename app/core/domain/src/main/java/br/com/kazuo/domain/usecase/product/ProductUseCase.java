package br.com.kazuo.domain.usecase.product;

import br.com.kazuo.domain.entity.product.Product;
import br.com.kazuo.domain.entity.exception.BusinessException;

public interface ProductUseCase {
    Product findProduct(Long id) throws BusinessException;
    Product createProduct(Product product) throws BusinessException;
    Product updateProduct(Product product) throws BusinessException;
    void deleteProduct(Long id) throws BusinessException;
}
