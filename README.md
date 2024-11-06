## 동작원리 간단히
- 클라이언트 요청 -> 서블릿 컨테이너의 필터 -> 스프링 부트 컨트롤러 도달
- spring security config 등록 -> 특정 필터 생성 -> 클라이언트 요청 가로챔 -> 클라이언트가 가고 싶은 목적지 이전에 해당 클라이언트가 특정한 권한이 있는지를 체크한다

- 즉, 특정 경로의 요청이 오면 controller 클래스에 도달하기 전에 spring security가 필터에서 요청을 가로챔
  - client가 접근 권한이 있는지
  - 로그인 했는지
  - 해당되는 role이 있는지를 검증함
  - 다음을 **인가 작업**이라고 함
  - 이것을 수행하려면 **Security configuration class**를 등록해야 함 

## Security Config class
cf) build() : 생성자 대신에 사용하는 초기화 메소드. 가독성과 유지보수성 up, 매개변수가 많을 때, 불변 객체일때
- 커스터 마이즈를 통해서 특정 경로에 대한 인가 작업을 수행하게 함
- Security config 클래스를 등록하면 로그인, 리다이렉트 과정을 다 커스텀해줘야 함 -> 안하면 그냥 접근 거부 

- <사용하는 애노테이션>
  - @EnableSecurity : 이 config 클래스가 스프링 시큐리티에게서도 관리를 받음
  - @Configuration + @Bean
- 의존하는 클래스 및 인터페이스
  - SecurityFilterChain : 인터페이스
    
  - HttpSecurity
    - authorizeHttpRequests() : 특정 경로에 요청을 허가 or 거부 
      - requestMatchers() : 특정 경로에 대한 작업을 진행할 때. 
      - anyRequest() : requestMatchers로 처리하지 못한 나머지 경로
      - permitAll() : 모든 사용자에게 로그인을 하지 않아도 접근 가능 
      - hasRole() : 로그인이 진행되어야 함(http login form 필요) + 특정 role이 있어야 경로에 접근 가능
      - hasAnyRole() : 여러가지의 role을 설정 -> role값이 있으면 접근 가능
      - authenticated() : 로그인만 하면 모두 접근 가능
      - denyAll() : 로그인을 진행해도 모든 사용자의 접근 거부
      - 순서가 중요하기에!! 가장 아래에서 모든 경로에 대한 셋팅을 설정해야 함
    - formLogin() : 로그인 설정 처리
      - loginPage() : 로그인 페이지 경로
      - loginProcessingUrl : 프론트에서 받은 아이디, 비밀번호를 전달할 경로
      - permitAll() : 해당 경로에서 누구나 접근 가능
  - BCryptPassword:
    - 내가 하던 프로젝트에서는 회원이 입력한 pwd + salt(랜덤 번호) -> sha-256(내가 사용한 단방향 해시 암호 알고리즘)으로 돌린 값을 mysql에 저장
    - 스프링 시큐리티는 비크립트(단방향 해시 암호 알고리즘) 

## 깨알 정리
- JSON -> DTO 객체 자동 변환 : @PostMapping에서 @RequestBody를 통해 받은 JSON 데이터는 DTO 객체 안에 있는 Setter와 Getter로 변환
- DTO -> JSON으로 자동 변환 : @GetMapping에서 사용. 이때 Getter가 DTO 안에 반드시 있어야 한다
- 뷰 페이지를 반환하면서 Post를 받아야 할 때에는 @Controller로 충분히 가능

## 회원 가입 로직
- Role(역할)을 만들자
  - service 단에서 반드시 `ROLE_`로 시작할 것 -> ROLE_ADMIN, ROLE_USER
- SecurityConfig에서 회원가입 uri와 api(회원가입 form 전송 누르면 요청할 주소)를 모든 사람들이 접근할 수 있도록 경로 처리

