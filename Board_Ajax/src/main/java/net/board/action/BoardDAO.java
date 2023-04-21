package net.board.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.board.db.BoardBean;

public class BoardDAO {
	
	private DataSource ds;
	
	public BoardDAO() {
		try {
			Context init =new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		}catch(Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
			return;
		}
		
		
	}
	
	
	
	public int getListCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;

		int x = 0; 
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*) from board";
			pstmt = conn.prepareStatement(sql.toString());
			rs= pstmt.executeQuery();
			
				
			if (rs.next()) {
				x = rs.getInt(1);
			}
			
			
		}catch(Exception se){
			System.out.println(se.getMessage());
			
		}finally {
			try {
				if(rs != null)
					rs.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}

		return x;
	}



	public List<BoardBean> getBoardList(int page, int limit) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;
			
			String sql = "select * "
                    + " from (select rownum rnum, j.* "
                    + "        from (select board.*, nvl(cnt, 0) as cnt "
                    + "            from board left outer join (select comment_board_num, count(*) as cnt "
                    + "                                  from comm "
                    + "                                  group by comment_board_num) "
                    + "            on board_num = comment_board_num "
                    + "            order by board_re_ref desc, "
                    + "            board_re_seq asc) j "
                    + "       where rownum <= ? "
                    + "      )"
                    + " where rnum >= ? and rownum <= ?";


			List<BoardBean> list = new ArrayList<BoardBean>();
			//한 페이지당 10개씩 목록인 경우 
			int startrow = (page -1) * limit + 1;  //읽기 시작할 row 번호 (1 11 21 31
			int endrow = startrow + limit -1;		// 읽을 마지막 row번호 (10 20 30 40
			
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, endrow);
			pstmt.setInt(2, startrow);
			pstmt.setInt(3, endrow);
			rs= pstmt.executeQuery();
			
				
			while (rs.next()) {
				BoardBean board = new BoardBean();
				board.setBoard_num(rs.getInt("BOARD_NUM"));
				board.setBoard_name(rs.getString("BOARD_NAME"));
				board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
				board.setBoard_content(rs.getString("BOARD_CONTENT"));
				board.setBoard_file(rs.getString("BOARD_FILE"));
				board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
				board.setBoard_re_lev(rs.getInt("BOARD_RE_lev"));
				board.setBoard_re_seq(rs.getInt("BOARD_RE_seq"));
				board.setBoard_readcount(rs.getInt("BOARD_READCOUNT"));
				board.setBoard_date(rs.getString("BOARD_DATE"));
				board.setCnt(rs.getInt("cnt"));
				list.add(board);
			}
			
			
		}catch(Exception se){
			System.out.println(se.getMessage());
			
		}finally {
			try {
				if(rs != null)
					rs.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}

		return list;
	
	
	}



	public boolean boardInsert(BoardBean board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
	
		int result = 0; 
		try {
			
			conn = ds.getConnection();
			
			String max_sql ="(select nvl(max(board_num),0)+1 from board)";
			
			String sql = "insert into board"
						+ "(BOARD_NUM , BOARD_NAME, BOARD_PASS ,BOARD_SUBJECT,"
						+ "BOARD_CONTENT, BOARD_FILE, BOARD_RE_REF, "
						+ "BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_READCOUNT)"
						+ " values (" + max_sql + ",?,?,?,"
						+ "			?,?," + max_sql + ","
						+ "			?,?,?)";
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_pass());
			pstmt.setString(3, board.getBoard_subject());
			pstmt.setString(4, board.getBoard_content());
			pstmt.setString(5, board.getBoard_file());
			pstmt.setInt(6, 0);
			pstmt.setInt(7, 0);
			pstmt.setInt(8, 0);
				
			result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("데이터 모두삽입완료");
				return true;
			}
			
			
		}catch(Exception se){
			System.out.println("boardInsert 에러:" + se);
			
		}finally {
		
			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		return false;

	
	}



	public BoardBean getDetail(int num) {
		BoardBean board= null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "select * from board where board_num = ?";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, num);
			rs= pstmt.executeQuery();
			
				
			if (rs.next()) {
				board = new BoardBean();
				board.setBoard_num(rs.getInt("BOARD_NUM"));
				board.setBoard_name(rs.getString("BOARD_NAME"));
				board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
				board.setBoard_content(rs.getString("BOARD_CONTENT"));
				board.setBoard_file(rs.getString("BOARD_FILE"));
				board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
				board.setBoard_re_lev(rs.getInt("BOARD_RE_lev"));
				board.setBoard_re_seq(rs.getInt("BOARD_RE_seq"));
				board.setBoard_readcount(rs.getInt("BOARD_READCOUNT"));
				board.setBoard_date(rs.getString("BOARD_DATE"));
				//board.setCnt(rs.getInt("cnt"));
			
			}
			
			
		}catch(Exception se){
			System.out.println("getDetail 에러:" + se);
			
		}finally {
			try {
				if(rs != null)
					rs.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}

		return board;
	}



	public void setReadCountUpdate(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
	
		String sql = "update board"
				+ "		set BOARD_READCOUNT = BOARD_READCOUNT +1"
				+ "		where BOARD_NUM = ? ";
		
		try {
			
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1,num );
			pstmt.executeUpdate();
			
		}catch(Exception se){
			System.out.println("setReadCountUpdate 에러:" + se);
			
		}finally {
		
			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}



	public boolean isBoardWriter(int num, String pass) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;

		boolean result = false; 
		try {
			
			conn = ds.getConnection();
			
			String sql = "select BOARD_PASS from board where board_num = ? ";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, num);
			rs= pstmt.executeQuery();
			
				
			if (rs.next()) {
				if(pass.equals(rs.getString("BOARD_PASS"))){
					result = true;
				}
			}
			
		}catch(Exception se){
			System.out.println("isBoardWriter() 에러 : " + se);
			
		}finally {
			try {
				if(rs != null)
					rs.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}

		return result;
	}



	public boolean boardModify(BoardBean modifyboard) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			conn = ds.getConnection();
			
			String sql = "update board set board_subject =? , board_content=? , board_file=? where board_num=?";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, modifyboard.getBoard_subject());
			pstmt.setString(2, modifyboard.getBoard_content());
			pstmt.setString(3, modifyboard.getBoard_file());
			pstmt.setInt(4, modifyboard.getBoard_num());
			int result = pstmt.executeUpdate();
			if(result==1) {
				System.out.println("수정 업데이트");
				return true;
			}
				
			
		
		}catch(Exception se){
			System.out.println("boardModify() 에러 : " + se);
			
		}finally {

			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}

		return false;
	}



	public int boardReply(BoardBean board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;
		int num=0;
		
		//board 테이블의 글번호를 구하기 위해 board_num 컬럼의 최대값+1을 구해옵니다
		String board_max_sql ="select max(board_num)+1 from board";
	
		/*
		 * 답변을 달 원물 글 그룹 번호입니다.
		 * 답변을 달게 되면 답변 글은 이 번호와 같은 관련글 번호를 갖게 처리되면서 같은 그룹에 속하게됩니다.
		 * 글 목록에서 보여줄때 하나의 그룹으로 묶여서 출력됩니다
		 * */
		int re_ref = board.getBoard_re_ref();
		
		/*
		 *  답글의 깊이를 의미합니다
		 *  원문에 대한 답글이 출력될 때 한 번 들여쓰기 처리가 되고 갑글에 대한 답글은 들여쓰기가 두 번 처리되게 합니다.
		 *  원문인 경우에는 이 이값이 0 이고 원문의 답글은1, 답글의 답글은 2가 도비니다.
		 * */
		int re_lev = board.getBoard_re_lev();
		
		//같은 관련 글 중에서 해당 글이 출력되는 순서입니다.
		int re_seq = board.getBoard_re_seq();
		
		try {
			
			conn = ds.getConnection();
			
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(board_max_sql.toString());
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				num = rs.getInt(1);
			}
				pstmt.close();
			
			
			//달린 답글들의 RE_SEQ 값을 1씩 증가
			//현재 글을 이미 달린 답글보다 앞에 출력되게 하기 위함
			String sql = "update board "
					+ 	"set BOARD_RE_SEQ = BOARD_RE_SEQ + 1 "
					+ 	"where BOARD_RE_REF = ? "
					+	"and BOARD_RE_SEQ > ?";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();
			pstmt.close();
		
			//등록할 답변 글의 RE_LEV , RE_SEQ 값을 원문 글보다 1씩 증가
			re_seq=re_seq+1;
			re_lev=re_lev+1;
		
				
			sql = "insert into board "
					+ "(BOARD_NUM,BOARD_NAME,BOARD_PASS,BOARD_SUBJECT,"
					+ "BOARD_CONTENT,BOARD_FILE,BOARD_RE_REF,"
					+ "BOARD_RE_LEV,BOARD_RE_SEQ,BOARD_READCOUNT) "
					+ " values (" +num +","
							+ "?,?,?,"
							+ "?,?,?,"
							+ "?,?,?)";
			
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_pass());
			pstmt.setString(3, board.getBoard_subject());
			pstmt.setString(4, board.getBoard_content());
			pstmt.setString(5, "");
			pstmt.setInt(6, re_ref);
			pstmt.setInt(7, re_lev);
			pstmt.setInt(8, re_seq);
			pstmt.setInt(9, 0);
			
			if(pstmt.executeUpdate() == 1) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
				
		}catch(SQLException se){
			se.printStackTrace();
			System.out.println("boardReply() 에러 : " + se);
			
		}finally {
			try {
				if(rs != null)
					rs.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.setAutoCommit(true);
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}

		return num;
	}



	public boolean boardDelete(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null , pstmt2=null;
		ResultSet rs = null;
		
		String select_sql = "select BOARD_RE_REF,BOARD_RE_LEV,BOARD_RE_SEQ from board where board_num=?";
		
		String board_delete_sql = "delete from board "
				+ "		 where  BOARD_RE_REF = ? "
				+ "	 	 and    BOARD_RE_LEV >=? "
				+ "		 and    BOARD_RE_SEQ >=? "
				+ "		 and    BOARD_RE_SEQ <=( "
				+ "	                    nvl((SELECT min(board_re_seq )-1 "
				+ "	                           FROM   BOARD  "
				+ "	                           WHERE  BOARD_RE_REF=? "
				+ "	                           AND    BOARD_RE_LEV=? "
				+ "	                           AND    BOARD_RE_SEQ>?) , "
				+ "	                           (SELECT max(board_re_seq) "
				+ "	                            FROM   BOARD  "
				+ "	                            WHERE  BOARD_RE_REF=? ))"
				+ "		                   )";
		
		System.out.println(board_delete_sql);
		
		boolean result_check = false;
		
		try {
			
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(select_sql);
			pstmt.setInt(1,num );
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pstmt2 = conn.prepareStatement(board_delete_sql);
				pstmt2.setInt(1, rs.getInt("BOARD_RE_REF"));
				pstmt2.setInt(2, rs.getInt("BOARD_RE_LEV"));
				pstmt2.setInt(3, rs.getInt("BOARD_RE_SEQ"));
				pstmt2.setInt(4, rs.getInt("BOARD_RE_REF"));
				pstmt2.setInt(5, rs.getInt("BOARD_RE_LEV"));
				pstmt2.setInt(6, rs.getInt("BOARD_RE_SEQ"));
				pstmt2.setInt(7, rs.getInt("BOARD_RE_REF"));
				
				int count = pstmt2.executeUpdate();
				
				if(count >= 1)
					result_check = true;
			}

		}catch(Exception se){
			System.out.println("boardDelete() 에러:" + se);
			
		}finally {
			try {
				if(rs != null)
					rs.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(pstmt != null)
					pstmt.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(pstmt2 != null)
					pstmt2.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return result_check;
	}
	
	
}
