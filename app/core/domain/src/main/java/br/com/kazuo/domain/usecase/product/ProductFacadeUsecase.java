package br.com.kazuo.domain.usecase.product;

import br.com.kazuo.domain.entity.exception.BusinessException;
import br.com.kazuo.domain.entity.product.Product;

public class ProductFacadeUsecase implements ProductUseCase {
    private ProductDsGateway gateway;

    public ProductFacadeUsecase(ProductDsGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Product findProduct(Long id) throws BusinessException {
        return gateway.findProductById(id);
    }

    @Override
    public Product createProduct(Product product) throws BusinessException {
        return gateway.createNewProduct(product);
    }

    @Override
    public Product updateProduct(Product product) throws BusinessException {
        return gateway.updateProduct(product);
    }

    @Override
    public void deleteProduct(Long id) throws BusinessException {
        gateway.deleteProduct(id);
    }
}
