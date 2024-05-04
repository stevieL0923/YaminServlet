package batch;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import exception.DataBaseDeleteException;
import exception.DataBaseInsertException;
import exception.DataBaseUpdateException;

public class JdbcBatchDao implements BatchDao {

	// Define a reference to a JdbcTemplateObject we can use to access the tables in the database
	private JdbcTemplate batchDB;

	// Instantiate a JdbcTemplate object and assign to reference in the constructor
	public JdbcBatchDao(DataSource aDataSource) {
		batchDB = new JdbcTemplate(aDataSource);
	}
	
	@Override
	public List<Batch> getAllBatch() {
		// Define a String with SQL to be run
		String allBatches = "select * from batch"; // Note ; not required in at then end of SQL string
		
		// Send the SQL String to the database manage and store result in an SqlRowSet
		SqlRowSet rowSet = batchDB.queryForRowSet(allBatches);
		
		// Define the object containing all batches
		List<Batch> batchList = new ArrayList<Batch>();
		
		// "Parse the result" - convert the rows from select into batch objects object
		//                      using a method to retrieve each row, create a batch
		//                         then add the batch object to the list
		
		// Loop through the SqlRowset converting the rows in it, one at a time
		while(rowSet.next()) { // Position at the next row (if there is one)
			batchList.add(batchHelper(rowSet)); // Convert current row to an object
		}
		
		return batchList;  // Return the object containing all the batchs

	}

	// Convert a row in the results from a Select into a Batch object
	// It's private to prevent others outside this class from being able to use
	// Since its requires an object (SqlRowSet) created in this class it doesn't
	//       make sense to allow other outside the class to use
	
	private Batch batchHelper(SqlRowSet aRow) {
	// Define an object to be returned
		Batch aBatch = new Batch(); // A Batch object initialized to default values
		
	 // Use the setters for the POJO to set the values from each column the SqlRowSet
		String id = aRow.getString("batch_id");
		aBatch.setId(Integer.parseInt(id));
		
		String timeSlot = aRow.getString("timeSlot");
		aBatch.setTimeslot(Integer.parseInt(timeSlot));

		String morning = aRow.getString("morning");
		aBatch.setMorning(Boolean.parseBoolean(morning));
		
		// Return the object created in this method
		return aBatch;
	}
	
	@Override
	public Batch getBatchById(int id) {
		Batch aBatch = null;
		
		String sqlString = " select * from batch"
	              + " where id = ?";
	
		SqlRowSet theBatchFromDatabase = batchDB.queryForRowSet(sqlString, id);
		
		if(theBatchFromDatabase.next()) {   // if the Batch was found, position the result at the first row
			aBatch = batchHelper(theBatchFromDatabase); // Send the result to conversion method
		}

		return aBatch;
	}

	@Override
	public void addBatch(Batch aBatch) throws DataBaseInsertException {
		// Define a String to hold the SQL to be sent to the database manager
		String sqlString = " insert into batch " 
				              +" (batch_id, timeSlot, morning) "
				              +" values(?, ?, ?)";		
				
		// Send the SQL String to the database manager with data from batch object received
		int numberRowsAdded = batchDB.update(sqlString,
												aBatch.getId(),
												aBatch.getTimeslot(),
												aBatch.isMorning()
											);
		
		// Check to see if a row was added to the data base
		if(numberRowsAdded != 1) {
			throw new DataBaseInsertException("Attempt to add Batch \"" + aBatch.getId() + "\"");
		}
		// return to caller
		return;
	}

	@Override
	public boolean updateBatch(Batch aBatch) throws DataBaseUpdateException {
		// Define String to hold SQL statement to be sent to database manager
		//      with "?" where values in program variables should be used
		//       and where clause to be sure only specific batch is updated
		// Note: Primary Key values should NEVER be updated (per the Relational Data Model)
		String updateSQL = " update batch "
				          +"    set timeSlot  = ? "
				          +",       morning   = ? "
				          +" where  batch_id  = ?";
		
		// Send the SQL String to the database manage and receive number of rows affected
		int numberRowsUpdated = batchDB.update(updateSQL
				                                  ,aBatch.getTimeslot()
				                                  ,aBatch.isMorning()
				                                  ,aBatch.getId()
				                                  );
		// Check to be sure only one row was updated		
		if(numberRowsUpdated == 1) {
			return true;
		}
		else {
			throw new DataBaseUpdateException("Attempt to update Batchs \"" + aBatch.getId() + "\"");
		}
	}

	@Override
	public void deleteBatch(int id) throws DataBaseDeleteException {
		// Define String to hold SQL statement to send to database manager
		String updateSQL = "delete from batch where id = ?";
		
		// Send SQL string to data base manager and receive how many rows were affected
		int numberRowsDeleted = batchDB.update(updateSQL, id);
		
		// Check to be sure only one row was deleted
		if(numberRowsDeleted != 1) {
			throw new DataBaseDeleteException("Attempt to delete Batch " + id + "\"");
		}

	}

}
