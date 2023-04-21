package net.member.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class MemberDAO {
	
	private DataSource ds;
	
	public MemberDAO() {
		try {
			Context init =new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		}catch(Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
		}
		
	}
	
	
	public int isId(String id, String pass) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;

		int result = -1; //  해당 id가 없습니다
		try {
			
			conn = ds.getConnection();
			
			String sql = "select * from member where id = ?";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs= pstmt.executeQuery();
			
				
			if (rs.next()) {
				if(rs.getString(2).equals(pass)) {
					result = 1 ; //아이디 비번 일치
				}else {
					result = 0 ; // 아이디 비번이 일치하지 않음
				}

				
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

		return result;
	}
	
	
	

	public int isId(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;

		int result = -1; // DB에 해당 id가 없습니다
		try {
			
			conn = ds.getConnection();
			
			String sql = "select * from member where id = ?";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs= pstmt.executeQuery();
			
				
			while (rs.next()) {
				result = 0 ; // DB에 해당 id가 있습니다
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

		return result;
	}

	public int insert(Member m) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		int result = 0;

		try {
	
			conn = ds.getConnection();
			System.out.println("getCinnection : insert() ;");
			
			String sql = "insert into member (id,password,name,age,gender,email) values ( ? , ?, ?, ? ,? ,?) ";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, m.getId());
			pstmt.setString(2, m.getPassword());
			pstmt.setString(3, m.getName());
			pstmt.setInt(4, m.getAge());
			pstmt.setString(5, m.getGender());
			pstmt.setString(6, m.getEmail());
	
			result = pstmt.executeUpdate();
			
			
		}catch(java.sql.SQLIntegrityConstraintViolationException e) {
			result = -1;
			System.out.println("아이디가 중복 에러입니다");
		
		}catch(Exception se){
			System.out.println(se.getMessage());
			
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


	public Member member_info(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		Member m = null;
		//select만 필요
		ResultSet rs = null;


		try {
			
			conn = ds.getConnection();
			
			String sql = "select * from member where id = ?";
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs= pstmt.executeQuery();
			
				
			if (rs.next()) {
				m = new Member();
				m.setId(rs.getString(1));
				m.setPassword(rs.getString(2));
				m.setName(rs.getString(3));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));
				m.setMemberfile(rs.getString(7));
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

		return m;
	}


	public int update(Member m) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		int result = 0;

		try {
	
			conn = ds.getConnection();
			String sql = "update member set name=?, age=? , gender=? ,email=?, Memberfile=? where id=? ";
			pstmt = conn.prepareStatement(sql.toString());

	
			pstmt.setString(1, m.getName());
			pstmt.setInt(2, m.getAge());
			pstmt.setString(3, m.getGender());
			pstmt.setString(4, m.getEmail());
			System.out.println( m.getMemberfile());
			pstmt.setString(5, m.getMemberfile());
			pstmt.setString(6, m.getId());
	
			result = pstmt.executeUpdate();
			

		}catch(Exception se){
			System.out.println(se.getMessage());
			
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

	public int getListCount(String field, String value) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;

		int x = 0; 
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*) from member where id != 'admin' and " 
			+ field + " like  ? ";
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, "%" + value + "%");
			rs= pstmt.executeQuery();
			
			if (rs.next()) {
				x = rs.getInt(1);
			}
			
			
		}catch(Exception se){
			se.printStackTrace();
			System.out.println("getListcount() 에러" + se);
			
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
	

	public int getListCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;

		int x = 0; 
		try {
			
			conn = ds.getConnection();
			
			String sql = "select count(*) from member where id != 'admin'";
			pstmt = conn.prepareStatement(sql.toString());
			rs= pstmt.executeQuery();
			
				
			if (rs.next()) {
				x = rs.getInt(1);
			}
			
			
		}catch(Exception se){
			se.printStackTrace();
			System.out.println("getListcount() 에러" + se);
			
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



	public List<Member> getList(String field, String value, int page, int limit) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;
		List<Member> list = new ArrayList<Member>();
		
		String sql = "select *  "
				+ "from (select b.*, rownum rnum "
				+ "		from ( select * from member "
				+ "			where id != 'admin' "
				+ "			and " + field + " like ? "		
				+ "			order by id) b "
				+ "		where rownum <= ?   "
				+ "		) "
				+ "   where rnum between  ? and  ?";
		
		//System.out.println( sql);
			
		try {
			int startrow = (page -1) * limit + 1; 
			int endrow = startrow + limit -1;		
			
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, "%" + value + "%");
			pstmt.setInt(2, endrow);
			pstmt.setInt(3, startrow);
			pstmt.setInt(4, endrow);
			rs= pstmt.executeQuery();
			
				
			while (rs.next()) {
				Member m = new Member();
				m.setId(rs.getString("id"));
				m.setPassword(rs.getString(2));
				m.setName(rs.getString("name"));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));
				list.add(m);
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
	
	public List<Member> getList(int page, int limit) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//select만 필요
		ResultSet rs = null;
		List<Member> list = new ArrayList<Member>();
		
		String sql = "select *  "
				+ "from (select b.*, rownum rnum "
				+ "		from ( select * from member "
				+ "			where id != 'admin' "
				+ "			order by id) b "
				+ "		where rownum <= ?   "
				+ "		) "
				+ "   where rnum >= ? and rnum<= ?";

			
		try {
			int startrow = (page -1) * limit + 1; 
			int endrow = startrow + limit -1;		
			
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setInt(1, endrow);
			pstmt.setInt(2, startrow);
			pstmt.setInt(3, endrow);
			rs= pstmt.executeQuery();
			
				
			while (rs.next()) {
				Member m = new Member();
				m.setId(rs.getString("id"));
				m.setPassword(rs.getString(2));
				m.setName(rs.getString("name"));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));
				list.add(m);
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


	public int delete(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		int result = 0;

		try {
	
			conn = ds.getConnection();
			String sql = "delete from member  where id=? ";
			pstmt = conn.prepareStatement(sql.toString());

	
			pstmt.setString(1, id);
			result = pstmt.executeUpdate();
			

		}catch(Exception se){
			System.out.println(se.getMessage());
			
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




	



	
	
	
	
	
}
