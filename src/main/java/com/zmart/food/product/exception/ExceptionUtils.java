package com.zmart.food.product.exception;

import com.zmart.food.product.utils.SpecialCaseProductsEnum;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;

@NoArgsConstructor
public class ExceptionUtils {

  /** Converts FQDN to class name. */
  public static String getClassName(final Class<?> fQDN) {
    final String strFQDN = String.valueOf(fQDN);
    return strFQDN.split("\\.")[strFQDN.split("\\.").length - 1];
  }

  /**
   * Thrown by implicit Integer-to-String type conversion; i.e. {"itemCode":9}.
   *
   * <p>Not thrown by implicit String-to-Integer type conversion; i.e. {"dayOffset": "10"}
   *
   * @throws AppIllegalArgumentException
   */
  public static void checkProductIllegalArgForStringArgs(@NonNull final String... strArgs) {
    Arrays.stream(strArgs)
        .forEach(
            strArg -> {
              if (strArg.matches("-?\\d+")) {
                throw new AppIllegalArgumentException();
              }
            });
  }

  /**
   * Thrown by key typo; i.e. {"qualXty":10,...,...}.
   *
   * <p>Not thrown by implicit type conversion; i.e. {"quality":"10",...,...}.
   *
   * <p>Not thrown by conditional arg typo; i.e. {...,"orderByAttribute":"selBy",...}
   *
   * <p>Not thrown by conditional arg type mismatch; i.e. {...,"orderByAttribute":10,...}
   *
   * @throws AppNullPointerException
   */
  public static void checkAppNullPointerForIntegerAndStringArgs(
      @Nullable final Integer intArg, @Nullable final String... strArgs) {
    final Consumer consumer =
        (obj) -> {
          throw new AppNullPointerException();
        };
    if (!(intArg instanceof Integer)) {
      // call functional interface method
      consumer.accept("");
    }
    Arrays.stream(strArgs)
        .forEach(
            strArg -> {
              if (!(strArg instanceof String)) {
                consumer.accept("");
              }
            });
  }

  /**
   * Thrown by non-conditional arg type mismatch NPE; i.e. {...,"itemName":10,...}
   *
   * <p>Thrown by key typo; i.e. {"itemNXme":"",...,...}.
   *
   * <p>Not thrown by implicit type conversion; i.e. {...,"orderByAttribute":10,...}.
   *
   * <p>Not thrown by conditional arg typo; i.e. {...,"orderByAttribute":"selBy",...}
   *
   * @throws AppNullPointerException
   */
  public static void checkProductNullPointerForStringsArgs(final String... strArgs) {
    Arrays.stream(strArgs)
        .forEach(
            strArg -> {
              if (!(strArg instanceof String)) {
                throw new AppNullPointerException();
              }
            });
  }

  /**
   * See checkProductNullPointerForStringsArgs
   *
   * @throws AppNullPointerException
   */
  public static void checkProductNullPointerForIntegerArgs(final Integer... intArgs) {
    Arrays.stream(intArgs)
        .forEach(
            intArg -> {
              if (!(intArg instanceof Integer)) {
                throw new AppNullPointerException();
              }
            });
  }

  /** Checks whether special case value is allowed to avoid Out of Bounds exception. */
  public static void checkProductSpecialCaseAllowed(final Integer... intArgs) {
    Arrays.stream(intArgs)
        .forEach(
            intArg -> {
              if (!(intArg <= SpecialCaseProductsEnum.values().length & intArg > -1)) {
                throw new AppSpecialCaseException();
              }
            });
  }
}
