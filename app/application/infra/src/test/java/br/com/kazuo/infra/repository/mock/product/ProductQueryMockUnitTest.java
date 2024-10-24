package br.com.kazuo.infra.repository.mock.product;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import br.com.kazuo.dataprovider.product.persistence.entity.ProductEntity;

public class ProductQueryMockUnitTest {
  private ProductQueryMock prdQuery;
  private ProductMockDataTestUtil prdTestUtil = new ProductMockDataTestUtil();
  private LocalDateTime dh_creation;

  @BeforeEach
  public void setup() throws Exception {
    this.prdQuery = new ProductQueryMock();
    ProductMockData.getInstance().entities = new ArrayList<>();
    dh_creation = LocalDateTime.now();
  }

  @Test
  public void givenId_whenFindingProductEntityById() throws Exception {
    Long qtde = 11L;
    Long productId = 3L;
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);

    Optional<ProductEntity> optResult = this.prdQuery.findById(productId);

    assertAll(() -> assertTrue(optResult.isPresent(), "ProductEntity must exist."),
        () -> assertEquals(productId, optResult.get().getId(), "ProductEntity id must be as expected."),
        () -> assertEquals(String.format(this.prdTestUtil.PRODUCT_NAME_TEST_FORMAT, productId),
            optResult.get().getName(), "ProductEntity name must be as expected."));

  }

  @Test
  public void givenInvalidId_whenFindingProductEntityById() throws Exception {
    Long qtde = 1L;
    Long productId = 3L;
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);
    Optional<ProductEntity> optResult = this.prdQuery.findById(productId);
    assertFalse(optResult.isPresent(), "ProductEntity cannot exist.");
  }

  @Test
  public void givenId_whenFindingProductEntityByName() throws Exception {
    Long qtde = 11L;
    Long productId = 3L;
    String productName = String.format(this.prdTestUtil.PRODUCT_NAME_TEST_FORMAT, productId);
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);

    List<ProductEntity> results = this.prdQuery.findByName(productName);

    assertAll(() -> assertNotNull(results, "Result list cannot be null "),
        () -> assertEquals(1, results.size(), "Result list size must be as expected."),
        () -> assertEquals(productId, results.get(0).getId(), "ProductEntity id must be as expected."),
        () -> assertEquals(productName, results.get(0).getName(), "ProductEntity name must be as expected."));

  }

  @Test
  public void givenInvalidId_whenFindingProductEntityByName() throws Exception {
    Long qtde = 1L;
    Long productId = 3L;
    String productName = String.format(this.prdTestUtil.PRODUCT_NAME_TEST_FORMAT, productId);
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);
    List<ProductEntity> results = this.prdQuery.findByName(productName);
    assertAll(() -> assertNotNull(results, "Result list cannot be null "),
        () -> assertEquals(0, results.size(), "Result list size must be empty."));
  }

  @Test
  public void givenPageSizeOrder_whenFindingPagedProductEntities() throws Exception {
    Long qtde = 11L;
    Integer page = 1;
    Integer size = 4;
    Map<String, Integer> order = new HashMap<>();
    order.put("id", 1);
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);
    List<Executable> executables = new ArrayList<>();

    List<ProductEntity> results = this.prdQuery.findAll(page, size, order);
    executables.add(() -> assertNotNull(results, "Asserts that result is not null."));
    executables.add(() -> assertEquals(size, results.size(), "Asserts that result size is the same as expected."));

    for (int i = 0; i < results.size(); i++) {
      final int productId = i + 5;
      ProductEntity entity = results.get(i);
      executables
          .add(() -> assertEquals(productId, entity.getId().intValue(), "Asserts that id is equals as expected value"));
      executables.add(() -> assertEquals(String.format(this.prdTestUtil.PRODUCT_NAME_TEST_FORMAT, productId),
          entity.getName(), "Asserts that product name is equals as expected value"));
    }

    Assertions.assertAll(executables);
  }

  @Test
  public void givenPageSizeReversedOrder_whenFindingPagedProductEntities() throws Exception {
    Long qtde = 11L;
    Integer page = 1;
    Integer size = 4;
    Map<String, Integer> order = new HashMap<>();
    order.put("name", 0);
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);
    List<Executable> executables = new ArrayList<>();

    List<ProductEntity> results = this.prdQuery.findAll(page, size, order);
    executables.add(() -> assertNotNull(results, "Asserts that result is not null."));
    executables.add(() -> assertEquals(size, results.size(), "Asserts that result size is the same as expected."));

    for (int i = 0; i < results.size(); i++) {
      final int productId = 5 - i;
      ProductEntity entity = results.get(i);
      executables
          .add(() -> assertEquals(productId, entity.getId().intValue(), "Asserts that id is equals as expected value"));
      executables.add(() -> assertEquals(String.format(this.prdTestUtil.PRODUCT_NAME_TEST_FORMAT, productId),
          entity.getName(), "Asserts that product name is equals as expected value"));
    }

    Assertions.assertAll(executables);
  }

  @Test
  public void givenZeroPageNegativeSizeOrder_whenFindingPagedProductEntities() throws Exception {
    Long qtde = 11L;
    Integer page = 0;
    Integer size = -100;
    Map<String, Integer> order = new HashMap<>();
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);
    List<Executable> executables = new ArrayList<>();

    List<ProductEntity> results = this.prdQuery.findAll(page, size, order);
    executables.add(() -> assertNotNull(results, "Asserts that result is not null."));
    executables.add(() -> assertEquals(qtde, results.size(), "Asserts that result size is the same as expected."));

    for (int i = 0; i < results.size(); i++) {
      final int productId = i + 1;
      ProductEntity entity = results.get(i);
      executables
          .add(() -> assertEquals(productId, entity.getId().intValue(), "Asserts that id is equals as expected value"));
      executables.add(() -> assertEquals(String.format(this.prdTestUtil.PRODUCT_NAME_TEST_FORMAT, productId),
          entity.getName(), "Asserts that product name is equals as expected value"));
    }

    Assertions.assertAll(executables);
  }

}
