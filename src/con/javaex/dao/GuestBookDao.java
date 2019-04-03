package con.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestBookVo;

public class GuestBookDao {
	private static GuestBookDao instance = new GuestBookDao();
	
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private PreparedStatement pstm;
	
	private final String url = "jdbc:oracle:thin:@192.168.56.101:1521:xe";
	private final String username = "webdb";
	private final String password = "webdb";
	
	private GuestBookDao() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static GuestBookDao getInstance() {
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
	
	public List<GuestBookVo> getList(){
		getConnection();
		
		List<GuestBookVo> list = new ArrayList<GuestBookVo>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT 	NO, ");
		sb.append("			NAME, ");
		sb.append("			PASSWORD, ");
		sb.append("			CONTENT, ");
		sb.append("			TO_CHAR(REG_DATE, 'YYYY-MM-DD HH24:MI:SS') as REG_DATE ");
		sb.append("FROM		GUESTBOOK ");
		sb.append("ORDER BY	NO DESC");
		
		String sql = sb.toString();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			GuestBookVo gstInfo;
			
			while(rs.next()) {
				gstInfo = new GuestBookVo(rs.getInt("NO"), 
										  rs.getString("NAME"),
										  rs.getString("PASSWORD"),
										  rs.getString("CONTENT"),
										  rs.getString("REG_DATE"));
				list.add(gstInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return list;
	}
	
	public int insert(GuestBookVo vo) {
		getConnection();
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		
		sb.append("INSERT INTO GUESTBOOK( ");
		sb.append("						NO, ");
		sb.append("						NAME, ");
		sb.append("						PASSWORD, ");
		sb.append("						CONTENT, ");
		sb.append("						REG_DATE) ");
		sb.append("VALUES ( ");
		sb.append(" 					seq_guestbook_no.nextval, ");
		sb.append("						?, ");
		sb.append("						?, ");
		sb.append("						?, ");
		sb.append("						SYSDATE)");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, vo.getName());
			pstm.setString(2, vo.getPassword());
			pstm.setString(3, vo.getContent());
			rs = pstm.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		cnt++;
		return cnt;
	}

	public int delete(GuestBookVo vo) {
		getConnection();
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		
		sb.append("DELETE FROM GUESTBOOK ");
		sb.append("WHERE NO = ? ");
		sb.append("AND PASSWORD = ? ");
		
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, vo.getNo());
			pstm.setString(2, vo.getPassword());
			rs = pstm.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		cnt++;
		return cnt;
	}
}
