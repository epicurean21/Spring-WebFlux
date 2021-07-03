package com.example.webflux.controller;

import com.example.webflux.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {

    /**
     * Mono.just() 안에 <String>에 들어간다. 미리 결정된 값을 넣어준다.
     * Container라고 생각할 수 있다.
     * Optional.of("ABC") 이러한 container안에 담아서 컨테이너가 제공하는 flatMap등의 기능을 사용할 수 있다.
     */
    @GetMapping("/test")
    public Mono<String> test() {
        String s = "Hello, World!";
        Mono<String> m = Mono.just("Hello, World!"); // 더 많은 기능을 제공한다 (Mono container가 제공)
        //return m;
        return Mono.just("Hello, World!");
    }

    @GetMapping("/{idx}")
    public Mono<String> webClientTest() {
        return Mono.just("WebClient Test");
    }

    @GetMapping("/2/{idx}")
    public Mono<String> webClientTest2() {
        return Mono.just("WebClient Test2");
    }

    /**
     * WebClient - 여러 스레드가 동시에 적용 가능. 기존에 Async Rest Template과 유사한 것
     * 각 Method 한 번 호출할때마다, 하나의 성정을 넣는 방식으로 한다.
     */
    WebClient webClient = WebClient.create();
    static final String URL1 = "http://localhost:8080/{idx}";

    @GetMapping("/rest")
    public Mono<String> rest(int idx) {

        Mono<ClientResponse> res = webClient.get() // Method는 get을 사용하겠다 명시
                .uri(URL1, idx) // URL과 named parameter 자리에 값을 넣는다.
                .exchange(); // 서버를 호출하는 exchange() 메소드

        /**
         * 이 경우 WebClient 호출이 될까? - No
         * Why?
         * .exchange()로 정의하는 것 만으로는 실행되지 않는다. Reactive Streams API를 이해해야한다.
         * Mono는 Publisher Interface를 구현한 것. Publisher <-> Subscriber를 가지고 Reactive Stream을 만드는건
         * Publisher는 만들어 놓는다고 해서 Publish 하지 못한다. Subscriber가 Subscribe하기 전까지 Data를 쏘거나 만들지 않는다.
         * Mono<ClientResponse> res = webClient.get().uri(URL1, idx).exchange();
         * 이러한 코드는 단지 Publisher. 얘는 비동기 작업을 수행하는걸 정의만 한 것.
         * 적어도 res.subscribe() 라도 해 주어야한다 그래야 실행이 된다.
         * 얘를 언제 Subscribe해야할까?
         * Mono<String>을 리턴을 하면, Spring에서 subscribe 해준다.
         */
        /*
        ClientResponse cr = null;
        Mono<String> body = cr.bodyToMono(String.class); // cr안에있는 body를 mono로 만들어 주는것
         */
        /**
         * Mono<T> 모노로 감싸져 있다면 T를 꺼내온다음 그걸 조작할 수도있지만, Reactive Programming Style에서는
         * Mono에 담겨있으면, 담겨져있는 container의 어떤 함수를 적용해서 그 결과를 Mono에 다시 담아서 리턴한다.
         * .map() 같은 함수를 사용한다.
         * Stream<String> st;
         * st.map(s -> s + 1); // 이런식으로 Stream에서 Mono에 담긴걸 꺼내서 변환하는 방식이 아닌, 그 자체의 컨테이너 함수를 map
         */
        //Mono<Mono<String>> body = res.map(clientResponse -> clientResponse.bodyToMono(String.class)); // clientResponse를 받아서 String을 감싼 Mono를 리턴하기 때문에, Mono<Mono<>> 가 된다.

        /**
         * Mono<Mono<String>> 을 어떻게 다시 Mono<String>으로 해줄까?
         * 그럴 때 flatMap을 사용하자
         * map이 Mono안에 container에 원소에 함수를 적용해서 다시 container에 집어 넣는것
         * flatMap으로 한번 납작하게, Mono<String>으로 만들어주자.
         */
        Mono<String> body = res.flatMap(clientResponse -> clientResponse.bodyToMono(String.class));
        //return body;
        // 정리하면,
        return webClient.get().uri(URL1, idx).exchange().flatMap(c -> c.bodyToMono(String.class));
        //return Mono.just("Hello WebClient!");
    }

    /**
     * GetMapping ("/rest") 에서는 int idx를 그대로 갖다가 사용했다.
     * 이걸 Lambda 식으로 값, 결과를 받아서 두 번째 api를 호출하는 방법
     * 이때 flatMap의 활용과 Mono안 Container의 변화를 잘 확인하고 사용하자
     */

    static final String URL2 = "http://localhost:8080/2/{idx}";

    @GetMapping("/rest/2")
    public Mono<String> rest2(@RequestParam("idx") int idx) {                                            // Return type
        return webClient.get().uri(URL1, idx).exchange()                            // Mono<ClientRespponse>
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)) // Mono<String> .. clientResponse를 꺼내서 String으로 만들어서 mono에 넣음
                .flatMap(res1 -> webClient.get().uri(URL2, res1).exchange()) // Mono<ClientResponse> .. res1이 clientString을 가져와서 다시 ClientResponse로
                .flatMap(c -> c.bodyToMono(String.class)); // Mono<String> .. clientResponse를 받아와서 다시 string으로 flatMap
    }

    /**
     * 별개의 Thread에 담아서 return
     * map 과 flatMap의 차이를 이해하자.
     * work는 string을 return 하니, 원소를 꺼내서 string으로 다시 만들어서 Mono로 넣어주는 map을 사용하면 된다.
     */

    private final MyService myService;

    @GetMapping("/rest/3")
    public Mono<String> rest3(@RequestParam("idx") int idx) {
        return webClient.get().uri(URL1, idx).exchange()                            // Mono<ClientRespponse>
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)) // Mono<String> .. clientResponse를 꺼내서 String으로 만들어서 mono에 넣음
                .flatMap(res1 -> webClient.get().uri(URL2, res1).exchange()) // Mono<ClientResponse> .. res1이 clientString을 가져와서 다시 ClientResponse로
                .flatMap(c -> c.bodyToMono(String.class)) // Mono<String> .. clientResponse를 받아와서 다시 string으로 flatMap
                .map(c -> myService.work(c)); // 이걸 map으로 넣어야 할까 ? flatMap으로 넣어야할까 : map ! work의 return type은 String.
    }

    /**
     * netty안에 worker thread를 이용해 exchange() 함수를 호출한다. 만약 Myservice안에 work가 오랜 수행시간이 필요하면, thread가 block되버릴수있다.
     * 동기적으로 수행시키면 thread를 묶는다 수행이 오래거리는동안 block..
     * 이걸 비동기적으로 수행하기 위해 @Async annotation을 걸어줄 수 있다.
     */

}
