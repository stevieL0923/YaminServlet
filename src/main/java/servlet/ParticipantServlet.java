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
import java.util.ArrayList;
import java.util.List;

import databaseerror.DataBaseErrorLog;
import dbConn.DbConn;
import exception.DataBaseDeleteException;

/**
 * Servlet implementation class ParticipantServlet
 */
@WebServlet("/participant")
public class ParticipantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ParticipantDao participantTable;
	
	DataBaseErrorLog databaseLog;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ParticipantServlet() {
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
//		response.getWriter().append("Served at: ").append(request.getContextPath());
				
		PrintWriter htmlWriter = response.getWriter();
		
		String idRequested   = request.getParameter("id");
		String nameRequested = request.getParameter("fullName");
		String id2Delete     = request.getParameter("id2Delete");

		List<Participant> theParticipants = new ArrayList<Participant>();
		
		// Check all parameters
		if(idRequested   == null
		&& nameRequested == null
		&& id2Delete     == null)
		{
			// Get all participants if all params are blank for doGet()
			theParticipants = participantTable.getAllParticipant();
		}
		
		// if the id of the Participant to be displayed was passed as a query parameter (it's not null)
		if(idRequested != null) {    // Use the DAO method to get the Participant with that id
			// if the Participant doesn't exist a null Participant is returned by the DAO
			Participant retVal = participantTable.getParticipantById(Integer.parseInt(idRequested));
			if (retVal != null) {
				theParticipants.add(retVal); // and add it to the list holding the Participants
			}
		}	
		
		// if the name of the Participant to be displayed was passed as a query parameter (it's not null)
		if (nameRequested != null) {   // Use the DAO method to get the Participant whose name contains the value
			
			Participant retVal = participantTable.getParticipantByName(nameRequested);
			
			// If at least one Participant doesn't have the name - an empty List is returned
			if (retVal != null) {
				theParticipants.add(retVal); // and add it to the list holding the Participants
			}
		}

		// if the id of the Participant to be deleted was passed as a query parameter (it's not null)
		if(id2Delete != null) {    // Use the DAO method to get the Participant with that id
			// if the Participant doesn't exist a null Participant is returned by the DAO
			Participant retVal = participantTable.getParticipantById(Integer.parseInt(id2Delete));
			if (retVal != null) {
				theParticipants.add(retVal); // and add it to the list holding the Participants
			}
		}	

		// Check to be sure we have found at least one Participant		
		if(theParticipants.size() == 0) {
			htmlWriter.println("<h1 style=\"color : red;\">No Participants found!</h1>");
			
			// Write the HTML to provide a link back to the Participant Page
			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./participant.jsp\">Return to Participant Page</a>");
			htmlWriter.println("</div>");			
		}
		
		for(Participant aParticipant : theParticipants) { // Loop through the list of Participants
			
			// Write the HTML for setup the page
			htmlWriter.println("<title>Participant Info</title>");   // The name to put the browser tab
			htmlWriter.println("<link rel=\"stylesheet\" href=\"./resources/style.css\"></link>"); // Use this stylesheet file
			try {
				// Write the HTML to provide a title on the page - using data from the current Participant
				htmlWriter.println("<h2>Participant Information for Id: " + aParticipant.getParticipantId() + "</h2>");
				
				// Write the HTML to show all the information for the current Participant
				htmlWriter.println("<div class=aParticipant>");
				htmlWriter.println("<p>Name: " + aParticipant.getFullName() + "</p>");
				htmlWriter.println("<p>Email: " + aParticipant.getEmail()+ "</p>");
				htmlWriter.println("<p>Phone: " + aParticipant.getPhone() + "</p>");
				htmlWriter.println("<p>Morning Session: " + aParticipant.isMorningSession() + "</p>");
				htmlWriter.println("</div>");
			} // end of try block
			catch(NullPointerException exceptionBlock) { // If there is a NullPointerException - display an error page
				htmlWriter.println("<h1 style=\"color : red;\">Participant Id: " + aParticipant.getParticipantId() + " not found!</h1>");
			} // End of catch
			
			// Write the HTML to provide a link back to the Home Page
			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./participant.jsp\">Return to Participants Page</a>");
			htmlWriter.println("</div>");
		}  // end of for-loop
		
		if (id2Delete != null && theParticipants.size() > 0) {
			int retVal = 0;
			
			try {
				retVal = participantTable.deleteParticipant(Integer.parseInt(id2Delete));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DataBaseDeleteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (retVal > 0) {
				htmlWriter.println("<h2 style=\"color : blue;\">Participant "+ id2Delete + " has been deleted</h2>");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String id2Update     = request.getParameter("id2Update");
		String action        = request.getParameter("action");
		
		PrintWriter htmlWriter = response.getWriter();
		
		if (action.equals("input")) {
			if (id2Update != null) {
				// if the Participant doesn't exist a null Participant is returned by the DAO
				Participant retVal = participantTable.getParticipantById(Integer.parseInt(id2Update));
				
				if (retVal != null) {
					// Write the HTML to provide a title on the page - using data from the current Participant
					htmlWriter.println("<title>Update Participant Info</title>");   // The name to put the browser tab
					htmlWriter.println("<link rel=\"stylesheet\" href=\"./resources/style.css\"></link>"); // Use this stylesheet file


					htmlWriter.println("<h2>Update Information for Id: " + id2Update + "</h2><br>");
					
					htmlWriter.println("<div class=aParticipant>");
					htmlWriter.println("<form name=\"updateForm\" method=\"post\" action=\"updateAddParticipant?updateId=" + id2Update +"\">");
					htmlWriter.println("Full Name: <input type=\"text\" name=\"username\" placeholder = \"" +retVal.getFullName()+ "\" required/> <br/>");
					htmlWriter.println("Email: <input type=\"text\" name=\"email\" placeholder = \"" +retVal.getEmail()+ "\" required/> <br/>");
					htmlWriter.println("Phone: <input type=\"text\" name=\"phone\" placeholder = \"" +retVal.getPhone()+ "\" required/> <br/>");
					
					if (retVal.isMorningSession()) {
						htmlWriter.println("<input type=\"radio\" id=\"morning\" name=\"session\" value=\"morning\" checked=\"checked\" required>");
						htmlWriter.println("<label for=\"morning\">Morning Session</label><br>");
						htmlWriter.println("<input type=\"radio\" id=\"evening\" name=\"session\" value=\"evening\" required>");
						htmlWriter.println("<label for=\"evening\">Evening Session</label><br>");
					} else {
						htmlWriter.println("<input type=\"radio\" id=\"morning\" name=\"session\" value=\"morning\" required>");
						htmlWriter.println("<label for=\"morning\">Morning Session</label><br>");
						htmlWriter.println("<input type=\"radio\" id=\"evening\" name=\"session\" value=\"evening\" checked=\"checked\" required>");
						htmlWriter.println("<label for=\"evening\">Evening Session</label><br>");
					}
					
					htmlWriter.println("<input type=\"submit\" value=\"Update\" />");
					htmlWriter.println("</form>");
					
					htmlWriter.println("</div>");
					
				} else {
					htmlWriter.println("<h1 style=\"color : red;\">Participant Id: " + id2Update + " not found!</h1>");
				}
			}
		}
	}
}
