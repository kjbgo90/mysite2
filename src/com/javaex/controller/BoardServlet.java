package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

import con.javaex.dao.BoardDao;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("board 컨트롤러");
		
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		if("list".equals(action)) {
			System.out.println("list 호출");
			
			List<BoardVo> list = BoardDao.getInstance().getList();
			request.setAttribute("boardList", list);
			
			WebUtil.forword(request, response, "/WEB-INF/views/board/list.jsp");
		}
		else if("read".equals(action)) {
			System.out.println("read 호출");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			request.setAttribute("boardContent", BoardDao.getInstance().getContent(no));
			BoardDao.getInstance().hitUp(no);
			WebUtil.forword(request, response, "/WEB-INF/views/board/read.jsp");
		}
		else if("modifyform".equals(action)) {
			System.out.println("modifyform 호출");
			int no = Integer.parseInt(request.getParameter("no"));
			
			request.setAttribute("modifyContent", BoardDao.getInstance().getContent(no));
			WebUtil.forword(request, response, "/WEB-INF/views/board/modifyform.jsp");
		}
		else if("modify".equals(action)) {
			System.out.println("modify 실행");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardDao.getInstance().modifyContent(title, content, no);
			
			WebUtil.redirect(request, response, "/board?action=read&no=" + no);
		}
		else if("writeform".equals(action)) {
			System.out.println("writeform 호출");
			WebUtil.forword(request, response, "/WEB-INF/views/board/writeform.jsp");
		}
		else if("write".equals(action)) {
			System.out.println("write 실행");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			HttpSession session = request.getSession();
			UserVo vo = (UserVo)session.getAttribute("authUser");
			
			int cnt = BoardDao.getInstance().writeContent(title, content, vo.getNo());
			if(cnt == 1)
				System.out.println("write 완료");
			WebUtil.redirect(request, response, "/board?action=list");
		}
		else if("delete".equals(action)) {
			System.out.println("delete 실행");
			
			int no = Integer.parseInt(request.getParameter("no"));
			int cnt = BoardDao.getInstance().deleteContent(no);
			if(cnt == 1)
				System.out.println("delete 완료");
			WebUtil.redirect(request, response, "/board?action=list");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
