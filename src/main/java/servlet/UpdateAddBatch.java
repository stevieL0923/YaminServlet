package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import participant.Participant;

import java.io.IOException;
import java.io.PrintWriter;

import batch.Batch;
import batch.BatchDao;
import batch.JdbcBatchDao;
import databaseerror.DataBaseErrorLog;
import dbConn.DbConn;

/**
 * Servlet implementation class UpdateAddBatch
 */
@WebServlet("/updateAddBatch")
public class UpdateAddBatch extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private BatchDao batchTable;
	
	DataBaseErrorLog databaseLog;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateAddBatch() {
        super();
        batchTable = new JdbcBatchDao();
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
		
		String id       = request.getParameter("batchId");
		String timeSlot = request.getParameter("timeSlot");
		String session  =  request.getParameter("session");
		boolean isMorning = false;
		if (session.equals("morning")) {
			isMorning = true;
		}

		htmlWriter.println("<title>Update Batch Record</title>");   // The name to put the browser tab
		htmlWriter.println("<link rel=\"stylesheet\" href=\"./resources/style.css\"></link>"); // Use this stylesheet file

		if ((id == null) &&
			(timeSlot == null) &&
			(session == null)) 
		{
			htmlWriter.println("<h1 style=\"color : red;\">No Batches updated or added!</h1>");
			return;
		}
			
		Batch newEntry = new Batch(Integer.parseInt(id), Integer.parseInt(timeSlot), isMorning);
		
		// Delete 
		try {

			batchTable.addBatch(newEntry);
			htmlWriter.println("<h2 style=\"color : blue;\">Batch " + id + " has been added</h2>");


			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./batch.jsp\">Return to Batch Page</a>");
			htmlWriter.println("</div>");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
