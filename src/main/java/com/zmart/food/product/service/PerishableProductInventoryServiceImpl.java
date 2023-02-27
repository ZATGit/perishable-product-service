package com.zmart.food.product.service;

import com.zmart.food.product.cache.*;
import com.zmart.food.product.exception.AppIllegalArgumentException;
import com.zmart.food.product.model.Product;
import com.zmart.food.product.model.Request.*;
import com.zmart.food.product.model.Response.*;
import com.zmart.food.product.repository.PerishableProductRepository;
import com.zmart.food.product.repository.ProductProjectionIdsAndNamesOnly;
import com.zmart.food.product.utils.QualityRequestEnum;
import com.zmart.food.product.utils.SpecialCaseProductsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.zmart.food.product.exception.ExceptionUtils.*;

@Slf4j
@CacheConfig(cacheNames = "products")
@Service
public class PerishableProductInventoryServiceImpl implements PerishableProductInventoryService {

  private final PerishableProductRepository perishableProductRepository;

  @Autowired
  public PerishableProductInventoryServiceImpl(
      final PerishableProductRepository perishableProductRepository) {
    this.perishableProductRepository = perishableProductRepository;
  }

  /**
   * Track day-to-day values.
   *
   * <p>An item can never have a negative quality.
   *
   * @param product
   * @param day
   * @return Product product
   */
  private static Product updateEachProductQualityAndFutureDate(
      final Product product, final Integer day) {
    IntStream.range(0, day).parallel().forEach(i -> endOfEachDayInventoryTracking(product, day));
    return product;
  }

  /**
   * At the end of each day, quality decrements by 1.
   *
   * <p>At the end of each day, sellby decrements by 1.
   *
   * <p>Once the sell by date has passed, quality degrades twice as fast.
   *
   * <p>The quality of an item is never more than 50.
   *
   * @param updateProduct
   * @return void
   */
  private static void endOfEachDayInventoryTracking(
      final Product updateProduct, final Integer day) {
    updateFutureDate(updateProduct, day);
    if (updateProduct.getSpecialCase() != 0) {
      handleSpecialQualityProducts(updateProduct);
    } else {
      handleRemainingSpecialProducts(updateProduct);
    }
  }

