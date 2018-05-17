package gov.nasa.pds.model.plugin;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;


import java.util.Arrays;

@WebServlet("/TermmapServlet")
public class TermmapServlet extends HttpServlet {
	public void doGe(HttpServletRequest request, HttpServletResponse response)
			throws IOException{
		
		        String param1 = request.getParameter("param1");
	            String param2 = request.getParameter("param2");
				PrintWriter out = response.getWriter();
				out.println("<html>");
				
				out.println("<head>");
				out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>PDS Servlet Get</h1>");
				out.write("<p>param1: " + param1 + "</p>");
			    out.write("<p>param2: " + param2 + "</p>");
				out.println("</body>");
				out.println("</html>");
				// get parameters from request
		      
		  //      String[] paramArray = req.getParameterValues("paramArray");
        //example
		//URL: http://localhost:8080/servlet-parameter/parameters?param1=hello&param2=world&paramArray=1&paramArray=2&paramArray=3
		       
		     
		   
			}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException{
		        String pds3Str = (String) request.getParameter("Param");
		   //     System.out.println(pds3Str);
		        String returnStr = "<h1>PDS Servlet POST</h1>";
		        if (pds3Str != null)
		        	returnStr = "<h1>PDS Servlet POST param name is : " + pds3Str + "</h1>";
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("<body>");
			//	out.println("<h1>PDS Servlet POST</h1>");
				out.println(returnStr);
				out.println("</body>");
				out.println("</html>");
			}

}