## DB 기반 로그인 검증 로직
- UserDetailsService(미리 스프링에서 제공)를 상속 받아 CustomUserDetailsService 클래스를 만든다
- DB에 있는 회원으로부터 로그인을 검증하는 로직 -> 로그인 완료 후 ROLE 값에 따른 인증, 인가 


## 꺠알 정리 2 <세션과 토큰 로그인>
- 둘다 사용자 인증을 위한 방식


## DB 기반 로그인 검증 로직
1. 사용자가 "/loginProc"경로로 login.mustache에 있는 <form>의 정보를 넘긴다
2. 스프링에서 UsernamePasswordAuthenticationFilter가 정보를 가로채서 아이디와 패스워드를 추출하고 그것을 Authentication Manager에게 넘긴다
3. Authentication Manager는 CustomUserDetailsService를 호출해서 아이디와 패스워드를 넘긴다
4. CustomUserDetailsService는 DB에서 사용자의 정보를 조회하고 가져온다
5. 가져온 정보를 CustomUserDetails에 넘기고 CustomUserDetails는 정보를 가공한다 그리고 그 결과를 다시 CustomUserDetailsService에게 넘긴다
5. CustomUserDetailsService는 가공한 정보를 다시 Authentication Manager에게 넘긴다
6. Authentication Manager는 받아서 비밀번호를 검증하고 맞으면 Authentication 객체를 생성해 UsernamePasswordAuthenticationFilter에 반환
7. UsernamePasswordAuthenticationFilter는 Authentication 객체를 SecurityContext에 저장, SecurityContext는 SecurityContextHolder를 통해 애플리케이션 전역에서 접근 가능 -> 세션 id가 생성이 되면서 클라이언트에 전달됨
- Security Context : 세션 안에 있는 Authentication(사용자 정보) 저장소
- Security Context Holder : Authentication인 사용자 정보를 관리함 

## 깨알 정리 3 <Security Context>가 그럼 세션? 
- 반은 맞음
  - SecurityContext는 세션 안에 있음
 
## 만약 시큐리티를 사용하지 않고 세션 로그인 방식을 구현한다면?
- 세션을 db로 별도로 만들어줘야 함 -> 시큐리티는 자동으로 세션을 만들어줌
- 세션 id를 클라이언트에게 줘야 함 -> 시큐리티는 자동으로 클라이언트에게 세션 id를 전달
- 세션 만료 자동 처리 -> 시큐리티는 만료가 되면 자동으로 세션을 삭제함
- 보안성을 높이는 것의 구현 어려움
  - 세션 소멸 시간 지정
  - 중복 로그인 : 로그인 정보를 동시에 몇 개의 기기에서 유지할 수 있게 할 것인가?(ex: pc 1 + 핸드폰 1)
  - 고정 보호 : 해커가 세션 아이디를 만들어서 클라이언트에게 전달하게 유도하여 서버 세션에서 해커가 만든 세션 id를 가진 새로운 세션을 생성하게 만듦 -> 시큐리티에서는 클라이언트가 세션 id를 변경하지 못하게 막음 

## 깨알 정리 4 <세션 = 입국심사국?>
- 세션을 입국심사로 비유할 수 있다
  - 세션 : 요청이 들어올 때마다 사용자를 검증하는 곳 = 공항 입국심사국 : 입국자들을 대상으로 여권 등을 조회하며 검증한다 
  - 세션 : 로그인 = 공항 입국심사국 : 물론 아니지만, 외국인 등록이 완료되면 비자 기간동안 그 나라에서 채류가능 
  - 세션 ID = 외국인 주민등록증
  - 세션 만료 = 비자연장 

검역소 만료: 세션이 만료되면 다시 검증이 필요해지며, 이때 사용자는 재로그인이 필요할 수 있습니다. 세션 만료는 보안과 시스템 관리의 일환으로, 검역소에 일정 시간이 지나면 다시 상태를 확인하도록 하는 것입니다.
