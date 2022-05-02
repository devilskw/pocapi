package br.com.kazuo.domain.usecase.product;

import br.com.kazuo.domain.entity.exception.BusinessException;
import br.com.kazuo.domain.entity.product.Product;

public interface ProductDsGateway {
    Product findProductById(Long id) throws BusinessException;
    Product createNewProduct(Product product) throws BusinessException;
    Product updateProduct(Product product) throws BusinessException;
    void deleteProduct(Long id) throws BusinessException;
}
