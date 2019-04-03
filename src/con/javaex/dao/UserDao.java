package con.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.javaex.vo.UserVo;

public class UserDao {
private static UserDao instance = new UserDao();
	
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private PreparedStatement pstm;
	
	private final String url = "jdbc:oracle:thin:@192.168.56.101:1521:xe";
	private final String username = "webdb";
	private final String password = "webdb";
	
	private UserDao() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static UserDao getInstance() {
		return instance;
	}
	
	private void getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeConnection() {
		try {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(pstm != null)
				pstm.close();
			if(conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int insert(UserVo vo) {
		getConnection();
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		
		sb.append("INSERT INTO users ");
		sb.append("VALUES ( ");
		sb.append(" 					seq_users_no.nextval, ");
		sb.append("						?, ");
		sb.append("						?, ");
		sb.append("						?, ");
		sb.append("						?) ");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, vo.getName());
			pstm.setString(2, vo.getEmail());
			pstm.setString(3, vo.getPassword());
			pstm.setString(4, vo.getGender());
			rs = pstm.executeQuery();
			cnt++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return cnt;
	}
	
	public UserVo getUser(String email, String password) {
		UserVo loginUser = null;
		
		getConnection();
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT	no, ");
		sb.append("			name ");
		sb.append("FROM 	users ");
		sb.append("WHERE	email = ? ");
		sb.append("	AND		password = ? ");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, email);
			pstm.setString(2, password);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				loginUser = new UserVo();
				loginUser.setNo(rs.getInt("no"));
				loginUser.setName(rs.getString("name"));
				loginUser.setEmail(email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return loginUser;
	}

	
	
	public int update(UserVo vo) {
		getConnection();
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		
		sb.append("UPDATE users ");
		sb.append("SET 	  name = ?, ");
		sb.append("		  password = ?, ");
		sb.append("		  gender = ? ");
		sb.append("WHERE ");
		sb.append("		  no = ? ");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, vo.getName());
			pstm.setString(2, vo.getPassword());
			pstm.setString(3, vo.getGender());
			pstm.setInt(4, vo.getNo());
			rs = pstm.executeQuery();
			cnt++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return cnt;
		
	}
	
}
