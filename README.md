## WebFlux
###Spring WebFlux Study ✏️


Spring WebMVC
Servlet API & 컨테이너를 위한것  
Blocking + Synchronize 방식.
Spring Webflux
반응형-스택 웹 프레임워크.
Non-blocking 이다. 
Non-blocking: 중단되지 않는것, 블로킹 반대. 네트워크 통신 완료때까지 기다리지 않고 다른 작업을 수행, 효율이나 반응속도가 블로킹보다 뛰어나다.
적은 수의 쓰레드로 동시 처리를 가능하게 하면서 더 적은 하드웨어 리소스로 확장 가능한 Non-blocking web stack이 필요해서 만들어짐.

Spring Framework 5.x 의 특징, Kotlin 지원, JDK 8 지원, Reactive Programming

WebFlux가 무조건 Webmvc 보다 무조건 빠르지는 않음. 
속도가 빠른거보다는 적은 리소스로 많은 트래픽을 감당. 하나의 스레드가 하나의 작업을 해서 스레드풀의 스레드 개수보다 많은 request가 들어왔을때 queue에서 대기하게 되어 CPU/메모리 리소스는 많은데 스레드가 부족해 성능 저하되는 Web MVC문제를 해결하기 위해 (넷플리스법?) 사용됨.



함수형 프로그래밍 (Functional Programming)
함수가 일급객체로 쓰인다. 반환값 인자가 된다. 람다, 메소드 체이닝으로 결과를 만듬
일급 객체 (First-class citizens): 컴퓨터 프로그래밍 언어 디자인에서 일반적으로 다른 객체들에 적용 가능한 연산을 모두 지원하는 객체
특징:
Input과 Output 존재
외부 환경으로부터 철저하게 독립적
같은 Input에 있어서 항상 같은 Output을 만들어낸다.

[비 함수형 프로그래밍 예제 초콜릿 공장. 각각의 프로세스가 서로에게 영향을 주며 역할 분담을 한다]

[함수형 프로그래밍. 각각의 프로세스가 독립적, 서로에게 Input (함수)를 준다]




[함수형 프로그래밍 JS 예시]

[출처] https://www.youtube.com/watch?v=jVG5jvOzu9Y


명령형 프로그래밍 vs. 함수형 프로그래밍
명령형 프로그래밍
프로그램이 상태를 갖고, 실행되는 순서에 따라서 이전 상태를 바꿔 가면서 계산하는 방식
함수형 프로그래밍
어떤 값을 주고 함수를 실행해서 변환된 결과 값이 나오는 것을 연쇄적으로 호출해서 최종 결과값을 유도
명령형은 변수에 상태가 있고, 계산한걸 그자리에 덮어쓴다.
함수형은 함수로 Input, Output을 주고, 그 다음 또 다른 함수를 호출해서 결과값을 연쇄적으로 유도해낸다.

변수의 ‘immutable’
함수형 언어에서 모든 데이터는 변경이 불가능 (‘immutable’) 해야한다. 이로인해 부작용이 덜하다.

“Reactive”라는 것은, Reactive Programming
변화에 반응하게 만들어진 프로그래밍 모델.
Non-Blocking은 Reactive 하는 것이다.
Blocking 대신에 Operation이 완료되거나 
비동기 데이터 Stream으로 Non-blocking application을 구현하는 programming

MVC vs. WebFlux 동작흐름
MVC: 
Request -> Dispatcher Servlet -> Handler Mapper - Controller - B/L - Controller - ViewResolver …
WebFlux:

Request -> HttpHandler - WebHandler - HandlerMapper / Handler Adapter - Controller - B/L …


Spring WebFlux도 Spring MVC와 마찬가지로 프론트 컨트롤러 패턴을 중신으로 설계되어 있다.
중앙의 WebHandler인 DispatcherHandler가 요청 처리의 공유 알고리즘을 제공하고, 실제 작업은 구성 가능한 위임 컴퓨넌트에 의해 실행된다.

