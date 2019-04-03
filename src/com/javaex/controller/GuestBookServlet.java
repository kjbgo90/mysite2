package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.util.WebUtil;
import com.javaex.vo.GuestBookVo;

import con.javaex.dao.GuestBookDao;

@WebServlet("/guestbook")
public class GuestBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("guestbook 컨트롤러");
		
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		if("addlst".equals(action)) {
			System.out.println("addlst 호출");
			List<GuestBookVo> list = GuestBookDao.getInstance().getList();
			
			request.setAttribute("guestList", list);
			WebUtil.forword(request, response, "/WEB-INF/views/guestbook/addlst.jsp");
		}
		
		else if("insert".equals(action)) {
			System.out.println("insert 실행!");
		
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");
			GuestBookVo vo = new GuestBookVo(name, password, content);
			
			GuestBookDao dao = GuestBookDao.getInstance();
			dao.insert(vo);
			
			WebUtil.redirect(request, response, "/guestbook?action=addlst");
		}else if("deleteform".equals(action)) {
			System.out.println("deleteForm 호출!");
			
			String no = request.getParameter("no");
			
			WebUtil.forword(request, response, "/WEB-INF/views/guestbook/deleteform.jsp?no="+no);
		} else if("delete".equals(action)) {
			System.out.println("delete 실행!");
			
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("password");
			
			GuestBookVo vo = new GuestBookVo(no, password);
			
			GuestBookDao.getInstance().delete(vo);
			
			WebUtil.redirect(request, response, "/guestbook?action=addlst");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
