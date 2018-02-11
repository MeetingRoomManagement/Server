

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class ClientServlet
 */
@WebServlet("/ClientServlet")
public class ClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HttpServletResponse response;
	private HttpServletRequest request;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void create(java.sql.Statement stmt) throws SQLException {
    	String createS = "CREATE TABLE IF NOT EXISTS client_manager(" + //验证正确性！！！！
				"   id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY," + 
				"   name VARCHAR(20)," + 
				"   password VARCHAR(21)," + ///8888 
				");";
    	stmt.execute(createS);
    }
    
    public void sendError(String Error) throws IOException {
    	Map<String, String> map = new HashMap<String,String>();
    	JSONObject jsonObject = new JSONObject();
    	map.put("error", Error);
    	jsonObject.put("map",map);
    	PrintWriter out = response.getWriter();
    	out.write(jsonObject.toString());
    }
    
    public boolean query(java.sql.Statement stmt) {
    	String name = request.getParameter("name");
    	String QueryS = "SELECT * from meetingroom_manager where name=" + name;//补充数据库语句
    	try {
    		ResultSet rs = stmt.executeQuery(QueryS);
    		if(rs.next()) {
    			return true;
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
		return false;
    }
    
    public void register(java.sql.Statement stmt) throws SQLException, IOException {
    	if(query(stmt)) {
    		sendError("existed");
    		return;
    	}
    	String name = request.getParameter("name");
    	String password = request.getParameter("request");
    	String registerS = "INSERT INTO meetingroom_manager (name,password)" +
				"VALUES (" + name + "," + password + ")";
    	stmt.execute(registerS);
    }
    
    public void signIn(java.sql.Statement stmt) throws Exception {
    	if(query(stmt)) {
    		String name = request.getParameter("name");
        	String QueryS = "SELECT * from meetingroom_manager where name=" + name;
        	try {
        		ResultSet rs = stmt.executeQuery(QueryS);
        		if(rs.next()) {
        			String passwordS = rs.getString("password");
        			String password = request.getParameter("password");
       			JSONObject jsonObject = new JSONObject();
    				Map<String,String> map = new HashMap<String,String>();
        			if(password.equals(passwordS)) {
        				map.put("result", "correct");
        			}
        			else {
        				map.put("result", "incorrect");
        			}
        			jsonObject.put("map", map);
        			PrintWriter out = response.getWriter();
        			out.write(jsonObject.toString());
        		}
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
    	}
    	else {
    		sendError("notExisted");
    	}
    }
    
    public void update(java.sql.Statement stmt) throws Exception{
    	if(query(stmt)) {
    		String name = request.getParameter("name");
    		String password = request.getParameter("password");
    		String QueryS = "UPDATE client_manager SET password=" + password + " WHERE name=" + name;
    		stmt.execute(QueryS);
    	}
    	else {
    		sendError("notExisted");
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		this.request = request;
		this.response = response;
		java.sql.Connection conn = null;
    	try{
    		Class.forName("com.mysql.jdbc.Driver");
    		conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/client_manager","root","2289108");//数据库地址
    		java.sql.Statement stmt = conn.createStatement();
    		String action = request.getParameter("action");
    		create(stmt);
    		switch(action){
    		case "register": register(stmt);break;
    		case "signIn": signIn(stmt);break;
    		case "alter": update(stmt);break;
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
