package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import participant.JdbcParticipantDao;
import participant.Participant;
import participant.ParticipantDao;

import java.io.IOException;
import java.io.PrintWriter;

import databaseerror.DataBaseErrorLog;
import dbConn.DbConn;

/**
 * Servlet implementation class UpdateAddParticipant
 */
@WebServlet("/updateAddParticipant")
public class UpdateAddParticipant extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ParticipantDao participantTable;
	
	DataBaseErrorLog databaseLog;
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateAddParticipant() {
        super();
        participantTable = new JdbcParticipantDao();
    }
    
    @Override
    public void init() {
    	DbConn.getConn();
    }
    
    @Override
    public void destroy() {
    	DbConn.close();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter htmlWriter = response.getWriter();
		
		String updateId = request.getParameter("updateId");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String session = request.getParameter("session");
		boolean isMorning = false;
		if (session.equals("morning")) {
			isMorning = true;
		}

		htmlWriter.println("<title>Update Participant Record</title>");   // The name to put the browser tab
		htmlWriter.println("<link rel=\"stylesheet\" href=\"./resources/style.css\"></link>"); // Use this stylesheet file

		if ((username == null) &&
			(email == null) &&
			(phone == null) &&
			(session == null)) 
		{
			htmlWriter.println("<h1 style=\"color : red;\">No participant updated or added!</h1>");
			return;
		}
		
		
		System.out.println("DEBUG: username = " + username + " updateId = " + updateId);
		
		Participant newEntry = new Participant(username, email, phone, isMorning);
		
		// Delete 
		try {
			if(updateId != null) {
				participantTable.updateParticipant(newEntry);
				htmlWriter.println("<h2 style=\"color : blue;\">Participant " + username + " has been updated</h2>");

			}
			else {
				participantTable.addParticipant(newEntry);
				htmlWriter.println("<h2 style=\"color : blue;\">Participant " + username + " has been added</h2>");

			}

			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./participant.jsp\">Return to Participants Page</a>");
			htmlWriter.println("</div>");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
