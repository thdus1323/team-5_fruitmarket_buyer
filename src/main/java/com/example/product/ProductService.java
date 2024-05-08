package com.example.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    //상품 목록보기
    public List<ProductResponse.ListDTO> getProductList(){
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    //상품 상세보기
    public ProductResponse.DetailDTO getProductDetail(Integer id){
        Product product = productRepository.findById(id);
        return new ProductResponse.DetailDTO(product);
    }

    public Product findById(Integer id) {
        Product product = productRepository.findById(id);
        return product;
    }
}