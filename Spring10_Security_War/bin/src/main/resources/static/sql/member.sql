drop table member3 CASCADE CONSTRAINTS purge;
--1. index.jsp���� �����մϴ�
--2. ������ ���� admin, ��� 1234�� ����ϴ�
--3. ����� ������ 3�� ����ϴ�

CREATE TABLE member3(
	id		 		varchar2(12),  
	password	 	varchar2(60), --��ȭȭ�� ���� 60��  
	name		 	varchar2(20), 
	age		 		Number(2),  
	gender		 	varchar2(3),  
	email		 	varchar2(30),
	auth varchar2(50) not null, --ȸ���� role(����)�� �����ϴ� ������ �⺻���� 'ROLE_MEMBER' �Դϴ�.
	primary KEY(id)  
);