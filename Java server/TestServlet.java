package test;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.Properties;
import java.util.Base64;

@WebServlet(name = "t", urlPatterns = {"/tests/*"})

public class TestServlet extends HttpServlet {

	private static final String LOCATION = "C:\\Program Files\\Java\\apache-tomcat-9.0.14\\webapps\\TestServlet\\WEB-INF\\config.conf";
	private String USERNAME = "admin";
	private String PASSWORD = "password";
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
				
		boolean success = checkAuth(request);
		
		if (success){
					
			// Set the response message's MIME type
			response.setContentType("text/html;charset=UTF-8");
			//Allocate a output writer to write the response message into the network socket
			PrintWriter out = response.getWriter();
		
			//Write the response message, in HTML page
			try{
				out.println("<!DOCTYPE html>");
				out.println("<html><head>");
				out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
				out.println("<title>Hello, ez a mi tesztünk!</title></head>");
				out.println("<body>");
				out.println("<h1>Hello Gyula!</h1>");
				//Echo client's request information
				out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
				out.println("<p>Protocol: " + request.getProtocol() + "</p>");
				out.println("<p>PathInfo: " + request.getPathInfo() + "</p>");
				out.println("<p>Remote Address: " + request.getRemoteAddr() + "</p>");
				//Generate a random number upon each request
				out.println("<p>A Random Number: <strong>" + Math.random() + "</strong></p>");
				out.println("</body>");
				out.println("</html>");
			} finally {
				out.close();	//Always close the output writer
			}
		}else{
			noAccess(response);
		}
	}
	
	private boolean checkAuth(HttpServletRequest request){
	
		String authValue = request.getHeader("Authorization");
		if( authValue == null){
			return false;
		}else {
			if(!authValue.toUpperCase().startsWith("BASIC ")){
				return false;
			}else{
				String userpass = new String(Base64.getDecoder().decode(authValue.substring(6)));
				int counter = userpass.indexOf(":");
				String observedUsername = userpass.substring(0,counter);
				String observedPassword = userpass.substring(counter+1);
				if (observedUsername.equals(USERNAME) && observedPassword.equals(PASSWORD))
					return true;
			}
		}
		return false;
	}
	
	private void noAccess(HttpServletResponse response){
		try{
			response.sendError(401);
		}catch (Exception e){
			System.out.println(""+ e);
		}
		
	}
	
	@Override
	public void destroy(){
		
	}
	
	@Override
	public void init(){
	
		try{
			//System.getenv("IDE A NÉV KELL"); //így lehet környezeti változóból meghívni
			Properties pr = new Properties();
			File conf = new File(LOCATION);
			if (conf.exists() && conf.canRead()){
				System.out.println("Mondom a fájlból kiszedett adatokat:");
				pr.load(new FileInputStream(conf));
					String username = pr.getProperty("dbusername");
					String password = pr.getProperty("dbpassword");
					System.out.println("user: " + username + " password: " + password);
					
			}else{
			System.out.println("Nincs meg a fájl");
			}
		} catch (Exception e){
			e.printStackTrace(System.err);
		}
			
	}
	
	@Override
	public String getServletInfo(){
		return "Ez egy teszt szervlet";
	}
}