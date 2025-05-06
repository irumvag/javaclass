package com.mycompany.assingment;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.mycompany.assingment.dao.CourseDao;
import com.mycompany.assingment.model.course;

/**
 * Servlet implementation class CourseServlet
 */
//@WebServlet("/Course")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private CourseDao dao;
    public CourseServlet() {
        super();
        // TODO Auto-generated constructor stub
        dao=new CourseDao();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher view=request.getRequestDispatcher("courses.jsp");
		request.setAttribute("courses", dao.getAllCourseces());
		view.forward(request,response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		course c=new course();
		c.setCoursename(request.getParameter("coursename"));
		c.setCategory(request.getParameter("coursecategory"));
		c.setDuration(Integer.parseInt(request.getParameter("courseduration")));
		c.setFees(Double.parseDouble(request.getParameter("coursefees")));
		String Courseid=request.getParameter("courseid");
		if(Courseid==null || Courseid.isEmpty())
		{
			dao.addCourse(c);
			request.getRequestDispatcher("view").forward(request, response);
		}
		else {
		c.setCourseid(Integer.parseInt(Courseid));
		c.setCoursename(request.getParameter("coursename"));
		c.setCategory(request.getParameter("coursecategory"));
		c.setDuration(Integer.parseInt(request.getParameter("courseduration")));
		c.setFees(Double.parseDouble(request.getParameter("coursefees")));
		dao.addCourse(c);
		request.getRequestDispatcher("courses.jsp").forward(request, response);
		
		}
		
	}

}
