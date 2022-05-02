package br.com.kazuo.infra.repository.mock.product;

import br.com.kazuo.dataprovider.product.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductMockData {
    private static volatile ProductMockData instance;

    public List<ProductEntity> entities;

    private ProductMockData() {
        if (Objects.isNull(this.entities)) {
            this.entities = new ArrayList<>();
        }
    }

    public static ProductMockData getInstance() {
        if (Objects.nonNull(instance)) {
            return instance;
        } else {
            synchronized (ProductMockData.class) {
                if (Objects.isNull(instance)) {
                    instance = new ProductMockData();
                }
                return instance;
            }
        }
    }
}
