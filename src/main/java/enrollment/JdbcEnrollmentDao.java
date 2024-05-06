package enrollment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dbConn.DbConn;
import exception.DataBaseDeleteException;
import exception.DataBaseInsertException;
import exception.DataBaseUpdateException;

public class JdbcEnrollmentDao implements EnrollmentDao {

	// Instantiate a JdbcTemplate object and assign to reference in the constructor
	public JdbcEnrollmentDao() {}
	
	@Override
	public List<Enrollment> getAllEnrollment() {
		ResultSet results;
		
		// Define a String with SQL to be run
		String sqlString = "select * from enrollment"; // Note ; not required in at then end of SQL string

		List<Enrollment> enrollmentList = new ArrayList<Enrollment>();
		
		try {

			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(sqlString);
			
			results = preparedStatement.executeQuery();
			
			while(results.next()) {
				enrollmentList.add(enrollmentHelper(results));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return enrollmentList;

	}

	// Convert a row in the results from a Select into a Enrollment object
	// It's private to prevent others outside this class from being able to use
	// Since its requires an object (SqlRowSet) created in this class it doesn't
	//       make sense to allow other outside the class to use
	
	private Enrollment enrollmentHelper(ResultSet aRow) {
		// Define an object to be returned
		Enrollment aEnrollment = new Enrollment(); // A Enrollment object initialized to default values
		
		try {
			// Use the setters for the POJO to set the values from each column the SqlRowSet	
			int id = aRow.getInt("id");
			aEnrollment.setId(id);
			
			int participantId = aRow.getInt("participant_id");
			aEnrollment.setParticipantId(participantId);

			int batchId = aRow.getInt("batch_id");
			aEnrollment.setBatchId(batchId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Return the object created in this method
		return aEnrollment;
	}
	
	@Override
	public Enrollment getEnrollmentById(int id) {
		ResultSet results;

		Enrollment aEnrollment = null;
		
		String sqlString = " select * from enrollment"
	              + " where id = ?";
	
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(sqlString);
			
			preparedStatement.setInt (1, id);
			
			results = preparedStatement.executeQuery();
			
			if(results.next()) {   // if the Participant was found, position the result at the first row
				aEnrollment = enrollmentHelper(results); // Send the result to conversion method
			}
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return aEnrollment;

	}

	@Override
	public void addEnrollment(Enrollment aEnrollment) throws DataBaseInsertException {

		// Define a String to hold the SQL to be sent to the database manager
		String sqlString = " insert into enrollment " 
				              +" values(null, ?, ?)";			
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(sqlString);
			
			preparedStatement.setInt( 1, aEnrollment.getParticipantId());
			preparedStatement.setInt( 2, aEnrollment.getBatchId());
			
			int numberRowsAdded = preparedStatement.executeUpdate();
			
			if(numberRowsAdded != 1) {
				throw new DataBaseInsertException("Attempt to add Enrollment \"" + aEnrollment.getId() + "\"");
			}
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return;
	}

	@Override
	public boolean updateEnrollment(Enrollment aEnrollment) throws DataBaseUpdateException {
		boolean updateWorked = false;
		
		// Define String to hold SQL statement to be sent to database manager
		//      with "?" where values in program variables should be used
		//       and where clause to be sure only specific enrollment is updated
		// Note: Primary Key values should NEVER be updated (per the Relational Data Model)
		String updateSQL = " update enrollment "
				          +"    set batch_id   = ? "
				          +" where  participant_id  = ?";
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(updateSQL);
			
			preparedStatement.setInt( 1, aEnrollment.getBatchId());
			preparedStatement.setInt( 2, aEnrollment.getParticipantId());
			
			int numberRowsUpdated = preparedStatement.executeUpdate();

			// Check to be sure only one row was updated		
			if(numberRowsUpdated == 1) {
				updateWorked = true;
			}
			else 
			{
				throw new DataBaseUpdateException("Attempt to update Enrollment \"" + aEnrollment.getId() + "\"");
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return updateWorked;
	}

	@Override
	public int deleteEnrollment(int id) throws DataBaseDeleteException {
		// Define String to hold SQL statement to send to database manager
		String updateSQL = "delete from enrollment where id = ?";
		
		// Send SQL string to data base manager and receive how many rows were affected
		int numberRowsDeleted = 0;
		
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(updateSQL);
			
			preparedStatement.setInt( 1, id);

			numberRowsDeleted = preparedStatement.executeUpdate();

			// Check to be sure only one row was deleted
			if(numberRowsDeleted != 1) {
				throw new DataBaseDeleteException("Attempt to delete Enrollment " + id + "\"");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return numberRowsDeleted;
	}
}
