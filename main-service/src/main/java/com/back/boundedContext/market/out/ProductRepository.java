package com.back.boundedContext.market.out;

import com.back.boundedContext.market.domain.MarketMember;
import com.back.boundedContext.market.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findBySeller(MarketMember seller);
    List<Product> findByOrderByIdDesc();
    List<Product> findBySellerOrderByIdDesc(MarketMember seller);
}