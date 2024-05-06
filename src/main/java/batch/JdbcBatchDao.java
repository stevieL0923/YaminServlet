package batch;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dbConn.DbConn;
import exception.DataBaseDeleteException;
import exception.DataBaseInsertException;
import exception.DataBaseUpdateException;

public class JdbcBatchDao implements BatchDao {


	// Instantiate a JdbcTemplate object and assign to reference in the constructor
	public JdbcBatchDao() {	}
	
	@Override
	public List<Batch> getAllBatch() {
		ResultSet results;
		
		// Define a String with SQL to be run
		String allBatches = "select * from batch"; // Note ; not required in at then end of SQL string
		
		// Define the object containing all batches
		List<Batch> batchList = new ArrayList<Batch>();
		
		try {

			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(allBatches);
			
			results = preparedStatement.executeQuery();
			
			while(results.next()) {
				batchList.add(batchHelper(results));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return batchList;

	}

	// Convert a row in the results from a Select into a Batch object
	// It's private to prevent others outside this class from being able to use
	// Since its requires an object (SqlRowSet) created in this class it doesn't
	//       make sense to allow other outside the class to use
	
	private Batch batchHelper(ResultSet aRow) {
	// Define an object to be returned
		Batch aBatch = new Batch(); // A Batch object initialized to default values
		
	 // Use the setters for the POJO to set the values from each column the SqlRowSet
		try {
			String id = aRow.getString("batch_id");
			aBatch.setId(Integer.parseInt(id));
			
			String timeSlot = aRow.getString("timeSlot");
			aBatch.setTimeslot(Integer.parseInt(timeSlot));
	
			String morning = aRow.getString("morning");
			aBatch.setMorning(Boolean.parseBoolean(morning));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Return the object created in this method
		return aBatch;
	}
	
	@Override
	public Batch getBatchById(int id) {
		ResultSet results;

		Batch aBatch = null;
		
		String sqlString = " select * from batch"
	              + " where id = ?";
	
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(sqlString);

			preparedStatement.setInt (1, id);
		
			results = preparedStatement.executeQuery();
		
			if(results.next()) {   // if the Batch was found, position the result at the first row
				aBatch = batchHelper(results); // Send the result to conversion method
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return aBatch;
	}

	@Override
	public void addBatch(Batch aBatch) throws DataBaseInsertException {
		// Define a String to hold the SQL to be sent to the database manager
		String sqlString = " insert into batch " 
				              +" (batch_id, timeSlot, morning) "
				              +" values(?, ?, ?)";		
		
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(sqlString);
			
			preparedStatement.setInt( 1, aBatch.getId());
			preparedStatement.setInt( 2, aBatch.getTimeslot());
			preparedStatement.setBoolean( 3, aBatch.isMorning());
			
			int numberRowsAdded = preparedStatement.executeUpdate();
			
			if(numberRowsAdded != 1) {
				throw new DataBaseInsertException("Attempt to add Batch \"" + aBatch.getId() + "\"");
			}
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// return to caller
		return;
	}

	@Override
	public boolean updateBatch(Batch aBatch) throws DataBaseUpdateException {
		// Define return value variable
		boolean updateWorked = false;

		// Define String to hold SQL statement to be sent to database manager
		//      with "?" where values in program variables should be used
		//       and where clause to be sure only specific batch is updated
		// Note: Primary Key values should NEVER be updated (per the Relational Data Model)
		String updateSQL = " update batch "
				          +"    set timeSlot  = ? "
				          +",       morning   = ? "
				          +" where  batch_id  = ?";
		
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(updateSQL);
			
			preparedStatement.setInt(     1, aBatch.getTimeslot());
			preparedStatement.setBoolean( 2, aBatch.isMorning());
			preparedStatement.setInt(     3, aBatch.getId());
			
			int numberRowsUpdated = preparedStatement.executeUpdate();

			// Check to be sure only one row was updated		
			if(numberRowsUpdated == 1) {
				updateWorked = true;
			}
			else 
			{
				throw new DataBaseUpdateException("Attempt to update Batchs \"" + aBatch.getId() + "\"");
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return updateWorked;
	}

	@Override
	public int deleteBatch(int id) throws DataBaseDeleteException {
		int numberRowsDeleted = 0;

		// Define String to hold SQL statement to send to database manager
		
		String updateSQL = "delete from batch where id = ?";
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(updateSQL);
			
			preparedStatement.setInt( 1, id);

			numberRowsDeleted = preparedStatement.executeUpdate();

			// Check to be sure only one row was deleted
			if(numberRowsDeleted != 1) {
				throw new DataBaseDeleteException("Attempt to delete Participant " + id + "\"");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return numberRowsDeleted;
	}
}
