package br.com.kazuo.config.spring;

import br.com.kazuo.dataprovider.product.ProductDataProvider;
import br.com.kazuo.dataprovider.product.ProductCommandRepository;
import br.com.kazuo.dataprovider.product.ProductQueryRepository;
import br.com.kazuo.domain.usecase.product.ProductFacadeUsecase;
import br.com.kazuo.domain.usecase.product.ProductDsGateway;
import br.com.kazuo.domain.usecase.product.ProductUseCase;
import br.com.kazuo.infra.repository.mock.product.ProductCommandMock;
import br.com.kazuo.infra.repository.mock.product.ProductQueryMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeansConfig {

    @Bean
    public ProductUseCase productUseCase() {
        return new ProductFacadeUsecase(productDsGateway());
    }

    @Bean
    public ProductDsGateway productDsGateway() {
        return new ProductDataProvider(productCommandRepository(), productQueryRepository());
    }

    @Bean
    public ProductCommandRepository productCommandRepository() {
        return new ProductCommandMock();
    }

    @Bean
    public ProductQueryRepository productQueryRepository() {
        return new ProductQueryMock();
    }

}