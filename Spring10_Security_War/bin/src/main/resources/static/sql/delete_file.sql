drop table delete_file purge;

create table delete_file(
    board_file varchar2(50),
    reg_date date default sysdate
);
