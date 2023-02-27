package com.zmart.food.product.repository;

import com.zmart.food.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional
@Repository
public interface PerishableProductRepository extends JpaRepository<Product, Long> {

  // Id
  <T> Collection<T> findAllByIdIn(List<Long> id, Class<T> type);

  // ItemName
  Product findByItemNameIgnoreCase(@Param("itemName") @NonNull String itemName);

  // ItemCode
  Product findByItemCodeIgnoreCase(@Param("itemCode") @NonNull String itemCode);

  // ItemCode & ItemName (Contains)
  List<Product> findByItemCodeContainsOrItemNameContainsIgnoreCaseOrderByItemCodeAsc(
      @Param("itemName") @NonNull String itemName, @Param("itemCode") @NonNull String itemCode);

  // Quality: SellBy, Desc & Asc
  List<Product> findByQualityOrderBySellbyDesc(@Param("quality") @NonNull Integer quality);

  // Quality: SellBy, Desc
  List<Product> findByQualityOrderBySellbyAsc(@Param("quality") @NonNull Integer quality);

  // Quality: ItemName, Desc
  List<Product> findByQualityOrderByItemNameDesc(@Param("quality") @NonNull Integer quality);

  // Quality: ItemName, Asc
  List<Product> findByQualityOrderByItemNameAsc(@NonNull Integer quality);
}
