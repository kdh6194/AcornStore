# acorn-store

**프로젝트 이름** : acorn-store

**프로젝트 소개** : ...과 관련된 쇼핑몰을 만들어 보고 싶어서 제작하게 되었습니다.

**프로젝트 기간** : 2023.08.23 - 2023.~

**팀원**

김도현
- 유저 로그인/로그아웃
- 쇼핑몰 물품 조회/구매
      
백지영
- 쇼핑몰 물품 판매
- 유저 마이페이지

**사용된 기술**

> lang : Java,(프론트)
>
> tools : spring boot,(프론트)
>
> DB : MySQL
>
> distrib : AWS EC2, AWS RDB, github action
>
> dependency : Spring Web, Spring Security, Spring boot devtools, jjwt, Spring Oauth2, lombok, JPA, Spring Cache

**구축 절차(모듈 아키텍트 디자인)**
![모듈 아키텍트 디자인]()

**ERD**
![프로젝트에 구성된 ERD]()

**테이블 명**
|컬럼명|컬럼명2|컬럼명3|컬럼명4|컬럼명5|컬럼명6|컬럼명7|컬럼명8|컬럼명9|
|--|--|--|--|--|--|--|--|--|
|mock1|mock2|mock3|mock4|mock5|mock6|mock7|mock8|mock9|

## 구현 기능

**로그인**

로그인 : 이메일 및 비밀번호 입력 // jwt인증 사용

로그아웃 : 해당 계정의 인증을 무효화 // 로그아웃 후 Access Token 및 Refresh Token 사용불가

회원가입 : 이메일,비밀번호,전화번호,주소,성별 입력
- 프로필 이미지 등록
- 비밀번호 영문자,숫자 조합으로 8자 이상 20자 이하
- 이메일 중복확인
  회원탈퇴 : 회원상태 deleted로 갱신 // 정보 보관용

OAuth 기반 로그인(소셜 로그인) : 카카오/google/네이버/깃허브 기반 인증 추가

전화번호 기반 인증 : 문자 SMS API와 연동하여 인증

로그인 일정시간 잠금 : 로그인 시도를 5회 실패하면 계정을 일정시간 동안 잠금

**쇼핑몰 물품 구매/조회**

쇼핑몰 전체 물품 조회 : 전체 페이지에서 현재 가지고 있는 물품을 모두 보여줌
- pagination 사용,재고 없는 물품은 보여주지 않음

쇼핑몰 상세 물품 조회 : 각 쇼핑몰 상품별 이미지, 상품명, 옵션 가격을 나타냄

쇼핑몰 물품 장바구니에 담기/수정 : 장바구니에 담긴 물품내역을 수정할 수 있으면 그에 따라 가격이 달라짐

장바구니 주문 : 유저는 재고과 예산이 일치하면 구매할 수 있음, 결제에 필요한 정보를 입력해서 주문기능 구현

쇼핑몰 특정 물품명이나 카테고리로 검색 : 카테고리나 특정 물품명을 검색하여 조회할 수 있음// 재고가 없는 물품을 볼 수 도 있음

쇼핑몰 특정 조건 배열 조회 : 조회수, 구매수, 인기순 배열 등 추가 기능 구현

**쇼핑몰 물품 판매**

**마이페이지**

## 데일리 스크럼

**매일 진행 예정(해당 날짜/해당 요일)**

진행 시간 : 15 ~ 30분 (매일 오후 10시 10분 or 매일 오전 9시)

진행 장소 : gather or kakao talk

어제 어떤 작업했는지

오늘은 무엇을 할 계획인지

작업 중 에러나 이슈가 있었는지 진행 상황 공유

## 코드 리뷰

**코드 리뷰 진행날짜**
```java
System.out.println("리뷰할 코드");
System.out.print("여기에 작성하면 되요");
```
## 사용된 문서

**스웨거 주소**
Click [Swagger]()

**노션 주소**
Click [Notion]([https://www.notion.so/AcornStore-892531ed392d4e5e96c4a89a1f339942](https://www.notion.so/AcornStore-892531ed392d4e5e96c4a89a1f339942?pvs=4)

### Reference Documentation

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.15-SNAPSHOT/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.15-SNAPSHOT/gradle-plugin/reference/html/#build-image)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.7.15-SNAPSHOT/reference/htmlsingle/index.html#web.security.oauth2.client)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.15-SNAPSHOT/reference/htmlsingle/index.html#web)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.7.15-SNAPSHOT/reference/htmlsingle/index.html#using.devtools)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.7.15-SNAPSHOT/reference/htmlsingle/index.html#web.security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.15-SNAPSHOT/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

### Guides

* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
