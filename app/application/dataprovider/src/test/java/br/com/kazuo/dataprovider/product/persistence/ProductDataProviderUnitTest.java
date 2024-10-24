package br.com.kazuo.dataprovider.product.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.kazuo.dataprovider.product.persistence.entity.ProductEntity;
import br.com.kazuo.domain.entity.exception.CustomException;
import br.com.kazuo.domain.entity.exception.CustomExceptionCategoryEnum;
import br.com.kazuo.domain.entity.exception.CustomException.CustomExceptionBuilder;
import br.com.kazuo.domain.entity.product.DefaultProduct;
import br.com.kazuo.domain.entity.product.Product;

class ProductDataProviderUnitTest {

  private ProductDataProvider productDataProvider;
  private ProductQueryRepository queryRepository;
  private ProductCommandRepository commandRepository;

  private static final String PRODUCT_NAME_FORMAT = "produto %d";

  @BeforeEach
  public void setup() {
    this.queryRepository = mock(ProductQueryRepository.class);
    this.commandRepository = mock(ProductCommandRepository.class);
    this.productDataProvider = new ProductDataProvider(commandRepository, queryRepository);
  }

  private List<ProductEntity> prepareFakeProductEntities(Long qtde, LocalDateTime dh_creation) throws Exception {
    List<ProductEntity> produtos = new ArrayList<>();
    for (long i = 0; i < qtde; i++) {
      produtos.add(this.prepareFakeProductEntity(i, dh_creation));
    }
    return produtos;
  }

  private Product prepareFakeProduct(Long id) throws Exception {
    return new DefaultProduct(id, String.format(PRODUCT_NAME_FORMAT, id));
  }

  private ProductEntity prepareFakeProductEntity(Long id, LocalDateTime created) throws Exception {
    return new ProductEntity(prepareFakeProduct(id), created);
  }

  @Test
  void givenPageSizeOrder_whenFindingProducts() throws Exception {
    long qtdFakeProductItems = 14L;
    int page = 3;
    int size = 5;
    String orderName = "testOrder";
    int orderValue = 1;
    LocalDateTime agora = LocalDateTime.now();
    Map<String, Integer> order = new HashMap<>();
    order.put(orderName, orderValue);
    List<ProductEntity> expectedProducts = this.prepareFakeProductEntities(qtdFakeProductItems, agora);

    when(this.queryRepository.findAll(anyInt(), anyInt(), anyMap())).thenReturn(expectedProducts);

    List<Product> result = this.productDataProvider.findProducts(page, size, order);
    Assertions.assertAll(() -> assertNotNull(result, "List of products cannot be null."),
        () -> assertEquals(qtdFakeProductItems, result.size(), "Qtd of product items must be the same as expected."));
  }

  @Test
  void givenPageSizeOrder_whenFindingProducts_throwsCustomException() throws Exception {
    int page = 3;
    int size = 5;
    String orderName = "testOrder";
    int orderValue = 1;
    Map<String, Integer> order = new HashMap<>();
    order.put(orderName, orderValue);

    CustomException exception = new CustomExceptionBuilder("Teste", CustomExceptionCategoryEnum.OPERATION_FAILED, null)
        .build();

    when(this.queryRepository.findAll(anyInt(), anyInt(), anyMap())).thenThrow(exception);

    assertThrows(CustomException.class, () -> this.productDataProvider.findProducts(page, size, order),
        "Test executable expects to throw a CustomException while findings Produts");
  }

