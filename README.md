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
- 

- 사용하는 애노테이션
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
