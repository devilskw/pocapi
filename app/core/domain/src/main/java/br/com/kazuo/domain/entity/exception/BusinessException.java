package br.com.kazuo.domain.entity.exception;

public class BusinessException extends RuntimeException {
    private BusinessCategoryExceptionEnum category;
    private Object[] args;

    public BusinessException(BusinessCategoryExceptionEnum category, String message, Object[] args) {
        super(message);
        this.category = category;
        this.args = args;
    }

    public BusinessCategoryExceptionEnum getCategory() {
        return category;
    }
}
