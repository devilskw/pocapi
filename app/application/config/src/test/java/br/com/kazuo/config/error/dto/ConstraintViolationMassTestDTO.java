package br.com.kazuo.config.error.dto;

import javax.validation.constraints.Min;

public class ConstraintViolationMassTestDTO {

    @Min(1L)
    private Long test;

    @Min(0L)
    private Long noMessagePropertyEquivalent;

    public ConstraintViolationMassTestDTO(Long test, Long noMessagePropertyEquivalent) {
        this.test = test;
        this.noMessagePropertyEquivalent = noMessagePropertyEquivalent;
    }

    public String testMethodInvalidArgument(@Min(1) Long id) { return null; }

}
