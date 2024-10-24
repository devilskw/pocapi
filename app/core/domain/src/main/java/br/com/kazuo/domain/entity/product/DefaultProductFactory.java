package br.com.kazuo.domain.entity.product;

import br.com.kazuo.domain.entity.exception.CustomException;

public class DefaultProductFactory implements ProductFactory {

  @Override
  public Product create(String name) throws CustomException {
    return new DefaultProduct(name);
  }

  @Override
  public Product create(Long id, String name) throws CustomException {
    return new DefaultProduct(id, name);
  }

}