Spring Boot WebFlux [Code]
Spring Webflux에는 reactive, non-blocking하게 HTTP 요청을 처리할 수 있도록 WebClient라는 모듈을 제공한다. 기존의 RestTemplate과 같은 역할 하지만, non-blocking하다라는 점에서 차이가 있다.
내부적으로 WebClient는 HTTP 클라이언트 라이브러리에 위임하는데, 디폴트로 Reactor Netty의 HttpClient를 사용한다. Reactor Netty 외에도, Jetty의 HttpClient를 지원하며, 다른 라이브러리도 ClientHttpConnector에 넣어주면 사용할 수 있다.
https://inyl.github.io/programming/2018/03/10/springboot2_api.html
WebFlux는 기존의 Annotation 방식, 새로운 Router & Handler (Functional) 방식 두 가지를 모두 지원한다. 어떤걸 사용하는게 좋을지 알아봐야한다.
Annotated Controller
Spring MVC에서 사용하던 방식입니다. MVC에서 사용하던 어노테이션들을 WebFlux에서도 그대로 사용가능합니다.
Functional Endpoints
Java 8 lambda style routing과 handling 방식입니다. callback 형태로써 요청이 있을 때만 호출된다는 점이 Annotated Controller 방식과의 차이점입니다.
Mono & Flux
WebFlux에서는 Reactive한 특징때문에 Plain Object를 사용할 수 없고 반드시 Mono, Flux와 같은 Publisher Object로 감싸서 반환해야합니다.
Mono와 Flux는 Reactive Streams 인터페이스 중에 데이터(시퀀스)를 제공하는 Publisher의 구현체.
Mono와 Flux의 차이점은 데이터를 전송하는 갯수의 차이가 있습니다.
Mono : 0 - 1 개의 데이터를 전달
Flux : 0 - N 개의 데이터를 전달
Mono

Flux

Router에서 Handler로 Mapping 해주는 형식은 어렵지 않음. 다만 Handler에서 Mono, Flux 형식으로 반환해주는 형식을 익힐 필요가있음. 

Router & Handler 방식

/*
RouterFunction:
ServerRequest를 인자로 받아 Mono<HandlerFunction>을 리턴한다. 여기서는 GreetingHandler를 리턴..?
router function이 매치가 되면, 그에 맞는 handler function이 리턴이 된다. 그렇지 않으면 Mono.empty(), 빈 Mono 를 리턴
 */

@Configuration
public class GreetingRouter {
    @Bean
    public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN))
                , greetingHandler::hello);
    }
}
/*
HandlerFunction:
ServerRequest를 인자로 받아서 Mono<ServerResponse>를 리턴.
Annotation 기반에서 @RequestMapping 메소드들의 Body와 같은 역할을 한다.
 */
@Component
public class GreetingHandler {
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                             .body(BodyInserters.fromValue("Hello, Spring!"));
    }
}


HTTP 요청은 HandlerFunction으로 처리.
ServerRequest을 받아 지연(비동기)되는 ServerResponse(즉, Mono<ServerResponse>)을 반환하는 함수이다.
HandlerFunction은 Immutable 한 객체이다.
Annotation 기반 프로그래밍 모델의 RequestMapping 메소드의 본체.

PersonRepository personRepository
PersonHandler handler = new PersonHandler(repository)

RouterFunction<ServerResponse> route = route()
	.GET("/person/{id}", accept(APPLICATION_JSON), handler::getPerson)
	.build()

public class PersonHandler() {
	public Mono<ServerResponse> getPerson(ServerRequest request) {
		//..
	}
}


WebClient
Spring WebFlux에는 HTTP 요청을 실행하는 클라이언트가 포함되어있다. WebClient에는 Reactor에 따른 함수를 기초로 하는 API가 있다. 
스레드 및 동시성을 처리를 할 필요없이, 비동기 로직의 선언적 구성을 가능하게 한다.
이는 완전히 논블로킹이며, 스트리밍을 지원하고, 서버 측에서 요청과 응답의 컨텐츠를 인코딩 및 디코딩하는데 사용되는 코덱에 의존하고 있다.
WebClient는 비동기 (Async) / 논블로킹 (Non-blocking)을 지원하는 HTTP Request Client (API) 이다.

WebClient.create()
WebClient.create(String baseUrl)
WebFlux, Reactive Programming Knowledge
요청당 스레드 모델
일반적인 Multi-Threaded Programming 구조로는, (default) 200개의 Thread가 Thread Pool에서 대기하며 데이터베이스 호출과 같은 대부분의 interaction은 Block 되어 있다.
멀티 스레드이기에 두 명의 사용자가 Web Server에 Request를 한다면 각기 다른 스레드로 핸들링한다.
이런것을 요청당 Thread Model 이라 한다.

