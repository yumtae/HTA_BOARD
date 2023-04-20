drop table board3 CASCADE CONSTRAINTS purge;
CREATE TABLE BOARD3(
	BOARD_NUM		 	NUMBER,  	-- �� ��ȣ
	BOARD_NAME		 	VARCHAR2(30), 	-- �ۼ���
	BOARD_PASS	 		VARCHAR2(30), 	-- ��й�ȣ
	BOARD_SUBJECT	 	VARCHAR2(300), 	-- ����
	BOARD_CONTENT 		VARCHAR2(4000), 	-- ����
	BOARD_FILE	 		VARCHAR2(50), 	-- ÷�� ���� ��(����)
	BOARD_ORIGINAL 		VARCHAR2(50), 	-- ÷�� ���� ��(����)
	BOARD_RE_REF 		NUMBER, 	-- �亯 �� �ۼ��� ����Ǵ� ���� �ӹ�ȣ
	BOARD_RE_LEV  		NUMBER, 	-- �亯 ���� ����
	BOARD_RE_SEQ 		NUMBER, 	-- �亰 ���� ����
	BOARD_READCOUNT	 	NUMBER, 	-- ���� ��ȸ��
	BOARD_DATE	 	DATE, 	-- ���� �ۼ� ��¥
	PRIMARY KEY(BOARD_NUM)
);



-- ���� ������Ʈ �� ���� �� ���� ����� ���� ���� Ʈ����
create or replace trigger save_delete_file
after update or delete
on board3
for each row
begin
   if(:old.board_file is not null) then
      if(:old.board_file != :new.board_file or :new.board_file is null) then
         insert into delete_file
         (board_file)
         values(:old.board_file);
      end if;
   end if;
end;
/
