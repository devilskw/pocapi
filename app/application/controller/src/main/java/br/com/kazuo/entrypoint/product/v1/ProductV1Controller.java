package br.com.kazuo.entrypoint.product.v1;

import br.com.kazuo.domain.entity.exception.CustomException;
import br.com.kazuo.domain.entity.product.DefaultProduct;
import br.com.kazuo.domain.usecase.product.ProductManagement;
import br.com.kazuo.domain.usecase.product.ProductUsecase;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RequestMapping(path = "/v1/produtos")
@RestController
@Validated
public class ProductV1Controller {
    private ProductUsecase productUseCase;

    public ProductV1Controller(ProductManagement productManagement) {
        this.productUseCase = productManagement;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<ProductDTO> newProduct(@Valid @RequestBody BaseProductDTO product) throws CustomException {
        return new ResponseWrapper<>(
                new ProductDTO(productUseCase.createProduct(new DefaultProduct(product.getName()))));
    }

    @GetMapping("/{codigo_produto}")
    public ResponseWrapper<ProductDTO> findProduct(@PathVariable(name = "codigo_produto") @Min(value = 1L) long id)
            throws CustomException {
        return new ResponseWrapper<>(new ProductDTO(productUseCase.findProduct(id)));
    }

    @PatchMapping("/{codigo_produto}")
    public ResponseWrapper<ProductDTO> updateProduct(@PathVariable(name = "codigo_produto") @Min(value = 1L) long id,
            @Valid @RequestBody BaseProductDTO product) throws CustomException {
        return new ResponseWrapper<>(
                new ProductDTO(productUseCase.updateProduct(new DefaultProduct(id, product.getName()))));
    }

    @DeleteMapping("/{codigo_produto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirProdutoAsync(@PathVariable(name = "codigo_produto") @Min(value = 1L) long id)
            throws CustomException {
        productUseCase.deleteProduct(id);
    }
}
