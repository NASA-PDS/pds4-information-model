// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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
