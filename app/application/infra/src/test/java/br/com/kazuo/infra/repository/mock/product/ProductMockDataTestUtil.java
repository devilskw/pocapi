package br.com.kazuo.infra.repository.mock.product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.kazuo.dataprovider.product.persistence.entity.ProductEntity;
import br.com.kazuo.domain.entity.product.DefaultProduct;
import br.com.kazuo.domain.entity.product.Product;

public class ProductMockDataTestUtil {
  public final String PRODUCT_NAME_TEST_FORMAT = "produto %d";

  public List<ProductEntity> prepareFakeProductEntities(Long qtde, LocalDateTime dh_creation) throws Exception {
    List<ProductEntity> produtos = new ArrayList<>();
    for (long i = 1; i <= qtde; i++) {
      produtos.add(this.prepareFakeProductEntity(i, dh_creation));
    }
    return produtos;
  }

  public Product prepareFakeProduct(Long id) throws Exception {
    return new DefaultProduct(id, String.format(PRODUCT_NAME_TEST_FORMAT, id));
  }

  public ProductEntity prepareFakeProductEntity(Long id, LocalDateTime created) throws Exception {
    return new ProductEntity(prepareFakeProduct(id), created);
  }
}
