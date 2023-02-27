package com.zmart.food.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Entity
@Table(name = "Product")
@JsonInclude(NON_NULL)
public class Product implements Serializable {
  private static final long serialVersionUID = 1323272533746093028L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @UpdateTimestamp
  @JsonFormat(pattern = "MM-dd-yyyy")
  @Column(name = "stockDate")
  private Date stockDate;

  @Transient
  @JsonFormat(pattern = "MM-dd-yyyy")
  @Column(name = "futureDate")
  private Date futureDate;

  @Column(name = "item", nullable = false)
  private String itemName;

  @Column(name = "code", nullable = false)
  private String itemCode;

  @JsonProperty("sellBy")
  @Column(name = "sellBy", nullable = false)
  private Integer sellby;

  @Column(name = "quality", nullable = false)
  private Integer quality;

  @Column(name = "specialCase", nullable = false, length = 1)
  private Integer specialCase;

  public Product() {}

  /**
   * @param stockDate
   * @param futureDate
   * @param itemName
   * @param itemCode
   * @param sellby
   * @param quality
   */
  public Product(
      final Date stockDate,
      final Date futureDate,
      final String itemName,
      final String itemCode,
      final Integer sellby,
      final Integer quality,
      final Integer specialCase) {
    this.stockDate = stockDate;
    this.futureDate = futureDate;
    this.itemName = itemName;
    this.itemCode = itemCode;
    this.sellby = sellby;
    this.quality = quality;
    this.specialCase = specialCase;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Date getStockDate() {
    return stockDate;
  }

  public void setStockDate(final Date stockDate) {
    this.stockDate = stockDate;
  }

  public Date getFutureDate() {
    return futureDate;
  }

  public void setFutureDate(final Date futureDate) {
    this.futureDate = futureDate;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(final String itemName) {
    this.itemName = itemName;
  }

  public String getItemCode() {
    return itemCode;
  }

  public void setItemCode(final String itemCode) {
    this.itemCode = itemCode;
  }

  public Integer getSellby() {
    return sellby;
  }

  public void setSellby(final Integer sellBy) {
    this.sellby = sellBy;
  }

  public Integer getQuality() {
    return quality;
  }

  public void setQuality(final Integer quality) {
    this.quality = quality;
  }

  public Integer getSpecialCase() {
    return specialCase;
  }

  public void setSpecialCase(final Integer specialCase) {
    this.specialCase = specialCase;
  }
}
