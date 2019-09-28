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
import javax.servlet.http.HttpSession;

import com.util.DBConnection;

/**
 * Servlet implementation class Register
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Register() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		if (flag) {

			Connection conn = DBConnection.getConnection();
			Statement createStatement = null;
			ResultSet executeQuery = null;

			try {
				createStatement = conn.createStatement();
				executeQuery = createStatement.executeQuery("select * from user where Username='" + UserName + "';");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (executeQuery.next()) {
					request.setAttribute("error3", "Username already used!");
					System.out.println(true);

					flag = false;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (flag) {
					System.out.println(true);
					createStatement.execute(
							"insert into user (Username,Password,Rating) values ('" + UserName + "','" + Password + "',1500);");
					HttpSession session = request.getSession();
					Statement createStatement2 = conn.createStatement();
					ResultSet executeQuery2 = createStatement2.executeQuery("select uid from user where Username='"+UserName+"' and Password='"+Password+"';");
					executeQuery2.next();
					int uid = executeQuery2.getInt("uid");
					session.setAttribute("uid", uid);
					response.sendRedirect("home.jsp");
//					conn.commit();
					conn.close();
				}else {
					response.sendRedirect("index.jsp");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("In");
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

		if (flag) {

			Connection conn = DBConnection.getConnection();
			Statement createStatement = null;
			ResultSet executeQuery = null;
			try {
				createStatement = conn.createStatement();
				executeQuery = createStatement.executeQuery("select * from user where Username='" + UserName + "';");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (executeQuery.next()) {
					request.setAttribute("error3", "Username already used!");
					flag = false;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (flag) {
					createStatement.execute(
							"insert into user (Username,Password,Rating) values ('" + UserName + "','" + Password + "',1500);");
					HttpSession session = request.getSession();
					Statement createStatement2 = conn.createStatement();
					ResultSet executeQuery2 = createStatement2.executeQuery("select uid from user where Username='"+UserName+"' and Password='"+Password+"';");
					executeQuery2.next();
					int uid = executeQuery2.getInt("uid");
					session.setAttribute("uid", uid);
					response.sendRedirect("home.jsp");
//					conn.commit();
					conn.close();
				}else {
					response.sendRedirect("index.jsp");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}


	}

}
