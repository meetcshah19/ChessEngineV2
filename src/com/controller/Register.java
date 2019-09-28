package com.controller;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean flag = true;
		String UserName = request.getParameter("username");
		String Password = request.getParameter("password");

		if (UserName == null || UserName.trim().length() == 0) {
			request.setAttribute("error1", "Please Enter Username!");
			flag = false;
		}

		if (Password == null || Password.length() < 8) {
		
			request.setAttribute("error1", "<span class=\"login100-form-title p-b-59\" style=\"font-size:20px;\">\n" + 
					"						Password can't be less than 8 characters!\n" + 
					"					</span>");
			System.out.println("bajhga");
			flag = false;
		}
		System.out.println("1"+flag);
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
					request.setAttribute("error1",  "<span class=\"login100-form-title p-b-59\" style=\"font-size:20px;\">\n" + 
							"						Username already used!\n" + 
							"					</span>");
					System.out.println(true);

					flag = false;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				System.out.println("2"+flag);
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
					request.getSession().setAttribute("username", UserName);
					
					response.sendRedirect("home.jsp");
//					conn.commit();
					conn.close();
				}else {
					RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
					rd.forward(request,response);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}else {RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
		rd.forward(request,response);
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());


	}

}
