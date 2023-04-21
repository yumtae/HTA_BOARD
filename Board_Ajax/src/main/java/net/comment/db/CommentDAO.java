package net.comment.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CommentDAO {

	
private DataSource ds;
	
	public CommentDAO() {
		try {
			Context init =new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		}catch(Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
			return;
		}
		
		
	}

	
	
	
	public int getListCount(int comment_board_num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;

		int x = 0; 
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*) from comm where COMMENT_BOARD_NUM=? ";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, comment_board_num);
			rs= pstmt.executeQuery();
			
				
			if (rs.next()) {
				x = rs.getInt(1);
			}
			
			
		}catch(Exception se){
			System.out.println("getListCount에러: " +se);
			
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




	public JsonArray getCommentList(int comment_board_num, int state) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;

		String sort="asc";
		if (state ==2) {
			sort="desc";
		}
		
		JsonArray array = new JsonArray();
		try {
			
			conn = ds.getConnection();
			
			String sql = "select num,comm.id,content,reg_date,comment_re_lev,"
							+ "		comment_re_seq,comment_re_ref, member.memberfile from"
							+ "	comm  inner join  member "
							+ "on comm.id = member.id "
							+ "where comment_board_num = ?"
							+ "order  by  comment_re_ref "+ sort + ", "
							+ "			comment_re_seq asc ";
			
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, comment_board_num);
			rs= pstmt.executeQuery();
			
				
			while (rs.next()) {
				JsonObject object= new JsonObject();
				object.addProperty("num", rs.getInt(1));
				object.addProperty("id", rs.getString(2));
				object.addProperty("content", rs.getString(3));
				object.addProperty("reg_date", rs.getString(4));
				object.addProperty("comment_re_lev", rs.getInt(5));
				object.addProperty("comment_re_seq", rs.getInt(6));
				object.addProperty("comment_re_ref", rs.getInt(7));
				object.addProperty("memberfile", rs.getString(8));
				array.add(object);
			}
			
			
		}catch(Exception se){
			System.out.println("getCommentList에러: " +se);
			
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

		return array;
	}




	public int commentsInsert(Comment c) {
		Connection conn = null;
		PreparedStatement pstmt = null;
	
		int result = 0; 
		try {
			
			conn = ds.getConnection();
			
			String sql = "insert into comm"
					+ 		" values (com_seq.nextval, ?, ? , sysdate, ?,?,?,com_seq.nextval)";
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, c.getId());
			pstmt.setString(2, c.getContent());
			pstmt.setInt(3, c.getComment_board_num());
			pstmt.setInt(4, c.getComment_re_lev());
			pstmt.setInt(5, c.getComment_re_seq());

				
			result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("데이터 모두삽입완료");
			}
			
			
		}catch(Exception se){
			System.out.println("commentsInsert 에러:" + se);
			
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
		return result;
	}




	public int commentsUpdate(Comment co) {
		Connection conn = null;
		PreparedStatement pstmt = null;
	
		int result = 0; 
		try {
			
			conn = ds.getConnection();
			
			String sql = "update comm set"
					+ 		" content = ?  where num = ?";
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, co.getContent());
			pstmt.setInt(2, co.getNum());


				
			result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("수정 완료");
			}
			
			
		}catch(Exception se){
			System.out.println("commentsUpdate 에러:" + se);
			
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
		return result;
	}




	public int commentsDelete(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
	
		int result = 0; 
		try {
			
			conn = ds.getConnection();
			
			String sql = "delete from comm "
					+ 		"  where num = ?";
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setInt(1, num);

			result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("삭제 완료");
			}
			
			
		}catch(Exception se){
			System.out.println("commentsDelete 에러:" + se);
			
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
		return result;
	}




	public int commentsReply(Comment c) {
		Connection conn = null;
		PreparedStatement pstmt = null;
	
		int result = 0; 
		try {
			
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			StringBuilder update_sql = new StringBuilder();
			
			update_sql.append("update comm ");
			update_sql.append("set comment_re_ref = comment_re_seq +1 ");
			update_sql.append("where comment_re_ref= ? ");
			update_sql.append("and comment_re_seq > ?");
			
			pstmt = conn.prepareStatement(update_sql.toString());
			pstmt.setInt(1, c.getComment_re_ref());
			pstmt.setInt(2, c.getComment_re_seq());

			pstmt.executeUpdate();
			pstmt.close();
			
			String sql = "insert into comm "
					+ " values ( com_seq.nextval, ? , ?,  sysdate , ? ,? ,? ,? )";
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, c.getId());
			pstmt.setString(2, c.getContent());
			pstmt.setInt(3, c.getComment_board_num());
			pstmt.setInt(4, c.getComment_re_lev()+1);
			pstmt.setInt(5, c.getComment_re_seq()+1);
			pstmt.setInt(6, c.getComment_re_ref());
			result = pstmt.executeUpdate();
			
			if(result ==1) {
				System.out.println("reply 삽입 완료");
				conn.commit();
			}
			
			
		}catch(Exception se){
			System.out.println("commentsReply 에러:" + se);
			try {
				conn.rollback();
			}catch(SQLException e1) {
				e1.printStackTrace();
			}
			
			
		}finally {
			
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
		return result;
	}
	
	
	
	
	
	
	
	
	
	
}