  /**
   * Tracks metrics and offset date.
   *
   * <p>Convert date to LocalDateTime.
   *
   * <p>Add one day.
   *
   * <p>Convert LocalDateTime to date.
   *
   * <p>
   *
   * @param updateProduct
   * @return void
   */
  private static void updateFutureDate(final Product updateProduct, final Integer day) {
    final Date currentDate = new Date();
    LocalDateTime localDateTime =
        currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    localDateTime = localDateTime.plusDays(day);
    updateProduct.setFutureDate(
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
  }

  /**
   * Handle special remaining items.
   *
   * <p>At the end of each day, quality decrements by 1.
   *
   * <p>An item can never have a negative quality.
   *
   * <p>At the end of each day, sellBy decrements by 1.
   *
   * <p>Once the sell by date has passed, quality degrades twice as fast.
   *
   * <p>At the end of each day, quality decrements by 1.
   *
   * <p>The quality of an item is never negative.
   *
   * <p>
   *
   * @param updateProduct
   * @return Product updateProduct
   */
  private static Product handleRemainingSpecialProducts(final Product updateProduct) {
    updateProduct.setSellby(updateProduct.getSellby() - 1);
    if (updateProduct.getQuality() > 0) {
      endOfDayQualityDegradationStatus(updateProduct);
    }
    return updateProduct;
  }

  /**
   * Quality degrades after sell by date. Calculate this.
   *
   * <p>After sell by date, quality degrades twice as fast.
   *
   * <p>
   *
   * @param updateProduct
   * @return void
   */
  private static void endOfDayQualityDegradationStatus(final Product updateProduct) {
    if (updateProduct.getSellby() >= 0) {
      updateProduct.setQuality(updateProduct.getQuality() - 1);
    } else {
      if (updateProduct.getQuality() >= 2) {
        updateProduct.setQuality(updateProduct.getQuality() - 2);
      } else {
        updateProduct.setQuality(updateProduct.getQuality() - 1);
      }
    }
  }

  /**
   * Edge cases to handle special quality items.
   *
   * <p>Some types of items don't follow normal deprecation rules:
   *
   * <p>1. SPECIAL_CASE_1 (example: Corn on the Cob)
   *
   * <p>Quality increments daily by 1 as the sell by date approaches 0 and continues to increase in
   * quality.
   *
   * <p>Will not go-over 50.
   *
   * <p>*
   *
   * <p>2. SPECIAL_CASE_2 (example: 3lb Ground Beef) * *
   *
   * <p>As sell by date approaches, quality increments by 1. * *
   *
   * <p>Quality increments by 2 when 10 days or less. * *
   *
   * <p>The quality drops to 0 after the sell by date.
   *
   * <p>3. SPECIAL_CASE_3 (example: Twinkies)
   *
   * <p>Never needs to be sold.
   *
   * <p>Never decreases in quality.
   *
   * <p>
   *
   * @param specialProduct
   * @return Product specialProduct
   */
  private static Product handleSpecialQualityProducts(final Product specialProduct) {
    final SpecialCaseProductsEnum enumNumber =
        SpecialCaseProductsEnum.values()[specialProduct.getSpecialCase() - 1];
    switch (enumNumber) {
      case SPECIAL_CASE_1:
        specialProduct.setSellby(specialProduct.getSellby() - 1);
        specialProduct.setQuality(
            handleSpecialQualityProductsThatIncreaseQuality(specialProduct.getQuality() + 1));
        break;
      case SPECIAL_CASE_2:
        specialProduct.setSellby(specialProduct.getSellby() - 1);
        if (specialProduct.getSellby() < 0) {
          specialProduct.setQuality(0);
        } else if (specialProduct.getSellby() <= 10) {
          specialProduct.setQuality(
              handleSpecialQualityProductsThatIncreaseQuality(specialProduct.getQuality() + 2));
        } else {
          specialProduct.setQuality(
              handleSpecialQualityProductsThatIncreaseQuality(specialProduct.getQuality() + 1));
        }
        break;
      case SPECIAL_CASE_3:
        break;
    }
    return specialProduct;
  }

  /**
   * Edge case to handle special quality items that increase in quality.
   *
   * <p>An item's quality is never more than 50.
   *
   * <p>
   *
   * @param quality
   * @return Integer results
   */
  private static Integer handleSpecialQualityProductsThatIncreaseQuality(final Integer quality) {
    Integer result = 50;
    if (quality <= result) {
      result = quality;
    }
    return result;
  }

  // *Inventories Start*

  @CacheableAllProducts
  public ProductAllProductsResponse getAllProductsInventory() {
    final List<Product> result = perishableProductRepository.findAll();
    return new ProductAllProductsResponse().withProductList(result).withCount(result.size());
  }

  @NoCache(reason = "expected date change with repeated arg")
  public ProductFutureDateResponse getAllProductsForFutureDateInventory(
      final ProductFutureDateRequest request) {
    checkAppNullPointerForIntegerAndStringArgs(request.getDayOffset());
    final List<Product> productList = this.getAllProductsInventory().getProductList();
    final List<Product> result =
        productList.stream()
            .parallel()
            .map(product -> updateEachProductQualityAndFutureDate(product, request.getDayOffset()))
            .collect(Collectors.toList());
    return new ProductFutureDateResponse().withProductList(result).withCount(result.size());
  }

  @CacheableItemName
  public ProductByItemNameResponse getProductByItemNameInventory(
      final ProductByItemNameRequest request) {
    checkProductNullPointerForStringsArgs(request.getItemName());
    checkProductIllegalArgForStringArgs(request.getItemName());
    final Product result =
        perishableProductRepository.findByItemNameIgnoreCase(request.getItemName());
    if (!(result instanceof Product)) {
      return new ProductByItemNameResponse().withProduct(new Product());
    }
    return new ProductByItemNameResponse().withProduct(result);
  }

  @CacheableItemCode
  public ProductByItemCodeResponse getProductByItemCodeInventory(
      final ProductByItemCodeRequest request) {
    checkProductNullPointerForStringsArgs(request.getItemCode());
    checkProductIllegalArgForStringArgs(request.getItemCode());
    final Product result =
        perishableProductRepository.findByItemCodeIgnoreCase(request.getItemCode());
    if (!(result instanceof Product)) {
      return new ProductByItemCodeResponse().withProduct(new Product());
    }
    return new ProductByItemCodeResponse().withProduct(result);
  }

  /**
   * Find all itemName & itemCode containing given params
   *
   * <p><b>Use case:</b> Search "Organic Spinach" when item names could be "Farm2Table Organic
   * Spinach" or "Organic Spinach Extra Fresh!"
   *
   * <p><b>Use case:</b> Search for all organic foods by item code with "Org" prefix
   *
   * @param request
   * @return List of Products
   */
  @CacheableNameOrCode
  public ProductByItemNameOrItemCodeResponse getProductByItemNameOrItemCodeInventory(
      final ProductByItemNameOrItemCodeRequest request) {
    checkProductNullPointerForStringsArgs(request.getItemName(), request.getItemCode());
    checkProductIllegalArgForStringArgs(request.getItemName(), request.getItemCode());
    final List<Product> result =
        perishableProductRepository
            .findByItemCodeContainsOrItemNameContainsIgnoreCaseOrderByItemCodeAsc(
                request.getItemCode(), request.getItemName());
    return new ProductByItemNameOrItemCodeResponse()
        .withProductList(result)
        .withCount(result.size());
  }

  @CacheableQuality
  public ProductByQualityResponse getAllProductsByQualityInventory(
      final ProductByQualityRequest request) {
    final List<Product> result;
    final Integer quality = request.getQuality();
    final String orderByAttribute = request.getOrderByAttribute();
    final String orderByAscOrDesc = request.getOrderByAscOrDesc();
    checkAppNullPointerForIntegerAndStringArgs(quality, orderByAttribute, orderByAscOrDesc);
    if (orderByAttribute.equalsIgnoreCase(QualityRequestEnum.SELLBY.name())) {
      result = handleSellByOrderByAscOrDesc(quality, orderByAscOrDesc);
      return new ProductByQualityResponse().withProductList(result).withCount(result.size());
    } else if (orderByAttribute.equalsIgnoreCase(QualityRequestEnum.ITEMNAME.name())) {
      result = handleItemNameOrderByAscOrDesc(quality, orderByAscOrDesc);
      return new ProductByQualityResponse().withProductList(result).withCount(result.size());
    } else {
      throw new AppIllegalArgumentException();
    }
  }

  @CacheEvictCreateOrUpdate
  public ProductCreateOrUpdateResponse createOrUpdateProductsInventory(
      final ProductCreateOrUpdateRequest request) {
    final List<Product> createdOrUpdateProductList = new ArrayList<>();
    request
        .getProductList()
        .forEach(
            product -> {
              checkProductNullPointerForStringsArgs(product.getItemName(), product.getItemCode());
              checkProductNullPointerForIntegerArgs(product.getQuality(), product.getSellby());
              checkProductSpecialCaseAllowed(product.getSpecialCase());
              final Product productByItemCodeResult =
                  perishableProductRepository.findByItemCodeIgnoreCase(product.getItemCode());
              if (productByItemCodeResult instanceof Product) {
                log.trace(
                    "Product \""
                        + product.getItemName()
                        + "\" is present in table. [id:"
                        + product.getId()
                        + "] --> [UPDATE]");
              } else {
                log.trace(
                    "Product \""
                        + product.getItemName()
                        + "\" is not present in table. --> [INSERT]");
              }
              createdOrUpdateProductList.add(product);
              perishableProductRepository.save(product);
            });
    return new ProductCreateOrUpdateResponse()
        .withProductList(createdOrUpdateProductList)
        .withCount(createdOrUpdateProductList.size());
  }

  @CacheEvictDelete
  public ProductDeleteResponse deleteProductsInventory(
      final ProductDeleteRequest productDeleteRequest) {
    final Collection<ProductProjectionIdsAndNamesOnly> deletableProductIdsResult =
        perishableProductRepository.findAllByIdIn(
            productDeleteRequest.getIdList(), ProductProjectionIdsAndNamesOnly.class);
    final List<Long> deletableProductIds =
        deletableProductIdsResult.stream()
            .map(ProductProjectionIdsAndNamesOnly::getId)
            .collect(Collectors.toList());
    perishableProductRepository.deleteAllById(deletableProductIds);
    log.trace("Deleted ids: " + deletableProductIds);
    return new ProductDeleteResponse()
        .withDeletedProducts(deletableProductIdsResult)
        .withCount(deletableProductIdsResult.size());
  }

  // *Inventories End*

  private List<Product> handleSellByOrderByAscOrDesc(
      final Integer quality, final String orderByAscOrDesc) {
    if (orderByAscOrDesc.equalsIgnoreCase(QualityRequestEnum.DESC.name())) {
      return perishableProductRepository.findByQualityOrderBySellbyDesc(quality);
    } else if (orderByAscOrDesc.equalsIgnoreCase(QualityRequestEnum.ASC.name())) {
      return perishableProductRepository.findByQualityOrderBySellbyAsc(quality);
    } else {
      throw new AppIllegalArgumentException();
    }
  }

  private List<Product> handleItemNameOrderByAscOrDesc(
      final Integer quality, final String orderByAscOrDesc) {
    if (orderByAscOrDesc.equalsIgnoreCase("desc")) {
      return perishableProductRepository.findByQualityOrderByItemNameDesc(quality);
    } else if (orderByAscOrDesc.equalsIgnoreCase("asc")) {
      return perishableProductRepository.findByQualityOrderByItemNameAsc(quality);
    } else {
      throw new AppIllegalArgumentException();
    }
  }
}
