

import java.io.IOException;
import java.io.PrintWriter;
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
import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import org.json.JSONObject;

import com.mysql.jdbc.Driver;
import com.sun.crypto.provider.RSACipher;
import com.sun.jmx.remote.util.OrderClassLoaders;

import sun.font.CreatedFontTracker;

/**
 * Servlet implementation class MeetingroomRecord
 */
@WebServlet("/MeetingroomRecord")
public class MeetingroomRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;
	java.sql.Statement statement;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MeetingroomRecord() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private void create() throws SQLException {
    	String createS = "CREATE TABLE IF NOT EXISTS meetingroom_record("
    			+ "id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,"
    			+ "name VARCHAR(20),"
    			+ "dateS VARCHAR(8),"
    			+ "number VARCHAR(10),"
    			+ "orderS VARCHAR(21)"
    			+  ");";
    	statement.execute(createS);
    }
    
    private void query() throws SQLException, IOException {
    	String number = request.getParameter("number");
    	String queryS = "SELECT * FROM meetingroom_record WHERE number=" + number + "";
    	ResultSet resultSet = statement.executeQuery(queryS);
    	Map<String, String> map = new HashMap<String,String>();
    	JSONObject jsonObject = new JSONObject();
    	while(resultSet.next()) {
    		String orderRecord = resultSet.getString("orderS"); //用于显示记录的预订序列
    		String dateRecord = resultSet.getString("dateS"); //用于显示预订日期
    		String nameRecord = resultSet.getString("name"); //显示预订人
    		map.put("orderRecord", orderRecord);
    		map.put("dateRecord", dateRecord);
    		map.put("nameRecord", nameRecord);
    	}
    	jsonObject.put("map", map);
    	PrintWriter out = response.getWriter();
    	out.write(jsonObject.toString());
    }
    
    private void insert() throws SQLException {
    	String name = request.getParameter("name");
    	String number = request.getParameter("number");
    	java.util.Date date = new java.util.Date();
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    	String dateS = simpleDateFormat.format(date);
    	String order = request.getParameter("order");
    	String insertS = "INSERT INTO meetingroom_record (name,dateS,number,orderS) VALUES ("
    			+ "\"" + name + "\",\"" + dateS + "\",\"" + number + "\",\"" + order + "\")";
    	statement.execute(insertS);
    }
    
    private void delete() throws Exception{
    	String name = request.getParameter("name");
    	String number = request.getParameter("number");
    	String dateS = request.getParameter("date");
    	String order = request.getParameter("order");
    	String dropS = "DELETE FROM meetingroom_record WHERE name=\"" + name + "\""
    			+ " AND number=" + number 
    			+ " AND dateS=\"" + dateS + "\""
    			+ " AND orderS=\"" + order + "\""
    			+ ";"
    			;
    	statement.execute(dropS);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		this.response = response;
		this.request = request;
		java.sql.Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/meetingroom_record","root","2289108");
			statement = connection.createStatement();
			String action = request.getParameter("action");
			create();
			switch (action) {
			case "insert":insert();break;
			case "delete":delete();break;
			case "query":query();
				break;

			default:
				break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
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
