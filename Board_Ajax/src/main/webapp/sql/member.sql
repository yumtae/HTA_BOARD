drop table member caascade constraints purge;

--1. index.jsp에서 시작
--2. 관리자 계정 admin, 1234만듭니다
--3. 사용자 계정 3개 생성

create table member(
	id	 		varchar2(12),
	password	varchar2(10),
	name	 	varchar2(15),
	age			number(2),
	gender	 	varchar2(3),
	email		varchar2(30),
	memberfile	varchar2(50),
	primary key(id)
);

--memberfile은 회원 정보 수정시 적용
