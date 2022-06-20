package br.com.kazuo.domain.usecase.product;

import br.com.kazuo.domain.entity.exception.BusinessCategoryExceptionEnum;
import br.com.kazuo.domain.entity.exception.BusinessException;
import br.com.kazuo.domain.entity.product.Product;
import br.com.kazuo.shared.observability.CounterAnnotation;
import io.micrometer.core.instrument.Metrics;

public class ProductFacadeUsecase implements ProductUseCase {
    private ProductDsGateway gateway;

    public ProductFacadeUsecase(ProductDsGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    @CounterAnnotation(value = "product.find.count")
    public Product findProduct(Long id) throws BusinessException {
        Metrics.counter("product.find.count2").increment();
        return gateway.findProductById(id);
    }

    @Override
    @CounterAnnotation(value = "product.create.count")
    public Product createProduct(Product product) throws BusinessException {
      return gateway.createNewProduct(product);
    }

    @Override
    @CounterAnnotation(value = "product.update.count")
    public Product updateProduct(Product product) throws BusinessException {
        return gateway.updateProduct(product);
    }

    @Override
    @CounterAnnotation(value = "product.delete.count")
    public void deleteProduct(Long id) throws BusinessException {
        gateway.deleteProduct(id);
    }
}
