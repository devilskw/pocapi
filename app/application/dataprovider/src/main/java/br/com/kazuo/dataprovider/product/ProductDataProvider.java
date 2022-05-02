package br.com.kazuo.dataprovider.product;

import br.com.kazuo.dataprovider.product.entity.ProductEntity;
import br.com.kazuo.domain.entity.exception.BusinessCategoryExceptionEnum;
import br.com.kazuo.domain.entity.exception.BusinessException;
import br.com.kazuo.domain.entity.product.Product;
import br.com.kazuo.domain.usecase.product.ProductDsGateway;

import java.time.LocalDateTime;

public class ProductDataProvider implements ProductDsGateway {

    private ProductQueryRepository queryRepository;
    private ProductCommandRepository commandRepository;

    public ProductDataProvider(ProductCommandRepository commandRepository, ProductQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    @Override
    public Product findProductById(Long id) throws BusinessException {
        return this.findProductEntityById(id)
                .toProduct();
    }

    @Override
    public Product createNewProduct(Product product) throws BusinessException {
        return this.commandRepository
                .save(new ProductEntity(product, LocalDateTime.now()))
                .toProduct();
    }

    @Override
    public Product updateProduct(Product product) throws BusinessException {
        ProductEntity entity = this.findProductEntityById(product.getId());
        entity.setName(product.getName());
        return this.commandRepository
                .save(entity)
                .toProduct();
    }

    @Override
    public void deleteProduct(Long id) throws BusinessException {
        this.findProductEntityById(id);
        this.commandRepository.deleteById(id);
    }


    private ProductEntity findProductEntityById(Long id) throws BusinessException {
        return this.queryRepository.findById(id).orElseThrow(() ->
                new BusinessException(
                    BusinessCategoryExceptionEnum.CLIENT_DATA_NOT_FOUND,
                    "Product entity not found",
                    new Object[]{id}
                )
        );
    }
}
