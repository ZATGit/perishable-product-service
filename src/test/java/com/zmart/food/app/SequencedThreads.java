package com.zmart.food.app;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class SequencedThreads { // SequenceRun

  public static void main(final String[] bar) {
    final int threadCount = 5;
    final List<Thread> threads = new ArrayList<>();
    IntStream.rangeClosed(1, threadCount)
        .forEach(i -> threads.add(new Thread(SequencedThreads::logIt)));
    threadsJoiner(threads);
  }

  private static void threadsJoiner(final List<Thread> threads) {
    threads.forEach(
        thread -> {
          thread.start();
          try {
            thread.join();
          } catch (final InterruptedException e) {
            log.debug(e.getMessage());
          }
        });
  }

  private static void logIt() {
    log.info("{} ....", Thread.currentThread().getName());
  }
}
