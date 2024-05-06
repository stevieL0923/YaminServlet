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
import batch.BatchDao;
import batch.JdbcBatchDao;
import databaseerror.DataBaseErrorLog;
import dbConn.DbConn;
import exception.DataBaseDeleteException;

/**
 * Servlet implementation class BatchServlet
 */
@WebServlet("/batch")
public class BatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BatchDao batchTable;
	
	DataBaseErrorLog databaseLog;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BatchServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		// response.getWriter().append("Served at: ").append(request.getContextPath());

		String batchId   = request.getParameter("batchId");
		String id2Delete = request.getParameter("id2Delete");

		PrintWriter htmlWriter = response.getWriter();
		List<Batch> theBatches = new ArrayList<Batch>();
		
		if (batchId == null
			&& id2Delete == null) { 
			
			theBatches = batchTable.getAllBatch();
		}
		
		// if the id of the Batch to be displayed was passed as a query parameter (it's not null)
		if(batchId != null) {    // Use the DAO method to get the Batch with that id
			// if the Batch doesn't exist a null Batch is returned by the DAO
			Batch retVal = batchTable.getBatchById(Integer.parseInt(batchId));
			if (retVal != null) {
				theBatches.add(retVal); // and add it to the list holding the Batch
			}
		}
		
		// if the id of the Batch to be deleted was passed as a query parameter (it's not null)
		if(id2Delete != null) {    // Use the DAO method to get the Participant with that id
			// if the Batch doesn't exist a null Batch is returned by the DAO
			Batch retVal = batchTable.getBatchById(Integer.parseInt(id2Delete));
			if (retVal != null) {
				theBatches.add(retVal); // and add it to the list holding the Participants
			}
		}	

		// Check to be sure we have found at least one Participant		
		if(theBatches.size() == 0) {
			htmlWriter.println("<h1 style=\"color : red;\">No Batches found!</h1>");
			
			// Write the HTML to provide a link back to the Participant Page
			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./batch.jsp\">Return to Batch Page</a>");
			htmlWriter.println("</div>");			
		}

		for(Batch aBatch : theBatches) { // Loop through the list of Batchs
			
			// Write the HTML for setup the page
			htmlWriter.println("<title>Batch Info</title>");   // The name to put the browser tab
			htmlWriter.println("<link rel=\"stylesheet\" href=\"./resources/style.css\"></link>"); // Use this stylesheet file
			try {
				// Write the HTML to provide a title on the page - using data from the current Batch
				htmlWriter.println("<h2>Batch Information for Id: " + aBatch.getId() + "</h2>");
				
				// Write the HTML to show all the information for the current Batch
				htmlWriter.println("<div class=aBatch>");
				htmlWriter.println("<p>Batch Id: " + aBatch.getId() + "</p>");
				htmlWriter.println("<p>TimeSlot: " + aBatch.getTimeslot()+ "</p>");
				htmlWriter.println("<p>Morning: " + aBatch.isMorning() + "</p>");
				htmlWriter.println("</div>");
			} // end of try block
			catch(NullPointerException exceptionBlock) { // If there is a NullPointerException - display an error page
				htmlWriter.println("<h1 style=\"color : red;\">Batch Id: " + aBatch.getId() + " not found!</h1>");
			} // End of catch
			
			// Write the HTML to provide a link back to the Home Page
			htmlWriter.println("<div>");
			htmlWriter.println("<a href=\"./batch.jsp\">Return to Batches Page</a>");
			htmlWriter.println("</div>");
		}  // end of for-loop
		
		if (id2Delete != null && theBatches.size() > 0) {
			int retVal = 0;
			
			try {
				retVal = batchTable.deleteBatch(Integer.parseInt(id2Delete));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DataBaseDeleteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (retVal > 0) {
				htmlWriter.println("<h2 style=\"color : blue;\">Batch "+ id2Delete + " has been deleted</h2>");
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
