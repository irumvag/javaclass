package com.dineix.web.servlet.home;
import com.dineix.web.models.home.TechStackItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AboutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Sample tech stack data (replace with database query in production)
        List<TechStackItem> techStack = new ArrayList<>();
        techStack.add(new TechStackItem("Java EE", "filetype-java", "Enterprise-grade backend framework"));
        techStack.add(new TechStackItem("MySQL", "database", "Robust relational database"));
        techStack.add(new TechStackItem("Bootstrap 5", "bootstrap", "Responsive frontend framework"));
        techStack.add(new TechStackItem("JWT Auth", "shield-check", "Secure authentication mechanism"));

        // Set tech stack as request attribute
        request.setAttribute("techStack", techStack);

        // Forward to JSP
        request.getRequestDispatcher("about.jsp").forward(request, response);
    }
}