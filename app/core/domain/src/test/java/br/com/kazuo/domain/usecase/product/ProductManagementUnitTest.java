package br.com.kazuo.domain.usecase.product;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.kazuo.domain.entity.exception.CustomException;
import br.com.kazuo.domain.entity.exception.CustomExceptionCategoryEnum;
import br.com.kazuo.domain.entity.exception.CustomException.CustomExceptionBuilder;
import br.com.kazuo.domain.entity.product.DefaultProduct;
import br.com.kazuo.domain.entity.product.DefaultProductFactory;
import br.com.kazuo.domain.entity.product.Product;

class ProductManagementUnitTest {
    private ProductManagement productManagement;
    private ProductDsGateway gateway;
    private static final String PRODUCT_NAME_FORMAT = "produto %d";

    @BeforeEach
    public void setup() {
        this.gateway = mock(ProductDsGateway.class);
        this.productManagement = new ProductManagement(gateway);
    }

    private List<Product> prepareFakeProducts(Long qtde) throws Exception {
        List<Product> produtos = new ArrayList<>();
        for (long i = 0; i < qtde; i++) {
            produtos.add(this.prepareFakeProduct(i));
        }
        return produtos;
    }

    private Product prepareFakeProduct(Long id) throws Exception {
        return new DefaultProduct(id, String.format(PRODUCT_NAME_FORMAT, id));
    }

    @Test
    void givenPageSizeOrder_whenFindingProducts() throws Exception {
        long qtde = 100;
        int page = 5;
        int size = 10;
        Map<String, Integer> order = new HashMap<>();
        order.put("name", 1);
        List<Product> produtos = prepareFakeProducts(qtde);

        when(this.gateway.findProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyMap())).thenReturn(produtos);

        List<Product> results = this.productManagement.findProducts(page, size, order);

        int assertionIndex = new Random().nextInt((int) qtde);

