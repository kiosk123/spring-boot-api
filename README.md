# Springboot를 이용한 API 개발

## 구성정보
* JPA 2.2
* java 버전 11
* bootstrap v4.3.1 - 구성은 했지만 사용하지는 않음
* Thymeleaf 3 - 구성은 했지만 사용하지는 않음
* Thymeleaf 이클립스 플러그인 - http://www.thymeleaf.org/eclipse-plugin-update-site/

## 스프링 부트 프로젝트 구성하기
* [Spring Initializr 사이트 활용](https://start.spring.io/)

## gradle 의존성 설정

```gradle
plugins {
	id 'org.springframework.boot' version '2.3.5.RELEASE'
	id 'io.spring.dependency-management' version '1.0.10.RELEASE'
	id 'java'
}

group = 'springboot.jpa'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
  implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6' //운영에서는 사용하지 말 것
  implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-devtools'
	implementation 'com.fasterxml.jackson.datatype:jackson-datatype-hibernate5'

	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation('org.springframework.boot:spring-boot-starter-test') {
		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	}
}

test {
	useJUnitPlatform()
}
```

## 현재 프로젝트의 의존관계 보기 gradlew 이용
```bash
./gradlew dependencies —configuration compileClasspath
```

## API 테스트
* [POSTMAN](https://www.postman.com/)
* [Katalon](https://www.katalon.com/)

## 참고사이트
 - [Spring 가이드 문서](https://spring.io/guides)
 - [Spring Boot 참고 문서](https://docs.spring.io/spring-boot/docs/)
 - [쿼리 파라미터 로그 남기기](https://github.com/gavlyukovskiy/spring-boot-data-source-decorator)
    - 그레이들에서 다음과 같이 설정
    - implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6' 
    - 운영에서는 사용하지 말 것


## 챕터별 설명
  - 챕터01 : 회원API를 가지고 API 개발 기본을 익힘
  - 챕터02 : 지연로딩과 조회 성능 최적화
    - 간단한 주문 조회 API - 엔티티 직접 노출  
    - 간단한 주문 조회 API - 엔티티를 DTO로 변환  
    - 간단한 주문 조회 API - 엔티티를 DTO로 변환 - 페치 조인 최적화  
    - 간단한 주문 조회 API - JPA에서 DTO로 바로 조회  
  - 챕터03 : 컬렉션 조회 최적화
    - 주문 조회 API - 엔티티 직접 노출
    - 주문 조회 API - 엔티티를 DTO로 변환 : 페치 조인 최적화
    - 주문 조회 API - 엔티티를 DTO로 변환 : 페이징과 한계 돌파
    - 주문 조회 API - JPA에서 DTO 직접 조회
    - 주문 조회 API - JPA에서 DTO 직접 조회 : 컬렉션 조회 최적화
    - 주문 조회 API - JPA에서 DTO로 직접 조회, 플랫 데이터 최적화
  
    

 
## 조회용 데이터 설정정보
* `com.study.springboot.service.InitDb`를 참고

## 핵심포인트
* 엔티티를 API에 직접 노출하지 말것
* API 노출시 DTO를 이용하여 노출할 것
* API 노출시 필요한 데이터만 노출할 것 - 클라이언트와 강한 커플링 방지

## OSIV (Open Session In View) 관련 내용
**스프링은 OSIV 옵션의 기본값**은 `spring.jpa.open-in-view: true` 값이다.  
이 값이 true이면 영속성 컨텍스트(1차캐시)가 응답(Response)가 나갈때 까지 살아있다.  
즉 `@Transactional` 이 붙은 메소드가 실행된 이후에도 영속성 컨텍스트(1차캐시)가 살아있다.  
다만 이런 경우에는 수정은 불가능하고 읽기만 가능하다.  
이런 점 때문에 뷰에서까지 지연로딩 기능을 십분 활용할 수 있다는 점이 장점이다.  
다만 OSIV의 치명적인 문제는 실시간 트래픽이 중요할 경우 이전략은 디비 커넥션을 오래 물고 있기 때문에  
커넥션이 모자라서 장애로 이어질 수 있는 위험이 있다.  

**이 옵션을 끄고 사용할 경우 뷰나 컨트롤러에서 지연로딩의 이점을 살릴 수 없기 때문에**  
**서비스나 리포지토리 계층에서 지연로딩되는 필드를 강제로 호출시킨후 로딩 후 뷰에 반환한다.**