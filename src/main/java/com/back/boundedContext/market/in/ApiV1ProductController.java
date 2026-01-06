package com.back.boundedContext.market.in;

import com.back.boundedContext.market.app.MarketFacade;
import com.back.boundedContext.market.domain.MarketMember;
import com.back.boundedContext.market.domain.Product;
import com.back.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/market/products")
@RequiredArgsConstructor
public class ApiV1ProductController {
    private final MarketFacade marketFacade;
    private final Rq rq;

    public record ProductDto(
            int id,
            LocalDateTime createDate,
            LocalDateTime modifyDate,
            int sellerId,
            String sellerName,
            String sourceTypeCode,
            int sourceId,
            String name,
            String description,
            long price,
            long salePrice
    ) {
        public static ProductDto from(Product product) {
            return new ProductDto(
                    product.getId(),
                    product.getCreateDate(),
                    product.getModifyDate(),
                    product.getSeller().getId(),
                    product.getSeller().getNickname(),
                    product.getSourceTypeCode(),
                    product.getSourceId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getSalePrice()
            );
        }
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<ProductDto> getProducts() {
        return marketFacade.findAllProducts()
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    @GetMapping("/mine")
    @Transactional(readOnly = true)
    public List<ProductDto> getMyProducts() {
        MarketMember seller = marketFacade.findMemberByUsername(rq.getActor().getUsername()).get();
        return marketFacade.findProductsBySeller(seller)
                .stream()
                .map(ProductDto::from)
                .toList();
    }
}