        Assertions.assertAll(
                () -> assertEquals(qtde, results.size(),
                        "List of products' size must be equal defined qtde when finding products"),
                () -> assertEquals(produtos.get(assertionIndex).getId(), results.get(assertionIndex).getId(),
                        "Product id must be equals as expected when finding products"),
                () -> assertEquals(produtos.get(assertionIndex).getName(), results.get(assertionIndex).getName(),
                        "Product name must be equals as expected when finding products"));
    }

    @Test
    void givenInvalidPageValue_whenFindingProducts() throws Exception {
        int page = -4;
        int size = 10;
        Map<String, Integer> order = new HashMap<>();
        order.put("name", 1);
        when(this.gateway.findProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyMap()))
                .thenThrow(new CustomExceptionBuilder("Negative page value is invalid",
                        CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null)
                                .addArg("page", String.valueOf(page)).build());
        assertThrows(CustomException.class, () -> this.productManagement.findProducts(page, size, order),
                "Method must throws a CustomException if page value is negative when finding products");
    }

    @Test
    void givenInvalidSizeValue_whenFindingProducts() throws Exception {
        int page = 5;
        int size = -3;
        Map<String, Integer> order = new HashMap<>();
        order.put("name", 1);
        when(this.gateway.findProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyMap()))
                .thenThrow(new CustomExceptionBuilder("Negative size value is invalid",
                        CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null)
                                .addArg("size", String.valueOf(size)).build());
        assertThrows(CustomException.class, () -> this.productManagement.findProducts(page, size, order),
                "Method must throws a CustomException if size value is negative when finding products");
    }

    @Test
    void givenId_whenFindingProduct() throws Exception {
        long id = 1;
        Product produto = prepareFakeProduct(id);

        when(this.gateway.findProductById(Mockito.anyLong())).thenReturn(produto);

        Product result = this.productManagement.findProduct(id);

        Assertions.assertAll(() -> assertNotNull(result, "Product objet cannot be null"),
                () -> assertEquals(produto.getId(), result.getId(),
                        "Product id must be as expected when finding a product"),
                () -> assertEquals(produto.getName(), result.getName(),
                        "Product name must be as expected when finding a product"));
    }

    @Test
    void givenInvalidId_whenFindingProduct() throws Exception {
        Long id = null;
        when(this.gateway.findProductById(Mockito.isNull()))
                .thenThrow(new CustomExceptionBuilder("Null id value is invalid",
                        CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null)
                                .addArg("id", String.valueOf(id)).build());
        assertThrows(CustomException.class, () -> this.productManagement.findProduct(id),
                "Method must throws a CustomException if size value is negative when finding a product");
    }

    @Test
    void givenNotFoundId_whenFindingProduct() throws Exception {
        Long id = 10L;
        when(this.gateway.findProductById(Mockito.anyLong()))
                .thenThrow(new CustomExceptionBuilder("Null id value is invalid",
                        CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null)
                                .addArg("id", String.valueOf(id)).build());
        assertThrows(CustomException.class, () -> this.productManagement.findProduct(id),
                "Method must throw a CustomException if a product not found when finding a product");
    }

    @Test
    void givenValidProduct_whenCreatingProduct() throws Exception {
        Long productId = 1L;
        Product expectedProduct = prepareFakeProduct(productId);
        when(this.gateway.createNewProduct(Mockito.any(Product.class))).thenReturn(expectedProduct);
        Product result = this.productManagement
                .createProduct(new DefaultProductFactory().create(expectedProduct.getName()));
        assertAll(() -> assertNotNull(result, "Product objet cannot be null"),
                () -> assertEquals(expectedProduct.getId(), result.getId(),
                        "Product id must be as expected after creating"),
                () -> assertEquals(expectedProduct.getName(), result.getName(),
                        "Product name must be as expected after creating"));
    }

    @Test
    void givenNullProductName_whenCreatingProduct() throws Exception {
        Long productId = 1L;
        Product product = prepareFakeProduct(productId);
        when(this.gateway.createNewProduct(Mockito.any(Product.class))).thenThrow(
                new CustomExceptionBuilder("Error", CustomExceptionCategoryEnum.OPERATION_FAILED, null).build());
        assertThrows(CustomException.class, () -> this.productManagement.createProduct(product),
                "Method must throw a CustomException when a problem occurs while creating a new product");
    }

    @Test
    void givenValidProduct_whenUpdatingProduct() throws Exception {
        Long productId = 1L;
        Product product = prepareFakeProduct(productId);
        when(this.gateway.updateProduct(Mockito.any(Product.class))).thenReturn(product);
        Product result = this.productManagement.updateProduct(product);
        assertAll(() -> assertNotNull(result, "Product objet cannot be null"),
                () -> assertEquals(product.getId(), result.getId(), "Product id must be as expected after updating"),
                () -> assertEquals(product.getName(), result.getName(),
                        "Product name must be as expected after updating"));
    }

    @Test
    void givenNullProductId_whenUpdatingProduct() throws Exception {
        Long productId = null;
        Product product = prepareFakeProduct(productId);
        assertThrows(CustomException.class, () -> this.productManagement.updateProduct(product),
                "Method must throw a CustomException if Product Id is null while updating a product");
    }

    @Test
    void givenValidProductId_whenDeletingProduct() throws Exception {
        Long productId = 1L;
        Long qtdeExclusions = 1050L;
        when(this.gateway.deleteProduct(Mockito.anyLong())).thenReturn(qtdeExclusions);

        Long result = this.productManagement.deleteProduct(productId);
        assertAll(() -> assertNotNull(result, "Result objet cannot be null"),
                () -> assertEquals(qtdeExclusions, result, "Qtd value must be as expected after deleting"));
    }

    @Test
    void givenNullProductId_whenDeletingProduct() {
        Long productId = null;
        assertThrows(CustomException.class, () -> this.productManagement.deleteProduct(productId),
                "Method must throw a CustomException if Product Id is null while deleting a product");
    }

}
