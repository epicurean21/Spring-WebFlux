package com.example.webflux.completableFuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureTest {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture.runAsync(() -> log.info("runAsync"))
                         .thenRun(() -> log.info("thenRunAsync"));
        log.info("exit");

        ForkJoinPool.commonPool().shutdown(); // thread pool을 아무것도 설정하지 않으면, Java 7 부터는 ForkJoinPool의 commonPool의 worker로 동작하낟
        // 즉, ForkJoinPool의 commonPool 내의 thread로 위 completableFuture의 runAsync log 작성을 수행한다.
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);

        final List<Integer> numbers = List.of(1, 2, 3,4, 5);
        final List<String> strings = List.of("hi", "hello", "nihao", "annyoung");

        printNumbers(numbers);
        printStrings(strings);
    }

    static void printNumbers(final List<Integer> numbers) {
        numbers.forEach(n -> log.info("print numbers: {}", n));
    }

    static void printStrings(final List<String> strings) {
        strings.forEach(s -> log.info("print strings: {}", s));
    }

    static CompletableFuture<Integer> printNumbersAsync(final CompletableFuture<Integer> future) {
        return future.toCompletableFuture();
    }
}
