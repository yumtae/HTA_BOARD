drop table member3 CASCADE CONSTRAINTS purge;
--1. index.jsp에서 시작합니다
--2. 관리자 계정 admin, 비번 1234를 만듭니다
--3. 사용자 계정을 3개 만듭니다

CREATE TABLE member3(
	id		 		varchar2(12),  
	password	 	varchar2(60), --암화화를 위해 60자  
	name		 	varchar2(20), 
	age		 		Number(2),  
	gender		 	varchar2(3),  
	email		 	varchar2(30),
	auth varchar2(50) not null, --회원의 role(권한)을 저장하는 곳으로 기본값은 'ROLE_MEMBER' 입니다.
	primary KEY(id)  
);