위 그림을 보면, 각 스레드는 단일 요청만 핸들링한다. 그러나 여전히 할당 받은 스레드는 Block 된다. 더군다나 동시성을 처리하기 위해 할당된 스레드들은 Context switch로 인해 상당한 비용을 낭비하게 된다. 때문에 웹 어플리케이션에서 요청이 점점 많아 질 수록 기대하던 성능이 미치지 못하게 된다.
결과적으로 스레드를 덜 사용하면서 더 많은 요청을 처리 할 수 있는 모델인 reactive programming이 등장하게 된 계기가 되었다.
출처: https://timewizhan.tistory.com/entry/번역-Concurrency-in-Spring-WebFlux 

Reactive Programming
Reactive programming은 데이터 flow 관점에서 프로그램을 구조화하기 편하게 한다. 때문에 완전한 non-blocking 환경에서, 리소스를 이전 모델보다 더 낫게 사용하여 보다 성능이 높은 동시성을 구현할 수 있게 한다. 그러나 thread based 와 근본적으로 다른점은 reactive programming은 비동기라는 것이다. 즉, 프로그램의 flow를 동기 작업에서 비동기 이벤트 스트림으로 변환된다.
예를 들어, 데이터베이스를 호출할 때 비동기로 작업한다. 그렇기 때문에 호출에 대한 응답(리턴 값)은 구독한 publisher가 된다. 이후 subscriber는 이후 이벤트를 처리한다.

위 그림에서 첫 번째 모델과 다른 점은 reactive programming은 스레드를 중점으로 보지 않고 비동기 이벤트 스트림을 중점으로 본다.
publisher와 subscriber는 같은 스레드일 필요 없으며 이 모델은 스레드 자원을 보다 효율적으로 사용하는데 도움이 된다.
Event Loop
이번 세션에서는 reactive programming에서 어떻게 적은 스레드로 동시성의 성능을 높이는지 알아보도록 하겠다.
먼저 event loop 모델이다.

위 그림은 reactive programming에서 비동기 아이디어를 나타낸 event loop의 개념 그림이다.
- event loop는 단일 스레드로 계속 동작한다. (물론 코어 갯수에 만큼 event loop 가 있다.)
- event loop는 순차적으로 event queue의 event를 처리한다. 그리고 platform에 callback을 등록한 후 바로 리턴한다.
- event loop는 작업 완료 callback을 trigger 를 발생시키고 결과를 반환한다.
해당 event loop 모델은 Node.js, Netty, Nginx를 비롯하여 여러 플랫폼에서 구현된다. 때문에 전통의 플랫폼인 Apache HTTP Server, Tomcat, JBoss보다 더 나은 확장성을 가진다.

Reactor Netty
Reactor Netty는 Spring Boot WebFlux starter에 담긴 기본 서버이다. Netty가 기본으로 생성하는 스레드를 살펴보자.
시작할 때, 다른 의존성이나 WebClient를 사용하지 않을 것이다. 그래서 Spring WebFlux 어플리케이션이 시작되면, 기본 스레드를 볼 수 있다.

서버의 기본 스레드를 제외하고 Netty는 요청 처리를 위해 worker thread를 생성한다. (위 그림은 쿼드 코어를 가진 머신의 결과로 일반적으로 CPU 코어 갯수보다 많지 않다.)
Netty는 event loop 모델을 사용한다. 그럼 Netty가 Java NIO를 활용하여 어떻게 event loop를 구현했는지 알아보자.

EventLoopGroup이 하나 이상의 EventLoop를 관리한다. (단, EventLoop를 가용할 수 있는 코어 갯수보다 더 만드는 것은 추천하지 않는다.) 그리고 EventLoopGroup은 EventLoop를 각 Channel에 할당한다. 이 말 즉슨, 각 Channel은 EventLoop와 같은 스레드에서 실행된다.

Scheduling Options in WebFlux
앞서 언급한 것처럼 reactive programming은 적은 스레드 환경에서 non-blocking 을 사용한다. 이 말은 즉, 코드 flow에 blocking이 포함되어 있으면 성능이 좋지 않을 것이라는 것을 의미한다. (event loop가 전체적으로 blocking으로 인해 freeze 되므로)
그렇다면 오래 진행되는 프로세스나 blocking 작업은 어떻게 reactive programming에서 다뤄야 할까?
사실 최선의 방법은 피하는 것이 상책이다. 그렇지만 꼭 해야 한다면, 스케줄링을 사용하면된다.
Spring WebFlux는 data flow chain 에서 처리 중인 작업을 다른 스레드풀로 전환하는 메커니즘을 제공한다.

