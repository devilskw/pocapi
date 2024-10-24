package br.com.kazuo.infra.repository.mock.product;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductMockDataUnitTest {
  private ProductMockDataTestUtil productMockDataTestUtil = new ProductMockDataTestUtil();

  @BeforeEach
  public void setup() throws Exception {
    ProductMockData.getInstance().entities = new ArrayList<>();
  }

  @Test
  public void givenListProducts_whengetttingInstance() throws Exception {
    Long qtde = 7L;
    LocalDateTime dh_creation = LocalDateTime.now();

    assertNotNull(ProductMockData.getInstance(), "ProductMockData instance cannot be null");

    ProductMockData.getInstance().entities = productMockDataTestUtil.prepareFakeProductEntities(qtde, dh_creation);
    assertAll(() -> assertNotNull(ProductMockData.getInstance().entities, "Entities cannot be null"),
        () -> assertEquals(qtde, (long) ProductMockData.getInstance().entities.size(),
            "Entities size must be equals expected value"));
  }

}
