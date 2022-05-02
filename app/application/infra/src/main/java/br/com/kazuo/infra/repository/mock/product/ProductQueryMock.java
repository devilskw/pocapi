package br.com.kazuo.infra.repository.mock.product;

import br.com.kazuo.dataprovider.product.ProductQueryRepository;
import br.com.kazuo.dataprovider.product.entity.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductQueryMock implements ProductQueryRepository {
    @Override
    public Optional<ProductEntity> findById(Long id) {
        return ProductMockData.getInstance().entities
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ProductEntity> findByName(String name) {
        return ProductMockData.getInstance().entities
                .stream()
                .filter(e -> e.getName().contains(name))
                .collect(Collectors.toList());
    }
}
