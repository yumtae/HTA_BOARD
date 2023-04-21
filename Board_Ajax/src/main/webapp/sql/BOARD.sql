drop table board cascade constraints purge;

create table board(
	BOARD_NUM			NUMBER,
	BOARD_NAME			VARCHAR2(30),
	BOARD_PASS			VARCHAR2(30),
	BOARD_SUBJECT		VARCHAR2(300),
	BOARD_CONTENT		VARCHAR2(4000),
	BOARD_FILE			VARCHAR2(50),
	BOARD_RE_REF			NUMBER,
	BOARD_RE_LEV			NUMBER,
	BOARD_RE_SEQ			NUMBER,
	BOARD_READCOUNT			NUMBER,
	BOARD_DATE DATE default	sysdate,
	PRIMARY KEY(BOARD_NUM)
);




select * from board;
delete from board;


insert into board ( board_num , board_subject , board_name, BOARD_re_ref) values(1,'처음','admin',1);
insert into board ( board_num , board_subject , board_name, BOARD_re_ref) values(2,'둘째','admin',2);
insert into board ( board_num , board_subject , board_name, BOARD_re_ref) values(3,'셋째','admin',3);

insert into comm ( num, id , comment_board_num) values (1, 'admin',1); 
insert into comm ( num, id , comment_board_num) values (2, 'admin',1); 
insert into comm ( num, id , comment_board_num) values (3, 'admin',2); 
insert into comm ( num, id , comment_board_num) values (4, 'admin',2); 



update board
set board_subject = '오늘도 행복하세요'
where board_num =1


--1. comm 테이블에서 COMMENT_BOARD_NUM별 갯수를 구합니다

COMMENT_BOARD_NUM 					cnt
----------------- ------------------------
                1                        2
                2                        2

             
select COMMENT_BOARD_NUM,count(*) cnt from comm group by COMMENT_BOARD_NUM;



-- 2. board와 조인을 합니다

 BOARD_NUM BOARD_SUBJECT                    CNT
---------- ------------------------- ----------
         1 오늘도 행복하세요                     2
         2 둘째                               2
         3 셋째

select board_num,board_subject,NVL(cnt , 0) from board 
left outer join
(select COMMENT_BOARD_NUM,count(*) cnt from comm group by COMMENT_BOARD_NUM)
on board_num = COMMENT_BOARD_NUM order by board_num desc


-- 2-2 순서정렬 & null 값 제거 (최종)
 BOARD_NUM BOARD_SUBJECT             NVL(CNT,0)
---------- ------------------------- ----------
         3 셋째                               0
         2 둘째                               2
         1 오늘도 행복하세요                     2


select board_num,board_subject,NVL(cnt , 0) from board 
left outer join
(select COMMENT_BOARD_NUM,count(*) cnt from comm group by COMMENT_BOARD_NUM)
on board_num = COMMENT_BOARD_NUM 
order by board_re_ref desc, 
board_re_seq asc;










