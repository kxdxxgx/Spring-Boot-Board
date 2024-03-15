<h1>인트라넷 게시판</h1>
<p>4가지 다른 환경에서 구현한 인트라넷 게시판 프로젝트</p>
<p>· 팀 구성 - 1인 프로젝트(BE, FE)</p>
<p>· 상세 역할 - 개발 및 프로젝트</p>

 <br />
 <h2>상세 기능</h2>
     
- 회원가입/로그인 : Form Login, Oauth, JWT 로그인 기능을 통해 사용자 인증을 진행하였습니다. 사용자의 로그인 상태를 유지하기 위해 쿠키와 세션을 활용하여 자동 로그인 기능을 구현하였습니다.
- 게시판 CRUD : 게시판 CRUD 기능을 개발하였습니다. 페이지 기능의 알고리즘을 직접 개발하여 사용자가 쉽게 원하는 게시물을 찾을 수 있도록 하였습니다. 스프링 부트 환경에서는 페이징 라이브러리를 활용하여 이를 구현하였습니다.
- 댓글 CRUD : 댓글 CRUD 기능을 개발하였습니다.
- 첨부파일 업로드 : Apache Commons 라이브러리를 활용해, 게시물 작성 시 사용자가 첨부파일을 업로드할 수 있는 기능을 구현하였습니다.
- 좋아요 기능 : 사용자가 게시물에 좋아요를 누를 수 있는 기능을 개발하였습니다.
- 관리자 기능 : 웹사이트의 활동 상태를 측정하기 위한 통계 기능을 제공합니다. 이를 통해 관리자는 직종별/성별 회원 수, 회원당 게시물, 댓글 수 등의 정보를 확인하며, 웹사이트의 사용 현황을 쉽게 파악할 수 있습니다.

 <br />
<h2>다양한 환경에서 구현한 게시판 프로젝트</h2>

· 같은 기능을 가진 게시판을 4가지 다른 환경에서 구축하면서, 각각의 기술과 환경이 프로젝트에 어떠한 영향을 미치는지, 그리고 이를 어떻게 적용해야 하는지에 대해 이해했습니다.
1) Window 환경에서 Spring JSP/Servlet, JDBC, Oracle, Apache Tomcat9, Apache로 게시판 구현 및 배포
2) Linux 환경에서 Spring Framework, HTML5/CSS3/Javascript, Maven, MyBatis, MariaDB, Cookie/Session, Tomcat9, Apache로 게시판 구현 및 배포
3) Window 환경에서 Spring Boot/Security/Thymeleaf HTML5/CSS3/Javascript, Gradle, MyBatis, MariaDB, Cookie/Session, OAuth2, Apache Tomcat10, Docker, Docker Compose로 게시판 구현 및 배포
4) Linux 환경에서 Spring Boot/Security/JPA, React, Gradle, Oracle, Cookie/Session, OAuth2, JWT, Apache Tomcat10, Docker, Kubernetes, Jenkins로 게시판 구현 및 배포

 <br />
<h2>프로젝트 아키텍처 및 기술 스택</h2>

     · 프론트엔드 
         · HTML, CSS, Javascript ES6
     · 백엔드 :
         · 운영체제 : OS, Linux (Ubuntu 22.04)
         · Framework : Spring Framework, Spring Boot, Spring JPA
         · 웹서버 : Apache HTTP Server
         · WAS : Apache Tomcat 10(Spring Boot 내장)
         · DBMS : ORACLE, MariaDB, MyBatis, Hibernate JPA
         · 보안, 인증 : Cookie / Session / OAuth2, JWT
      · 배포 : Docker & Kubernetes & Jenkins
