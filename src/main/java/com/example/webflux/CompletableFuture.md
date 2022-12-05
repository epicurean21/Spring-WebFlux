# Completable Future

#### 우선 Future가 무엇일까?

- Java 5 이후 등장한 비동기 프로그래밍 기법이다.
  
  - 비동기는 결국 새로운 Thread를 만들고 할당해서 Background 에서 작업을 수행하는것.  

- 비동기 작업의 결과를 담고있는 Object 이다.
  
  - 비동기 작업의 결과를 가져오는 방법은 여러가지가 있지만, Future Object를 이용해서 get method 사용하여 결과를 가져오는게 가장 원시적이고 Simple 한 방식이다.

- Listenable Future가 존재한다.
  
  - Callback 구조로 결과가 완료가 되는 시점에 걸어놓고 결과를 가져온다.

- Future는 결국 Future 결과를 가져오는 Get 이라는 method를 제공한다.
  
  - ```java
    Future f;
    f.get() // 결과 가져오기.
    ```
  
  - get() 메소드는 비동기 작업이 완료가 안됐다면 대기하고, 에러가 발생했다면 get() 에서 에러를 리턴한다.

- 결국 비동기 작업의 결과를 쓰는건, 비동기 작업을 수행하는 코드 안에서 수행된다.

#### CompletableFuture는 뭐가 다를까?

- 이름에서 힌트를 얻을 수 있듯, CompletableFuture Object 를 가지고, 비동기 작업을 직접 완료하도록 수행할 수 있다.

- ListenableFuture에서 Future task 를 작성하면 비슷한 역할을 구현할 수 있지만, 좀 복잡하다.

- CompletableFuture를 쓰면 간단하게 비동기 작업의 결과를 만들어낼 수 있다.
  
  - CompletableFuture 리스트의 모든 값이 완료될 때 까지 기다릴지, 아니면 하나의 값만 완료되길 기다릴지 선택할 수 있다는 것이 장점이다. 
  
  - CompletableFuture Class는 Lambda 표현식과 Pipeline 을 활용하면 구조적으로 우아하게 코딩할 수 있다.

```java
 CompletableFuture<Integer> f = CompletableFuture.compleatedFuture(1);
 // completetedFuture static method를 사용하면 이미 작업이 완료된 future object를 만들 수 있다.
 System.out.println(f.get()); // 이미 결과가 완료된 Future object이기에 리턴이 가능
```

```java
CompletableFuture<Integer> f = new ComplatbleFuture<>(); // 작업이 완료되지 않은 상태의 CompletableFuture를 생성

f.get(); // 무한 대기에 빠진다. ! 아직 작업이 완료된 상태가 아니다. 
```

위와같이 완료되지 않은 비동기 대기 상태의 Completable Future 를 만들어놓으면, get() 메소드 실행 시 완료되지 않았기에 무한 대기에 빠진다. 이를 완료시켜주는 작업이 필요하다.

```java
CompletableFuture<Integer> f = new ComplatbleFuture<>(); // 작업이 완료되지 않은 상태의 CompletableFuture를 생성
f.complete(2); // 작업이 완료됐다.
f.get(); // 결과 2를 받아올 수 있다.
```

새로운 Thread 생성 없이도 비동기 프로그래밍이 가능함을 알 수 있다.



CompletableFuture 에다가 Background에서 돌아가는 새로운 Thread를 생성해서 작업을 할당해보자.

```java
CompletableFuture.runAsync(() -> log.info("runAsync"));

log.info("exit");

ForkJoinPool.commonPool().shutdown();

```


