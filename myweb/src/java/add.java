
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/add"})
public class add extends HttpServlet 
{
   public void service(HttpServletRequest req, HttpServletResponse res) throws IOException
   {
       int i=Integer.parseInt(req.getParameter("t1"));
       int j=Integer.parseInt(req.getParameter("t2"));
       int k=i+j;
       PrintWriter out=res.getWriter();
       out.print(k);
       
   }
}
   