# Spring Boot + Redis (Lettuce, Cache) 
## Spring Boot와 Redis를 사용한 간단한 회원 관리 API 구현 예제
<br>

### API Spec
|API Endpoint|HTTP Method|Request Body|Description|
|--|--|--|--|
|/redis-api/save|POST|{<br>"id": id, <br>"description": desc<br>}|사용자 등록|
|/redis-api/get/{id}|GET||사용자 조회|
|/spring-redis/users/{username}|DELETE|사용자 삭제|
|/spring-redis/users|GET|전체 사용자 조회|

<br>

### 참고
>- https://ozofweird.tistory.com/entry/Spring-Boot-Redis-Lettuce%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EA%B0%84%EB%8B%A8%ED%95%9C-API-%EC%A0%9C%EC%9E%91
>- https://daddyprogrammer.org/post/3870/spring-rest-api-redis-caching/