package br.com.kazuo.domain.entity.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import br.com.kazuo.domain.entity.exception.CustomException;

public class DefaultProductFactoryUnitTest {

  private static final String ProductNamePrefix = "produto";
  private static final String ProductNameSeparator = "_";

  private String getMassTestProductName(Long id) {
    StringBuilder fakeProductName = new StringBuilder(ProductNamePrefix);
    if (id != null) {
      fakeProductName.append(ProductNameSeparator).append(id.toString());
    }
    return fakeProductName.toString();
  }

  private Map<Long, String> getMassTestProducts(int qtdeProdcuts, boolean withId) throws IllegalArgumentException {
    final Map<Long, String> products = new HashMap<>(qtdeProdcuts);
    for (long i = 1; i <= qtdeProdcuts; i++) {
      if (withId) {
        products.put(i, this.getMassTestProductName(i));
      } else
        products.put(null, this.getMassTestProductName(i));
    }
    return products;
  }

  private List<Executable> prepareDefaulProductAssertions(Product product, Long idAssertionValue,
      String nameAssertionValue, boolean testNullProductId) {
    List<Executable> asserts = new ArrayList<>();
    Executable assertProductisDefaultProductClass = () -> assertInstanceOf(DefaultProduct.class, product,
        "Product object should be a instance of DefaultProduct class");
    asserts.add(assertProductisDefaultProductClass);
    Executable assertProductinheritesFromProductClass = () -> assertInstanceOf(Product.class, product,
        "Product object should inherit from Product superclass");
    asserts.add(assertProductinheritesFromProductClass);
    Executable assertProductId = null;
    if (testNullProductId) {
      assertProductId = () -> assertNull(product.getId(), "Product id should be null");
    } else
      assertProductId = () -> assertEquals(idAssertionValue, product.getId(),
          String.format("Product id should be %d", idAssertionValue));
    asserts.add(assertProductId);
    Executable assertProductName = () -> assertEquals(nameAssertionValue, product.getName(),
        String.format("Product name should be %s", nameAssertionValue));
    asserts.add(assertProductName);
    return asserts;
  }

  @Test
  public void givenIdAndName_whenCreatingDefaultProduct() throws Exception {
    int qtdeIds = 100;
    Map<Long, String> products = this.getMassTestProducts(qtdeIds, true);
    List<Executable> asserts = new ArrayList<>();
    for (Entry<Long, String> item : products.entrySet()) {
      Product product = new DefaultProductFactory().create(item.getKey(), item.getValue());
      asserts.addAll(this.prepareDefaulProductAssertions(product, item.getKey(), item.getValue(), false));
    }
    Assertions.assertAll(asserts);
  }

  @Test
  public void givenOnlyName_whenCreatingDefaultProduct() throws Exception {
    int qtdeIds = 100;
    Map<Long, String> products = this.getMassTestProducts(qtdeIds, false);
    List<Executable> asserts = new ArrayList<>();
    for (Entry<Long, String> item : products.entrySet()) {
      Product product = new DefaultProductFactory().create(item.getKey(), item.getValue());
      asserts.addAll(this.prepareDefaulProductAssertions(product, item.getKey(), item.getValue(), true));
    }
    Assertions.assertAll(asserts);
  }

  @Test
  public void givenNullName_whenCreatingDefaultProduct_shouldThrowCustomException() throws Exception {
    Assertions.assertAll(
        () -> assertThrowsExactly(CustomException.class, () -> new DefaultProductFactory().create(null, null),
            "Product with null name should throw CustomException"),
        () -> assertThrowsExactly(CustomException.class, () -> new DefaultProductFactory().create(1L, null),
            "Product with null name should throw CustomException"));
  }

}
