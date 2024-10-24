package br.com.kazuo.infra.repository.mock.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductCommandMockUnitTest {

  private ProductCommandMock prdCommand;
  private ProductMockDataTestUtil prdTestUtil = new ProductMockDataTestUtil();
  private LocalDateTime dh_creation;

  @BeforeEach
  public void setup() throws Exception {
    this.prdCommand = new ProductCommandMock();
    ProductMockData.getInstance().entities = new ArrayList<>();
    dh_creation = LocalDateTime.now();
  }

  @Test
  public void givenId_whenProductEntityDeletingById() throws Exception {
    Long prdIndex = 2L;
    Long qtde = 5L;
    Long expectedDeleted = 1L;
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);
    Long result = this.prdCommand.deleteById(prdIndex);
    assertEquals(expectedDeleted, result, "Expects that deleted items is the same as expected.");
  }

  @Test
  public void givenInvalidId_whenProductEntityDeletingById() throws Exception {
    Long prdIndex = 258954L;
    Long qtde = 5L;
    Long expectedDeleted = 0L;
    ProductMockData.getInstance().entities = this.prdTestUtil.prepareFakeProductEntities(qtde, dh_creation);
    Long result = this.prdCommand.deleteById(prdIndex);
    assertEquals(expectedDeleted, result, "Expects that are no deleted items.");
  }

}
