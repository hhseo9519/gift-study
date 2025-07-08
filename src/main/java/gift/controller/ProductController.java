package gift.controller;


import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RestController는 @Controller + @ResponseBody
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;//여기에 service layer를 넣어서 아래에서 호출 할 수 있게 함

    public ProductController(ProductService productService) {
        this.productService = productService;// 생성자를 통한 productService 주입
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAllProduct() {
        return new ResponseEntity<>(productService.findAllProduct(), HttpStatus.OK);
    }//ResponseEntity는 내가 HTTP 응답을 커스터마이징 할 수 있다는데 의의가 있다. ResponseEntity는 기본 제공되는 객체이다.

    //질문 1번. <T> 이건 뭐지? 제네릭?


    @GetMapping("/{id}")//
    public ResponseEntity<ProductResponseDto> findProduct(@PathVariable Long id){

        return new ResponseEntity<>(productService.findProduct(id),HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(
            @RequestBody @Valid ProductRequestDto productRequestDto) {
        //@RequestBody = Json에 있는게 그대로 RequestBody뒤에 나온 객체로 들어간다
        return new ResponseEntity<>(productService.addProduct(productRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequestDto productRequestDto) {
        return new ResponseEntity<>(productService.updateProduct(id, productRequestDto), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new 	ResponseEntity<>(HttpStatus.OK);
    }


}

