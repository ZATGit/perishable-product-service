package com.zmart.food.product.cache;

import com.zmart.food.product.exception.AppInternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@PropertySource("redis-local.properties")
@Component
@ConditionalOnProperty(prefix = "local.redis.server", name = "embedded", havingValue = "on")
public class EmbeddedRedis {

  @Value("${spring.cache.redis.port}")
  private int redisPort;

  private RedisServer redisServer;

  @PostConstruct
  public void startRedis() throws IOException {
    log.info("[REDIS] Starting Redis");
    Boolean redisPortAvailable = false;
    final int maxPortRange = 9999;
    while (!redisPortAvailable && redisPort <= maxPortRange) {
      try {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
        redisPortAvailable = true;
        log.trace("[REDIS] Connecting to port " + redisPort);

      } catch (final Exception ex) {
        if (ex.getMessage().contains("bind: Address already in use")) {
          log.trace("[REDIS] Port " + redisPort + " already in use");
          redisPort++;
        } else {
          throw new AppInternalServerErrorException();
        }
      }
    }
  }

  @PreDestroy
  public void stopRedis() {
    redisServer.stop();
  }
}
