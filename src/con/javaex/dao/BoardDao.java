package con.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {
	private static BoardDao instance = new BoardDao();
	
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private PreparedStatement pstm;
	
	private final String url = "jdbc:oracle:thin:@192.168.56.101:1521:xe";
	private final String username = "webdb";
	private final String password = "webdb";
	
	private BoardDao() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static BoardDao getInstance() {
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
	
	public List<BoardVo> getList() {
		getConnection();
		
		List<BoardVo> list = new ArrayList<BoardVo>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT  b.no AS NO, ");
		sb.append("        b.title AS TITLE, ");
		sb.append("        u.name AS NAME, ");
		sb.append("        b.hit AS HIT, ");
		sb.append("        TO_CHAR(b.reg_date, 'YYYY-MM-DD') AS REG_DATE, ");
		sb.append("        b.user_no AS USER_NO ");
		sb.append("FROM board b JOIN users u ");
		sb.append("    	   ON b.user_no = u.no ");
		sb.append("ORDER BY NO DESC");
		
		String sql = sb.toString();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			BoardVo vo;
			
			while(rs.next()) {
				vo = new BoardVo(rs.getInt("NO"),
								 rs.getString("TITLE"),
								 rs.getString("NAME"),
								 rs.getInt("HIT"),
								 rs.getString("REG_DATE"),
								 rs.getInt("USER_NO"));
				list.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return list;
	}

	public BoardVo getContent(int no) {
		getConnection();
		
		BoardVo vo = null;
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT  title, ");
		sb.append("        content, ");
		sb.append("        user_no, ");
		sb.append("        no ");
		sb.append("FROM    board ");
		sb.append("WHERE   no = ? ");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, no);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				vo = new BoardVo();
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setUser_no(rs.getInt("user_no"));
				vo.setNo(rs.getInt("no"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return vo;
	}

	public int modifyContent(String modTitle, String modContent, int no) {
		getConnection();
		
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		
		sb.append("UPDATE board ");
		sb.append("SET 	  title = ?, ");
		sb.append("		  content = ? ");
		sb.append("WHERE  no = ? ");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, modTitle);
			pstm.setString(2, modContent);
			pstm.setInt(3, no);
			rs = pstm.executeQuery();
			cnt++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return cnt;
	}
	
	public int writeContent(String title, String content, int no) {
		getConnection();
		
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		
		sb.append("INSERT INTO board ");
		sb.append("VALUES ( seq_board_no.nextval, ");
		sb.append("         ?, ");
		sb.append("         ?, ");
		sb.append("         DEFAULT, ");
		sb.append("         SYSDATE, ");
		sb.append("         ? ) ");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, title);
			pstm.setString(2, content);
			pstm.setInt(3, no);
			rs = pstm.executeQuery();
			cnt++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return cnt;
	}
	
	public int deleteContent(int no) {
		getConnection();
		
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		
		sb.append("DELETE FROM board ");
		sb.append("WHERE no = ? ");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, no);
			rs = pstm.executeQuery();
			cnt++;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return cnt;
	}
	
	public int hitUp(int no) {
		getConnection();
		
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		
		sb.append("UPDATE board ");
		sb.append("SET hit = hit + 1 ");
		sb.append("WHERE no = ? ");
		
		String sql = sb.toString();
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, no);
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
