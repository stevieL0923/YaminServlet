package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import batch.Batch;
import databaseerror.DataBaseErrorLog;
import dbConn.DbConn;
import enrollment.Enrollment;
import enrollment.EnrollmentDao;
import enrollment.JdbcEnrollmentDao;
import exception.DataBaseDeleteException;

/**
 * Servlet implementation class EnrollmentServlet
 */
@WebServlet("/enrollment")
public class EnrollmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private EnrollmentDao enrollmentTable;
	
	DataBaseErrorLog databaseLog;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnrollmentServlet() {
        super();
        enrollmentTable = new JdbcEnrollmentDao();
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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String enrollmentId  = request.getParameter("enrollmentId");
		String id2Delete = request.getParameter("id2Delete");

		
		PrintWriter htmlWriter = response.getWriter();
		List<Enrollment> theEnrollments = new ArrayList<Enrollment>();
		
		if (enrollmentId == null
				&& id2Delete == null) { 
			theEnrollments = enrollmentTable.getAllEnrollment();
		}
		
		// if the id of the Batch to be displayed was passed as a query parameter (it's not null)
		if(enrollmentId != null) {    // Use the DAO method to get the Batch with that id
			// if the Enrollment doesn't exist a null Enrollment is returned by the DAO
			Enrollment retVal = enrollmentTable.getEnrollmentById (Integer.parseInt(enrollmentId));
			if (retVal != null) {
				theEnrollments.add(retVal); // and add it to the list holding the Enrollment
			}
		}
		
		// if the id of the Enrollment to be deleted was passed as a query parameter (it's not null)
		if(id2Delete != null) {    // Use the DAO method to get the Participant with that id
			// if the Enrollment doesn't exist a null Enrollment is returned by the DAO
			Enrollment retVal = enrollmentTable.getEnrollmentById(Integer.parseInt(id2Delete));
			if (retVal != null) {
				theEnrollments.add(retVal); // and add it to the list holding the Enrollment
			}
		}	
		// Check to be sure we have found at least one Participant		
		if(theEnrollments.size() == 0) {
			htmlWriter.println("<h1 style=\"color : red;\">No Enrollments found!</h1>");
			
			// Write the HTML to provide a link back to the Participant Page
			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./batch.jsp\">Return to Batch Page</a>");
			htmlWriter.println("</div>");			
		}

		for(Enrollment aEnrollment : theEnrollments) { // Loop through the list of Enrollments
			
			// Write the HTML for setup the page
			htmlWriter.println("<title>Enrollment Info</title>");   // The name to put the browser tab
			htmlWriter.println("<link rel=\"stylesheet\" href=\"./resources/style.css\"></link>"); // Use this stylesheet file
			try {
				// Write the HTML to provide a title on the page - using data from the current Enrollment
				htmlWriter.println("<h2>Enrollment Information for Id: " + aEnrollment.getId() + "</h2>");
				
				// Write the HTML to show all the information for the current Enrollment
				htmlWriter.println("<div class=aEnrollment>");
				htmlWriter.println("<p>Enrollment Id: " + aEnrollment.getId() + "</p>");
				htmlWriter.println("<p>Participant Id: " + aEnrollment.getParticipantId() + "</p>");
				htmlWriter.println("<p>Batch Id: " + aEnrollment.getBatchId() + "</p>");

				htmlWriter.println("</div>");
			} // end of try block
			catch(NullPointerException exceptionBlock) { // If there is a NullPointerException - display an error page
				htmlWriter.println("<h1 style=\"color : red;\">Enrollment Id: " + aEnrollment.getId() + " not found!</h1>");
			} // End of catch
			
			// Write the HTML to provide a link back to the Home Page
			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./batch.jsp\">Return to Batch Page</a>");
			htmlWriter.println("</div>");
		}  // end of for-loop

		if (id2Delete != null && theEnrollments.size() > 0) {
			int retVal = 0;
			
			try {
				retVal = enrollmentTable.deleteEnrollment(Integer.parseInt(id2Delete));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DataBaseDeleteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (retVal > 0) {
				htmlWriter.println("<h2 style=\"color : blue;\">Enrollment "+ id2Delete + " has been deleted</h2>");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}
