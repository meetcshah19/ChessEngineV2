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

public class SaveGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SaveGame() {
		super();
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	Integer cid=(int)request.getSession().getAttribute("uid");
			
	String pgn = request.getParameter("pgn");
	Connection conn = DBConnection.getConnection();
	Statement createStatement = null;
	ResultSet executeQuery = null;
	System.out.println(request.getParameter("cid")+pgn);
	try {
		createStatement = conn.createStatement();
		 createStatement.execute("insert into game (uid,pgn) values("+cid.toString()+",'"+pgn+"');");
		System.out.println(executeQuery);
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
	}

}