  @Test
  void givenInvalidId_whenFindingProduct() throws Exception {
    Long id = 98989898L;
    when(this.queryRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(CustomException.class, () -> this.productDataProvider.findProductById(id),
        "Method must throws a CustomException if size value is negative when finding a product");
  }

  @Test
  void givenId_whenFindingProduct() throws Exception {
    long id = 1;
    LocalDateTime agora = LocalDateTime.now();
    ProductEntity productEntity = prepareFakeProductEntity(id, agora);
    when(this.queryRepository.findById(anyLong())).thenReturn(Optional.of(productEntity));
    Product result = this.productDataProvider.findProductById(id);

    Assertions.assertAll(() -> assertNotNull(result, "Product objet cannot be null"),
        () -> assertEquals(productEntity.getId(), result.getId(),
            "Product id must be as expected when finding a product"),
        () -> assertEquals(productEntity.getName(), result.getName(),
            "Product name must be as expected when finding a product"));
  }

  @Test
  void givenValidProduct_whenCreatingProduct() throws Exception {
    Long productId = 1L;
    LocalDateTime agora = LocalDateTime.now();
    ProductEntity expectedProductEntity = prepareFakeProductEntity(productId, agora);
    when(this.commandRepository.save(any(ProductEntity.class))).thenReturn(expectedProductEntity);
    Product result = this.productDataProvider.createNewProduct(expectedProductEntity.toProduct());
    assertAll(() -> assertNotNull(result, "Product objet cannot be null"),
        () -> assertEquals(expectedProductEntity.getId(), result.getId(),
            "Product id must be as expected after creating"),
        () -> assertEquals(expectedProductEntity.getName(), result.getName(),
            "Product name must be as expected after creating"));
  }

  @Test
  void givenInvalidId_whenUpdatingProduct() throws Exception {
    Long id = 98989898L;
    Product product = prepareFakeProduct(id);
    when(this.queryRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(CustomException.class, () -> this.productDataProvider.updateProduct(product),
        "Method must throws a CustomException if product not found by id.");
  }

  // @Test
  // void throwsCustomException_whenUpdatingProduct() throws Exception {
  // Long id = 98989898L;
  // Product product = prepareFakeProduct(id);
  // when(this.queryRepository.findById(anyLong()))
  // .thenReturn(Optional.of(new ProductEntity(product)));
  // when(this.commandRepository.save(any(ProductEntity.class)))
  // .thenThrow(new CustomExceptionBuilder("Error test",
  // CustomExceptionCategoryEnum.OPERATION_FAILED).build());
  // assertThrows(CustomException.class,
  // () -> this.productDataProvider.updateProduct(product),
  // "Test assertion expects to throw a CustomException during test
  // execution");
  // }

  @Test
  void givenValidProduct_whenUpdatingProduct() throws Exception {
    Long productId = 1L;
    LocalDateTime agora = LocalDateTime.now();
    ProductEntity originalProductEntity = prepareFakeProductEntity(productId, agora);
    ProductEntity expectedProductEntity = new ProductEntity(originalProductEntity.toProduct(), agora);
    expectedProductEntity.setName("Novo Nome do Produto");
    when(this.queryRepository.findById(anyLong())).thenReturn(Optional.of(originalProductEntity));
    when(this.commandRepository.save(Mockito.any(ProductEntity.class))).thenReturn(expectedProductEntity);
    Product result = this.productDataProvider.updateProduct(expectedProductEntity.toProduct());
    assertAll(() -> assertNotNull(result, "Product objet cannot be null"),
        () -> assertEquals(expectedProductEntity.getId(), result.getId(),
            "Product id must be as expected after updating"),
        () -> assertEquals(expectedProductEntity.getName(), result.getName(),
            "Product name must be as expected after updating"));
  }

  @Test
  void givenInvalidId_whenDeletingProduct() throws Exception {
    Long id = 98989898L;
    when(this.queryRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(CustomException.class, () -> this.productDataProvider.deleteProduct(id),
        "Method must throws a CustomException if product not found by id.");
  }

  // @Test
  // void throwsCustomException_whenDeletingProduct() throws Exception {
  // Long id = 98989898L;
  // Product product = prepareFakeProduct(id);
  // CustomException exception = new CustomExceptionBuilder("Error
  // test", CustomExceptionCategoryEnum.OPERATION_FAILED).build()
  // when(this.queryRepository.findById(anyLong()))
  // .thenReturn(Optional.of(new ProductEntity(product)));
  // when(this.commandRepository.save(any(ProductEntity.class)))
  // .thenThrow(exception);
  // assertThrows(CustomException.class,
  // () -> this.productDataProvider.deleteProduct(id),
  // "Test assertion expects to throw a CustomException during test
  // execution.");
  // }

  // @Test
  // void givenValidProductId_whenDeletingProduct() throws Exception {
  // Long productId = 1L;
  // Long qtdeExclusions = 1050L;
  // when(this.commandRepository.deleteById(Mockito.anyLong()))
  // .thenReturn(qtdeExclusions);

  // Long result = this.productDataProvider.deleteProduct(productId);
  // assertAll(
  // () -> assertNotNull(result, "Result objet cannot be null"),
  // () -> assertEquals(productId, result, "Qtd value must be as
  // expected after deleting")
  // );
  // }

}
