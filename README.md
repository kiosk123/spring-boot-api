# Springboot를 이용한 API 개발

## 구성정보
* JPA 2.2
* java 버전 11
* bootstrap v4.3.1
* Thymeleaf 3
* Thymeleaf 이클립스 플러그인 - http://www.thymeleaf.org/eclipse-plugin-update-site/

## 스프링 프로젝트 구성하기
* [Spring Initializr 사이트 활용](https://start.spring.io/)

## API 테스트
* [POSTMAN](https://www.postman.com/)
* [Katalon](https://www.katalon.com/)

## 참고사이트
* [Spring 가이드 문서](https://spring.io/guides)
* [Spring Boot 참고 문서](https://docs.spring.io/spring-boot/docs/)
* [쿼리 파라미터 로그 남기기](https://github.com/gavlyukovskiy/spring-boot-data-source-decorator)


## 챕터별 설명
  - 챕터01 : 회원API를 가지고 API 개발 기본을 익힘
  - 챕터02  
    - 간단한 주문 조회 API - 엔티티 직접 노출  
    - 간단한 주문 조회 API - 엔티티를 DTO로 변환  
    - 간단한 주문 조회 API - 엔티티를 DTO로 변환 - 페치 조인 최적화  
    - 간단한 주문 조회 API - JPA에서 DTO로 바로 조회  
    
    

 
## 조회용 데이터 설정정보
* com.study.springboot.service.InitDb를 참고

## 핵심포인트
* 엔티티를 API에 직접 노출하지 말것
* API 노출시 DTO를 이용하여 노출할 것
* API 노출시 필요한 데이터만 노출할 것 - 클라이언트와 강한 커플링 방지