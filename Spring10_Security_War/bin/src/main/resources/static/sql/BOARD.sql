drop table board3 CASCADE CONSTRAINTS purge;
CREATE TABLE BOARD3(
	BOARD_NUM		 	NUMBER,  	-- 글 번호
	BOARD_NAME		 	VARCHAR2(30), 	-- 작성자
	BOARD_PASS	 		VARCHAR2(30), 	-- 비밀번호
	BOARD_SUBJECT	 	VARCHAR2(300), 	-- 제목
	BOARD_CONTENT 		VARCHAR2(4000), 	-- 내용
	BOARD_FILE	 		VARCHAR2(50), 	-- 첨부 파일 명(가공)
	BOARD_ORIGINAL 		VARCHAR2(50), 	-- 첨부 파일 명(가공)
	BOARD_RE_REF 		NUMBER, 	-- 답변 글 작성시 참고되는 글으 ㅣ번호
	BOARD_RE_LEV  		NUMBER, 	-- 답변 글의 깊이
	BOARD_RE_SEQ 		NUMBER, 	-- 답별 글의 순서
	BOARD_READCOUNT	 	NUMBER, 	-- 글의 조회수
	BOARD_DATE	 	DATE, 	-- 글의 작성 날짜
	PRIMARY KEY(BOARD_NUM)
);



-- 파일 업데이트 및 삭제 시 기존 저장된 파일 삭제 트리거
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
