package br.com.kazuo.infra.repository.mock.product;

import br.com.kazuo.dataprovider.product.ProductCommandRepository;
import br.com.kazuo.dataprovider.product.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ProductCommandMock implements ProductCommandRepository {
    @Override
    public ProductEntity save(ProductEntity entity) {
        if (Objects.isNull(entity.getId())) {
            Long maxId = ProductMockData.getInstance().entities
                    .parallelStream()
                    .map(ProductEntity::getId)
                    .reduce((major, e) -> e > major ? e : major )
                    .orElse(0L) ;
            entity.setId(maxId + 1L);
            if (Objects.isNull(entity.getCreated())) {
                entity.setCreated(LocalDateTime.now());
            }
            ProductMockData.getInstance().entities.add(entity);
            return entity;
        }
        ProductEntity product = ProductMockData.getInstance().entities
                .stream()
                .filter(e -> e.getId().equals(entity.getId()))
                .findFirst()
                .orElseThrow( () -> new NoSuchElementException("Producto não localizado"));
        product.setName(entity.getName());
        return product;
    }

    @Override
    public void deleteById(Long id) {
        ProductMockData.getInstance().entities
                .removeIf(e -> e.getId().equals(id));
    }
}
