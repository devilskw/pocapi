package br.com.kazuo.infra.repository.mock.product;

import br.com.kazuo.dataprovider.product.persistence.ProductQueryRepository;
import br.com.kazuo.dataprovider.product.persistence.entity.ProductEntity;
import br.com.kazuo.domain.entity.exception.CustomException;
import br.com.kazuo.domain.entity.exception.CustomExceptionCategoryEnum;
import br.com.kazuo.domain.entity.exception.CustomException.CustomExceptionBuilder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ProductQueryMock implements ProductQueryRepository {
    @Override
    public Optional<ProductEntity> findById(Long id) {
        return ProductMockData.getInstance().entities.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<ProductEntity> findByName(String name) {
        return ProductMockData.getInstance().entities.stream().filter(e -> e.getName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAll(Integer page, Integer size, Map<String, Integer> order) throws CustomException {
        Integer maxSize = ProductMockData.getInstance().entities.size();
        Integer tmpPage = (page == null || page < 0) ? 0 : page;
        Integer tmpSize = (size == null || size <= 0) ? maxSize : size;
        Integer fromIndex = tmpPage * tmpSize > tmpPage * tmpSize ? maxSize : tmpPage * tmpSize;
        Integer toIndex = (fromIndex + tmpSize) > maxSize ? maxSize : (fromIndex + tmpSize);
        Comparator<ProductEntity> sortProdList = null;
        if (order == null)
            order = new HashMap<>();
        for (Entry<String, Integer> orderItem : order.entrySet()) {
            switch (orderItem.getKey().toLowerCase()) {
            case "id":
                if (orderItem.getValue() > 0) {
                    sortProdList = (sortProdList == null ? Comparator.comparing(ProductEntity::getId)
                            : sortProdList.thenComparing(Comparator.comparing(ProductEntity::getId)));
                } else {
                    sortProdList = (sortProdList == null ? Comparator.comparing(ProductEntity::getId).reversed()
                            : sortProdList.thenComparing(Comparator.comparing(ProductEntity::getId).reversed()));
                }
                break;
            case "name":
                if (orderItem.getValue() > 0) {
                    sortProdList = (sortProdList == null ? Comparator.comparing(ProductEntity::getName)
                            : sortProdList.thenComparing(Comparator.comparing(ProductEntity::getName)));
                } else {
                    sortProdList = (sortProdList == null ? Comparator.comparing(ProductEntity::getName).reversed()
                            : sortProdList.thenComparing(Comparator.comparing(ProductEntity::getName).reversed()));
                }
                break;
            default:
                break;
            }
        }
        if (sortProdList == null)
            sortProdList = Comparator.comparing(ProductEntity::getId);
        return ProductMockData.getInstance().entities.parallelStream().sorted(sortProdList).collect(Collectors.toList())
                .subList(fromIndex, toIndex);
    }
}
