package br.com.kazuo.domain.usecase.product;

import java.util.List;
import java.util.Map;

import br.com.kazuo.domain.entity.exception.CustomException;
import br.com.kazuo.domain.entity.product.Product;

public interface ProductDsGateway {
    List<Product> findProducts(Integer page, Integer size, Map<String, Integer> order) throws CustomException;

    Product findProductById(Long id) throws CustomException;

    Product createNewProduct(Product product) throws CustomException;

    Product updateProduct(Product product) throws CustomException;

    Long deleteProduct(Long id) throws CustomException;
}
