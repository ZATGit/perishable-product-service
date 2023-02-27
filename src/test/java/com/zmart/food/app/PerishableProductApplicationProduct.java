package com.zmart.food.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ActiveProfiles("h2")
@ExtendWith(MockitoExtension.class)
public class PerishableProductApplicationProduct implements MockProductList {

  @Test
  public void contextLoads() {}

  @Test
  public void testListIsGrowable() {
    final List<String> stringList = new ArrayList<>();
    IntStream.range(0, 8).forEach(i -> stringList.add(Integer.toBinaryString(i)));
  }
}
