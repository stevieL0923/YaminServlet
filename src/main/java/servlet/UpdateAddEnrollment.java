package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import batch.Batch;
import batch.BatchDao;
import batch.JdbcBatchDao;
import databaseerror.DataBaseErrorLog;
import dbConn.DbConn;
import enrollment.Enrollment;
import enrollment.EnrollmentDao;
import enrollment.JdbcEnrollmentDao;

/**
 * Servlet implementation class UpdateAddEnrollment
 */
@WebServlet("/updateAddEnrollment")
public class UpdateAddEnrollment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private EnrollmentDao enrollmentTable;
	
	DataBaseErrorLog databaseLog;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateAddEnrollment() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		PrintWriter htmlWriter = response.getWriter();
		
		String participantId = request.getParameter("participantId");
		String batchId  =  request.getParameter("batchId");

		htmlWriter.println("<title>Update Enrollment Record</title>");   // The name to put the browser tab
		htmlWriter.println("<link rel=\"stylesheet\" href=\"./resources/style.css\"></link>"); // Use this stylesheet file

		if ((participantId == null) &&
			(batchId == null)) 
		{
			htmlWriter.println("<h1 style=\"color : red;\">No Enrollment updated or added!</h1>");
			return;
		}
		
		Enrollment newEntry = new Enrollment(Integer.parseInt(participantId), Integer.parseInt(batchId));
		
		// Delete 
		try {

			enrollmentTable.addEnrollment(newEntry);
			htmlWriter.println("<h2 style=\"color : blue;\">Enrollment " + participantId + " has been added</h2>");


			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./batch.jsp\">Return to Batch Page</a>");
			htmlWriter.println("</div>");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

