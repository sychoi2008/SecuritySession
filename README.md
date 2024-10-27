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
- 
