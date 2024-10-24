package br.com.kazuo.domain.usecase.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.kazuo.domain.entity.exception.CustomException;
import br.com.kazuo.domain.entity.exception.CustomExceptionCategoryEnum;
import br.com.kazuo.domain.entity.exception.CustomException.CustomExceptionBuilder;
import br.com.kazuo.domain.entity.product.Product;
import br.com.kazuo.shared.observability.CounterAnnotation;

public class ProductManagement implements ProductUsecase {
    private ProductDsGateway gateway;

    public ProductManagement(ProductDsGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    @CounterAnnotation(value = "product_find_paged_count", tags = { "page", "${page}", "size", "${size}" })
    public List<Product> findProducts(Integer page, Integer size, Map<String, Integer> order) throws CustomException {
        this.validatePagingParameters(page, size);
        return gateway.findProducts(page, size, order);
    }

    private void validatePagingParameters(Integer page, Integer size) throws CustomException {
        Map<String, String> args = new HashMap<>();
        if (page != null && page < 0)
            args.put("page", String.valueOf(page));
        if (size != null && size < 0)
            args.put("size", String.valueOf(size));
        if (args.size() > 0) {
            throw new CustomExceptionBuilder("Page and Size cannot be a negative value",
                    CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null).setArgs(args).build();
        }
    }

    @Override
    @CounterAnnotation(value = "product_find_count")
    public Product findProduct(Long id) throws CustomException {
        Product product = gateway.findProductById(id);
        if (product == null) {
            throw new CustomExceptionBuilder("Null id value is invalid",
                    CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null).addArg("id", String.valueOf(id))
                            .build();
        }
        return product;
    }

    @Override
    @CounterAnnotation(value = "product_create_count")
    public Product createProduct(Product product) throws CustomException {
        return gateway.createNewProduct(product);
    }

    @Override
    @CounterAnnotation(value = "product_update_count")
    public Product updateProduct(Product product) throws CustomException {
        this.validateProductBeforeUpdating(product);
        return gateway.updateProduct(product);
    }

    private void validateProductBeforeUpdating(Product product) throws CustomException {
        if (product.getId() == null) {
            throw new CustomExceptionBuilder("Null id value is invalid",
                    CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null)
                            .addArg("id", String.valueOf(product.getId())).build();
        }
    }

    @Override
    @CounterAnnotation(value = "product_delete_count")
    public Long deleteProduct(Long id) throws CustomException {
        this.validateIdBeforeDeleting(id);
        return gateway.deleteProduct(id);
    }

    private void validateIdBeforeDeleting(Long id) throws CustomException {
        if (id == null) {
            throw new CustomExceptionBuilder("Null id value is invalid",
                    CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null).addArg("id", String.valueOf(id))
                            .build();
        }
    }

}
