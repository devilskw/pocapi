package br.com.kazuo.domain.entity.product;

import br.com.kazuo.domain.entity.exception.CustomException;
import br.com.kazuo.domain.entity.exception.CustomExceptionCategoryEnum;
import br.com.kazuo.domain.entity.exception.CustomException.CustomExceptionBuilder;

public class DefaultProduct implements Product {

    private Long id;
    private String name;

    public DefaultProduct(Long id, String name) throws CustomException {
        if (name == null) {
            throw new CustomExceptionBuilder("O nome do produto não pode ser nulo",
                    CustomExceptionCategoryEnum.CLIENT_INVALID_PARAMETER_OR_DATA, null)
                            .addArg("name", String.valueOf(name)).build();
        }
        this.id = id;
        this.name = name;
    }

    public DefaultProduct(String name) throws CustomException {
        this(null, name);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
