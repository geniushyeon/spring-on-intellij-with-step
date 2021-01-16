# 1강
# 1. intelliJ에서 tomcat 설정
1. Open - 첨부파일 내에 있는 pom.xml 선택 - Open as Project 선택
2. 오른쪽 상단 Edit Configuration -  왼쪽 + 버튼  - Tomcat - local 선택
3. Tomcat Home: Tomcat 설치된 경로 지정해주기(나의 경우 `/usr/local/Cellar/tomcat/9.0.40/libexec`)
4. 포트번호 지정(default: 8080 / oracle에서 쓰고 있을 가능성이 높으므로 `9090`으로 변경해줌)
5. 아래쪽 Warning - fix - `프로젝트명.war exploded` 선택
6. OK 눌러서 완료

# 2. 컴파일 시 java: error: release version 5 not supported 오류 해결방법
1. pom.xml 파일에
```xml
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
```
추가<br/>
2. 그래도 오류가 나네?
- Preferences - Compiler - Java Compiler에서 `Target bytecode version`을 1.8로 수정
  - Maven으로 빌드할 때 pom에 있는 정보를 참조하긴 하나 intelliJ로 빌드할 때는 프로그램에 설정되어 있는 정보로 빌드하기 때문에 빌드를 어떻게 하느냐에 따라 다를 수 있다.
  - File - Project Structure - language level도 체크

# 2강
# Maven 빌드하기
1. 프로젝트 import 후
2. Run - Edit Configuration
3. 좌측 상단 + 버튼 클릭 - Maven 선택
4. Name - `prod`, Command Line - `package`, Profiles - `prod` 입력해주기
5. Run 버튼 클릭<br/>
   
클릭했더니
- source option 5 is no longer supported. use 6 or later. 이런 오류가 났다.
  - 맥에서 maven을 brew로 설치하였을 때 발생하는 것으로 추정

- 해결방법: `pom.xml`에 `properties` 추가
  - 해결하고 보니 1강에서와 같은 오류였음.. 추후에 학원 수업 시간에서도 미리 설정해줘야 할 듯

```xml
 <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <!-- 추가 -->
        <maven.compiler.source>1.6</maven.compiler.source>
        <maven.compiler.target>1.6</maven.compiler.target>

        <spring.version>4.2.0.RELEASE</spring.version>
```
![screenshot](img/step02.png)
- properties 안에 추가해주고 나니 잘 됨.

# 3강
# 1. IoC
Inversion of Control, 제어의 역전
- 임의의 객체는 자신이 사용할 다른 객체를 선택/생성하지 않음.
- 객체 스스로가 향후 어떻게 만들어지고 어디서 사용될지 알 수 없음.
- 모든 제어 권한은 다른 객체(상위)에게 위임
- 장점
  - 프로그램 소스 코드의 유연성 및 확장성 증가
  - 스프링에서 제공하는 모든 기능의 기초가 되는 기반 기술임.

## 1.1. Object Factory
- 스프링의 역할: 자체적으로 객체를 생성하새 관리하는 일종의 Factory 역할
  - Bean: 이 때, Spring에서 제어권을 가지고 직접 생성, 관리하는 객체

### 1.1.1. Factory 클래스
- org.springframework.beans.factory.`BeanFactory`를 이용해 객체를 생성, 관리할 수 있음.
- 일반적으로는 BeanFactory를 상속하고 기능이 추가된 org.springframework.context.`ApplicationContext` 인터페이스를 통해 어플리케이션 전반에 걸쳐 제어를 담당함.

### 1.1.2. ApplicationContext
- IoC를 적용해서 관리할 모든 객체에 대한 제어권 담당.
- 코드는 설정 정보(`Java config` or `XML`)를 통해 획득
  - 각 Bean에 대한 정보를 기입 후 사용
- `Annotaion 기반`으로 동작하기 때문에 각 Bean들의 등록 빈도 감소
```xml
<bean id="emailService" class="com.service.EmailService"></bean>
```
- 장점
  - 본연의 기능 및 관심사에 집중 가능
  - Bean의 종합적 관리 자동화

### 1.2. Spring에서 관리하는 Bean의 수명
- Spring Container의 컨테이너 내에 한 개의 객체로 생성되며, 이를 `Singleton Registry`로 관리
- 강제로 제거하지 않는 한 스프링 컨테이너 내에 계속해서 유지됨
- Singleton Registry를 사용하지 않는다면
  - 클라이언트의 요청 1개당 1개의 객체 생성
    - 즉, 클라이언트의 요청이 많아질수록 객체가 많이 생성되며, 이는 서버의 메모리를 차지함
    - 서버의 사용량이 매우 높아져 과부하에 걸릴 수 있음

# 2. DI
## 2.1. 의존 관계(Dependency)
- A가 B를 의존한다: 방향성 존재
  - B에서 많은 변화가 일어날 때 A도 영향을 받게 됨
