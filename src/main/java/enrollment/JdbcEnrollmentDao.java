package enrollment;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import exception.DataBaseDeleteException;
import exception.DataBaseInsertException;
import exception.DataBaseUpdateException;

public class JdbcEnrollmentDao implements EnrollmentDao {

	// Define a reference to a JdbcTemplateObject we can use to access the tables in the database
	private JdbcTemplate enrollmentDB;

	// Instantiate a JdbcTemplate object and assign to reference in the constructor
	public JdbcEnrollmentDao(DataSource aDataSource) {
		enrollmentDB = new JdbcTemplate(aDataSource);
	}
	
	@Override
	public List<Enrollment> getAllEnrollment() {
		// Define a String with SQL to be run
		String allEnrollmentes = "select * from enrollment"; // Note ; not required in at then end of SQL string
		
		// Send the SQL String to the database manage and store result in an SqlRowSet
		SqlRowSet rowSet = enrollmentDB.queryForRowSet(allEnrollmentes);
		
		// Define the object containing all enrollments
		List<Enrollment> enrollmentList = new ArrayList<Enrollment>();
		
		// "Parse the result" - convert the rows from select into enrollment objects object
		//                      using a method to retrieve each row, create a enrollment
		//                         then add the enrollment object to the list
		
		// Loop through the SqlRowset converting the rows in it, one at a time
		while(rowSet.next()) { // Position at the next row (if there is one)
			enrollmentList.add(enrollmentHelper(rowSet)); // Convert current row to an object
		}
		
		return enrollmentList;  // Return the object containing all the enrollments
	}

	// Convert a row in the results from a Select into a Enrollment object
	// It's private to prevent others outside this class from being able to use
	// Since its requires an object (SqlRowSet) created in this class it doesn't
	//       make sense to allow other outside the class to use
	
	private Enrollment enrollmentHelper(SqlRowSet aRow) {
	// Define an object to be returned
		Enrollment aEnrollment = new Enrollment(); // A Enrollment object initialized to default values
		
	 // Use the setters for the POJO to set the values from each column the SqlRowSet		
		String participantId = aRow.getString("participant_id");
		aEnrollment.setParticipantId(Integer.parseInt(participantId));

		String batchId = aRow.getString("batch_id");
		aEnrollment.setBatchId(Integer.parseInt(batchId));
		
		// Return the object created in this method
		return aEnrollment;
	}
	
	@Override
	public Enrollment getEnrollmentById(int id) {
		Enrollment aEnrollment = null;
		
		String sqlString = " select * from enrollment"
	              + " where id = ?";
	
		SqlRowSet theEnrollmentFromDatabase = enrollmentDB.queryForRowSet(sqlString, id);
		
		if(theEnrollmentFromDatabase.next()) {   // if the Enrollment was found, position the result at the first row
			aEnrollment = enrollmentHelper(theEnrollmentFromDatabase); // Send the result to conversion method
		}

		return aEnrollment;
	}

	@Override
	public void addEnrollment(Enrollment aEnrollment) throws DataBaseInsertException {
		// Define a String to hold the SQL to be sent to the database manager
		String sqlString = " insert into enrollment " 
				              +" (participant_id, batch_id) "
				              +" values(?, ?)";		
				
		// Send the SQL String to the database manager with data from enrollment object received
		int numberRowsAdded = enrollmentDB.update(sqlString,
												aEnrollment.getParticipantId(),
												aEnrollment.getBatchId()
											);
		
		// Check to see if a row was added to the data base
		if(numberRowsAdded != 1) {
			throw new DataBaseInsertException("Attempt to add Enrollment for\"" + aEnrollment.getParticipantId() + "\"");
		}
		// return to caller
		return;
		
	}

	@Override
	public boolean updateEnrollment(Enrollment aEnrollment) throws DataBaseUpdateException {
		// Define String to hold SQL statement to be sent to database manager
		//      with "?" where values in program variables should be used
		//       and where clause to be sure only specific enrollment is updated
		// Note: Primary Key values should NEVER be updated (per the Relational Data Model)
		String updateSQL = " update enrollment "
				          +"    set batch_id   = ? "
				          +" where  participant_id  = ?";
		
		// Send the SQL String to the database manage and receive number of rows affected
		int numberRowsUpdated = enrollmentDB.update(updateSQL
				                                  ,aEnrollment.getBatchId()
				                                  ,aEnrollment.getParticipantId()
				                                  );
		// Check to be sure only one row was updated		
		if(numberRowsUpdated == 1) {
			return true;
		}
		else {
			throw new DataBaseUpdateException("Attempt to update Enrollments \"" + aEnrollment.getParticipantId() + "\"");
		}
	}

	@Override
	public void deleteEnrollment(int id) throws DataBaseDeleteException {
		// Define String to hold SQL statement to send to database manager
		String updateSQL = "delete from enrollment where id = ?";
		
		// Send SQL string to data base manager and receive how many rows were affected
		int numberRowsDeleted = enrollmentDB.update(updateSQL, id);
		
		// Check to be sure only one row was deleted
		if(numberRowsDeleted != 1) {
			throw new DataBaseDeleteException("Attempt to delete Enrollment " + id + "\"");
		}
	}

}
