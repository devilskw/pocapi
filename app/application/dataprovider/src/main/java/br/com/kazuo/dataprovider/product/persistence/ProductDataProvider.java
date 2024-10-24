package br.com.kazuo.dataprovider.product.persistence;

import br.com.kazuo.dataprovider.product.persistence.entity.ProductEntity;
import br.com.kazuo.domain.entity.exception.CustomExceptionCategoryEnum;
import br.com.kazuo.domain.entity.exception.CustomException.CustomExceptionBuilder;
import br.com.kazuo.domain.entity.exception.CustomException;
import br.com.kazuo.domain.entity.product.Product;
import br.com.kazuo.domain.usecase.product.ProductDsGateway;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDataProvider implements ProductDsGateway {

    private ProductQueryRepository queryRepository;
    private ProductCommandRepository commandRepository;

    public ProductDataProvider(ProductCommandRepository commandRepository, ProductQueryRepository queryRepository) {
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
    }

    @Override
    public List<Product> findProducts(Integer page, Integer size, Map<String, Integer> order) throws CustomException {
        List<ProductEntity> productEntities = this.queryRepository.findAll(page, size, order);
        List<Product> products = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            products.add(productEntity.toProduct());
        }
        return products;
    }

    @Override
    public Product findProductById(Long id) throws CustomException {
        return this.findProductEntityById(id).toProduct();
    }

    @Override
    public Product createNewProduct(Product product) throws CustomException {
        return this.commandRepository.save(new ProductEntity(product, LocalDateTime.now())).toProduct();
    }

    @Override
    public Product updateProduct(Product product) throws CustomException {
        ProductEntity entity = this.findProductEntityById(product.getId());
        entity.setName(product.getName());
        return this.commandRepository.save(entity).toProduct();
    }

    @Override
    public Long deleteProduct(Long id) throws CustomException {
        this.findProductEntityById(id);
        return this.commandRepository.deleteById(id);
    }

    private ProductEntity findProductEntityById(Long id) throws CustomException {
        return this.queryRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionBuilder("Product entity not found",
                        CustomExceptionCategoryEnum.CLIENT_DATA_NOT_FOUND, null).addArg("id", String.valueOf(id))
                                .build());
    }

}
