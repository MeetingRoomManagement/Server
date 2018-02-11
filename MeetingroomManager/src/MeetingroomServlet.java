

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class MeetingroomServlet
 */
@WebServlet("/MeetingroomServlet")
public class MeetingroomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MeetingroomServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void create(java.sql.Statement stmt) throws SQLException {
    	String createS = "CREATE TABLE IF NOT EXISTS meetingroom_manager(" + //验证正确性！！！！
				"   id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY," + 
				"   number VARCHAR(20)," + 
				"   orderS VARCHAR(21)," + ///8888
				"   dateS TEXT" + 
				");";
    	stmt.execute(createS);
    }
    
    public void send(String date,String order) throws Exception {
    	/*response.setContentType("text/html;charset=utf-8");  
        request.setCharacterEncoding("utf-8");  
        response.setCharacterEncoding("utf-8");*/  
    	Map<String, String> map = new HashMap<String,String>();
    	JSONObject jsonObject = new JSONObject();
    	map.put("date", date);
    	map.put("order", order);
    	map.put("error", "0");
    	jsonObject.put("map",map);
    	PrintWriter out = response.getWriter();
    	out.write(jsonObject.toString());
    }
    
    public void sendError(String error) throws IOException {
    	Map<String, String> map = new HashMap<String,String>();
    	JSONObject jsonObject = new JSONObject();
    	map.put("error", error);
    	jsonObject.put("map",map);
    	PrintWriter out = response.getWriter();
    	out.write(jsonObject.toString());
    }
    
    public void sendNoError() throws IOException {
    	Map<String, String> map = new HashMap<String,String>();
    	JSONObject jsonObject = new JSONObject();
    	map.put("error", "0");
    	jsonObject.put("map",map);
    	PrintWriter out = response.getWriter();
    	out.write(jsonObject.toString());
    }
    
    public void update(String number, String dateS,String order,java.sql.Statement stmt) {
    	String QueryS = "SELECT * from meetingroom_manager where number=" + number;//补充数据库语句
    	try {
    		ResultSet rs = stmt.executeQuery(QueryS);
    		if(rs.next()) {
    			String date = rs.getString("dateS");	
    			System.out.printf("text,%s,%s/n",date,dateS);
    			if(date.equals(dateS)) {
    				String updateS = "UPDATE meetingroom_manager SET orderS = " + order + " WHERE number = " + number;
    				System.out.printf("%s",updateS);
    				stmt.execute(updateS);
    				sendNoError();
    			}
    		}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		try {
				sendError("updateError");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    }
    
    public void addMeetingroom(java.sql.Statement stmt) throws IOException {
    	String number = request.getParameter("number");
    	String QueryS = "SELECT * from meetingroom_manager where number=" + number;//补充数据库语句
    	try {
    		ResultSet rs = stmt.executeQuery(QueryS);
    		if(rs.next()) {
    			sendError("existed");
    			return;
    		}
    	}
    	catch (Exception e) {
    		sendError("addError");
    		e.printStackTrace();
    	}
    	String order = "000000000000000000000000";
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	String date = sdf.format(new java.util.Date());
    	System.out.printf("%s",date);
    	try {
    		String addS = "INSERT INTO meetingroom_manager (number,orderS,dateS)" +
					"VALUES (" + number + "," + order + "," + date + ")";
    		stmt.execute(addS);
    		sendNoError();
    	}
    	catch (Exception e) {
    		sendError("addError");
    		e.printStackTrace();
    	}
    	
    }
    
    public void query(java.sql.Statement stmt) throws IOException {
    	String number = request.getParameter("number");
    	String date;
    	String order;
    	String QueryS = "SELECT * from meetingroom_manager where number=" + number;//补充数据库语句
    	try {
    		ResultSet rs = stmt.executeQuery(QueryS);
    		if(rs.next()) {
    		date = rs.getString("dateS");
    		order = rs.getString("orderS");
    		send(date,order);
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		sendError("queryError");
    	}
    }
    
    public void reserve(java.sql.Statement stmt) {
    	String dateS = request.getParameter("date");
		String order = request.getParameter("order");//直接发过来01序列
		String number = request.getParameter("number");
		update(number,dateS,order,stmt);
    }
    
    public void cancel(java.sql.Statement stmt) {
    	String dateS = request.getParameter("date");
		String order = request.getParameter("order");//直接发过来01序列
		String number = request.getParameter("number");
		update(number,dateS,order,stmt);
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
    		conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/meetingroom_manager","root","2289108");//数据库地址
    		java.sql.Statement stmt = conn.createStatement();
    		String action = request.getParameter("action");
    		create(stmt);
    		switch(action){
    		case "query" :query(stmt); break;
    		case "reserve": reserve(stmt);break;
    		case "cancel": cancel(stmt);break;
    		case "add": addMeetingroom(stmt);break;
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
