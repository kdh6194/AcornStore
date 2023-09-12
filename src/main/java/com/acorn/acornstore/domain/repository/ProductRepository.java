package com.acorn.acornstore.domain.repository;

import com.acorn.acornstore.domain.Product;
import com.acorn.acornstore.domain.ProductStatus;
import com.acorn.acornstore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserAndProductStatus(User seller, ProductStatus active);

    @Query("SELECT e FROM Product e WHERE e.productStatus = 'FINISHED' OR e.productStatus = 'CLOSED'")
    List<Product> findByUserAndProductStatusIsNotActive();

    Product findByProductIdAndUser(Long productId, User seller);

    @Modifying
    @Query("update Product p set p.productStatus = :status where p.closingAt < :now")
    int updateStatusForPastProducts(@Param("status") ProductStatus status, @Param("now") LocalDate now);
}
