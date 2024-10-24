package br.com.kazuo.domain.entity.product;

import br.com.kazuo.domain.entity.exception.CustomException;

public interface ProductFactory {
  public Product create(String name) throws CustomException;

  public Product create(Long id, String name) throws CustomException;
}
