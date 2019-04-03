package com.javaex.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

import con.javaex.dao.UserDao;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("user 컨트롤러");
		
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		if("joinform".equals(action)) {
			System.out.println("joinform 호출");
			WebUtil.forword(request, response, "/WEB-INF/views/user/joinform.jsp");
		}
		else if("join".equals(action)) {
			System.out.println("join 호출");
			
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo(name, email, password, gender);
			System.out.println(userVo.toString());
			
			UserDao userDao = UserDao.getInstance();
			userDao.insert(userVo);
			
			WebUtil.forword(request, response, "/WEB-INF/views/user/joinsuccess.jsp");
//			if(cnt == 1) {
//				cnt = 0;
//				WebUtil.forword(request, response, "/WEB-INF/views/user/joinsuccess.jsp");
//			}
//			else
//				WebUtil.forword(request, response, "/WEB-INF/views/main/index.jsp");
		}
		else if("loginform".equals(action)) {
			System.out.println("loginform 호출");
			
			WebUtil.forword(request, response, "/WEB-INF/views/user/loginform.jsp");
		}
		else if("login".equals(action)) {
			System.out.println("login 호출");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserVo userVo = UserDao.getInstance().getUser(email, password);
			
//			System.out.println(userVo.toString());
			
			if(userVo == null) {
				WebUtil.redirect(request, response, "/user?action=loginform&result=fail");
				
			} else {//성공시
				HttpSession session = request.getSession(true);
				session.setAttribute("authUser", userVo);
				
				WebUtil.redirect(request, response, "/main");
			}
		}
		else if("logout".equals(action)) {
			System.out.println("logout 실행");
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/main");
		}
		else if("modifyform".equals(action)) {
			System.out.println("modifyform 호출");
			
			WebUtil.forword(request, response, "/WEB-INF/views/user/modifyform.jsp");
		}else if("modify".equals(action)) {
			System.out.println("modify 실행");
			
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			//세션에서 수정할 UserVo를 추출하고 modifyform에서 가져온 정보를 넣어준다.
			HttpSession session = request.getSession(false);
			UserVo authVo = (UserVo)session.getAttribute("authUser");
			authVo.setName(name);
			authVo.setPassword(password);
			authVo.setGender(gender);
			
			int cnt = UserDao.getInstance().update(authVo);
			if (cnt == 1) {
			//no와 name를 가지고 있는 새로운 객체를 생성하고 authUser이름으로 넣어준다.
				System.out.println("수정 성공!");
				session.setAttribute("authUser", new UserVo(authVo.getNo(), name, authVo.getEmail()));
			}
			WebUtil.redirect(request, response, "/main");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