## 2.2. DI
Dependency Injection, 의존 관계 주입, 의존성 주입
- Spring에서 객체 간의 관계 설정 의도를 명확히 표현하는 용어
### 2.2.1. 의존 관계 주입 조건
- 클래스 모델이나 코드에서는 보통 인터페이스를 사용하기 때문에 런타임 시점에 어떤 구체적인 클래스가 적용될지 알 수 없음
  - 런타임 시점의 의존 관계는 `ApplicationContext`와 같은 제3의 객체가 결정함.
  - 의존 관계는 (의존할) 객체에 대한 레퍼런스를 제3의 객체가 제공(주입, DI)해줌으로써 만들어짐.

## 2.3. Annotation
### 2.3.1. @Autowired
- Spring Framework에서 지원하는 Dependency 정의 용도의 Annotation으로, Spring Framework에 종속적
### 2.3.2. @Inject
- Spring 3부터 지원
- 특정 Framework에 종속되지 않는 어플리케이션을 구성하기 위해서는 @Autowired보다 @Inject를 사용할 것을 권장
- (치호선생님은 deprecated라고 하셨음. 확인 필요)
- pom.xml에 설정
```xml
<!-- @Inject -->
<dependency>
  <groupId>javax.inject</groupId>
  <artifactId>javax.inject</artifactId>
  <version>1</version>
</dependency>
```

# 5강
# 1. AOP
Aspect-Oriented Programming, 관점 지향 프로그래밍
- 어플리케이션의 핵심적인 기능에서 부가적인 기능을 분리하여 `Aspect`라는 모듈로 구분하여 설계하고 개발하는 방법
- ✔︎ OOP(Object-Oriented Programming, 객체 지향 프로그래밍)를 돕는 보조적인 기술이며, OOP를 대체하는 기술은 아님
- 어플리케이션을 다양한 측면에서 독립적으로 모델링, 설계, 개발할 수 있도록 함
## 1.1. Aspect
- 객체 지향 기술에서 부가기능 모듈을 부르는 이름
- Aspect 자체로는 애플리케이션의 핵심 기능을 담고 있지는 않지만, 요소마다의 공통 관심사항이 될 수 있음
- 어플리케이션을 구성하는 한 가지 측면
### 1.1.1. Aspect 개념이 적용되어있지 않은 어플리케이션
  - 부가 기능이 핵심 기능의 모듈에 침투해 들어가면서 설계와 코드가 지저분해짐
  - 부가 기능 코드가 여기저기 메소드에 마구 흩어져 나타나고 코드는 중복됨
### 1.1.2. Aspect 개념을 염두에 두고 분리한 어플리케이션
- 코드 사이에 침투한 부가 기능을 독립적인 모듈인 `Aspect`로 구분
- 각기 성격이 다른 부가 기능은 다른 측면에 존재하여 독립적으로 그 코드를 살펴볼 수 있음

## 1.2. 용어 정리
- Target: 부가 기능을 부여할 대상(ex. 클래스)
- Advice: 타겟에게 제공할 부가 기능을 담은 모듈. 객체로 정의하기도 하지만 메소드 레벨에서 정의하기도 함.
- Join Point: Advice가 적용될 수 있는 위치. 
  - 스프링 프록시 AOP에서 Join Point는 메소드의 실행 단계임.
- Pointcut: Advice를 적용할 조인 포인트를 선별하는 작업 또는 그 기능을 정의한 모듈. 
  - 스프링의 포인트컷은 특정 타입 객체의 특정 메소드를 선정하는 기능.

# 2. @AspectJ
Java Annotation을 사용한 일반 Java class로 관점을 정의하는 방식
- pom.xml에 설정
```xml
<!-- Spring AOP + AspectJ -->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-aop</artifactId>
  <version>4.2.0</version>
</dependency>

<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectj</artifactId>
  <version>1.8.0</version>
</dependency>

<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjweaver</artifactId>
  <version>1.8.6</version>
</dependency>
```

# 3. Annotation
## 3.1. @Before
## 3.2. @After
## 3.3. @AfterReturning
- Pointcut에 지정한 함수가 실행된 후 `결과값을 활용`
## 3.4. Around
- @Before와 @After를 통합하여 실행 가능(함수 실행 전후)

# 4. Pointcut 활용
## 4.1. Pointcut 표현식
- 효과적으로 Pointcut의 클래스와 메소드를 설정하는 언어
- 보다 복잡하고 세밀한 Pointcut 필터 기준 설정
  - 클래스와 메소드 이름, 정의된 패키지, 메소드 파라미터, 리턴 값, 부여된 annotaion, 구현한 인터페이스, 상속한 클래스
```text
execution([접근제한자 패턴] 리턴타입패턴 [패키지 타입패턴]이름패턴 (파라미터 타입패턴) [throws 예외 패턴])
```