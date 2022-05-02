package br.com.kazuo.entrypoint.product.v1;

import br.com.kazuo.domain.entity.product.Product;
import br.com.kazuo.domain.usecase.product.ProductUseCase;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RequestMapping(path = "/v1/produtos")
@RestController
@Validated
public class ProductV1Controller {
    private ProductUseCase productUseCase;

    @Autowired
    public ProductV1Controller(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper newProduct(@Valid @RequestBody BaseProductDTO product) {
        return new ResponseWrapper(new ProductDTO(productUseCase.createProduct(new Product(product.getName()))));
    }

    @GetMapping("/{codigo_produto}")
    public ResponseWrapper findProduct( @PathVariable(name = "codigo_produto") @Min(value = 1L) long id ) {
        return new ResponseWrapper<ProductDTO>(new ProductDTO(productUseCase.findProduct(id)));
    }

    @PatchMapping("/{codigo_produto}")
    public ResponseWrapper updateProduct(@PathVariable(name = "codigo_produto") @Min(value = 1L) long id, @Valid @RequestBody BaseProductDTO product ) {
        return new ResponseWrapper<ProductDTO>(new ProductDTO(productUseCase.updateProduct(new Product(id, product.getName()))));
    }

    @DeleteMapping("/{codigo_produto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirProdutoAsync(@PathVariable(name = "codigo_produto") @Min(value = 1L) long id ) {
        productUseCase.deleteProduct(id);
    }
}
