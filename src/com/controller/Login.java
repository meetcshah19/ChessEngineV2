package com.controller;

import java.io.IOException;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.DBConnection;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = DBConnection.getConnection();
		boolean flag = true;
		String UserName = request.getParameter("username");
		String Password = request.getParameter("password");
		if (UserName == null || UserName.trim().length() == 0) {
			request.setAttribute("error1", "Please Enter Username!");
			flag = false;
		}

		if (Password == null || Password.length() < 8) {
			request.setAttribute("error2", "Password cannot be less than 8 characters!");
			flag = false;
		}
		try {
			Statement createStatement = conn.createStatement();
			ResultSet executeQuery = createStatement.executeQuery(
					"select uid from user where Username='" + UserName + "' and Password='" + Password + "';");
			executeQuery.next();
			Integer uid = null;
			if (flag && executeQuery.isLast()) {
				uid = executeQuery.getInt("uid");
			}
			if (uid == null) {
				if (request.getAttribute("error3") == null)
					request.setAttribute("error3",
							"Wrong Username/Password");
				request.getRequestDispatcher("index.jsp").forward(request, response);
			} else {
				ResultSet a=createStatement.executeQuery("select pgn from game where uid="+uid+";");
				a.setFetchDirection(ResultSet.FETCH_REVERSE);
				;				
				
				if(a.next()) {
				request.getSession().setAttribute("pgn", a.getString("pgn"));
				}
				request.getSession().setAttribute("uid", uid);
				request.getSession().setAttribute("username", UserName);
				
				request.getSession().setAttribute("conn", conn);
				
				request.getSession().setAttribute("Loggedin", true);
				response.sendRedirect("home.jsp");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
