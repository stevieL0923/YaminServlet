package participant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dbConn.DbConn;
import exception.DataBaseDeleteException;
import exception.DataBaseInsertException;
import exception.DataBaseUpdateException;

//create table participant
//(id                   smallint            not null  auto_increment,
// full_name            varchar(32)         not null,
// email                varchar(32)         not null,
// phone                varchar(32)         not null,
// morningSession       boolean,
// primary key(id));


public class JdbcParticipantDao implements ParticipantDao {
	
	// Instantiate a JdbcTemplate object and assign to reference in the constructor
	public JdbcParticipantDao() { }

	@Override
	public List<Participant> getAllParticipant() {
		ResultSet results;
		
		// Define a String with SQL to be run
		String allParticipants = "select * from participant"; // Note ; not required in at then end of SQL string

		List<Participant> participantList = new ArrayList<Participant>();
		
		try {

			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(allParticipants);
			
			results = preparedStatement.executeQuery();
			
			while(results.next()) {
				participantList.add(participantHelper(results));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return participantList;
	}

	// Convert a row in the results from a Select into a Participant object
	// It's private to prevent others outside this class from being able to use
	// Since its requires an object (SqlRowSet) created in this class it doesn't
	//       make sense to allow other outside the class to use
	
	private Participant participantHelper(ResultSet aRow) {
		// Define an object to be returned
		Participant aParticipant = new Participant(); // A Participant object initialized to default values
		
		try {			
			// Use the setters for the POJO to set the values from each column the SqlRowSet
			int participantId = aRow.getInt("participant_id");
			aParticipant.setParticipantId(participantId);
	
			String fullName = aRow.getString("full_name");
			aParticipant.setFullName(fullName);
			
			String email = aRow.getString("email");
			aParticipant.setEmail(email);
	
			String phone = aRow.getString("phone");
			aParticipant.setPhone(phone);
			
			boolean morning = aRow.getBoolean("morningSession");
			aParticipant.setMorningSession(morning);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Return the object created in this method
		return aParticipant;
	}
	@Override
	public Participant getParticipantByName(String name) {

		ResultSet results;

		Participant aParticipant = null;
		
		String sqlString = " select * from participant"
	              + " where full_name = ?";
	
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(sqlString);
			
			preparedStatement.setString(1, name);
			
			results = preparedStatement.executeQuery();
			
			if(results.next()) {   // if the Participant was found, position the result at the first row
				aParticipant = participantHelper(results); // Send the result to conversion method
			}
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return aParticipant;
	}

	@Override
	public Participant getParticipantById(int id) {
		ResultSet results;

		Participant aParticipant = null;
		
		String sqlString = " select * from participant"
	              + " where participant_id = ?";
	
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(sqlString);
			
			preparedStatement.setInt (1, id);
			
			results = preparedStatement.executeQuery();
			
			if(results.next()) {   // if the Participant was found, position the result at the first row
				aParticipant = participantHelper(results); // Send the result to conversion method
			}
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return aParticipant;
	}
	
	@Override
	public void addParticipant(Participant aParticipant) throws DataBaseInsertException {

		// Define a String to hold the SQL to be sent to the database manager
		String sqlString = " insert into participant " 
				              +" values(null, ?, ?, ?, ?)";
		
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(sqlString);
			
			preparedStatement.setString( 1, aParticipant.getFullName());
			preparedStatement.setString( 2, aParticipant.getEmail());
			preparedStatement.setString( 3, aParticipant.getPhone());
			preparedStatement.setBoolean(4, aParticipant.isMorningSession());
			
			int numberRowsAdded = preparedStatement.executeUpdate();
			
			if(numberRowsAdded != 1) {
				throw new DataBaseInsertException("Attempt to add Participant \"" + aParticipant.getFullName() + "\"");
			}
	
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return;		
	}

	@Override
	public boolean updateParticipant(Participant aParticipant) throws DataBaseUpdateException {

		// Define return value variable
		boolean updateWorked = false;
		
		// Define String to hold SQL statement to be sent to database manager
		//      with "?" where values in program variables should be used
		//       and where clause to be sure only specific participant is updated
		// Note: Primary Key values should NEVER be updated (per the Relational Data Model)
		String updateSQL = " update participant "
				          +"    set email          = ? "
				          +",       phone          = ? "
				          +",       morningSession = ? "
				          +" where  full_name = ?";
		
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(updateSQL);
			
			preparedStatement.setString( 1, aParticipant.getEmail());
			preparedStatement.setString( 2, aParticipant.getPhone());
			preparedStatement.setBoolean(3, aParticipant.isMorningSession());
			preparedStatement.setString( 4, aParticipant.getFullName());
			
			int numberRowsUpdated = preparedStatement.executeUpdate();

			// Check to be sure only one row was updated		
			if(numberRowsUpdated == 1) {
				updateWorked = true;
			}
			else 
			{
				throw new DataBaseUpdateException("Attempt to update Participants \"" + aParticipant.getFullName() + "\"");
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return updateWorked;
	}

	@Override
	public int deleteParticipant(String name) throws DataBaseDeleteException {
		int numberRowsDeleted = 0;
		
		// Define String to hold SQL statement to send to database manager
		String updateSQL = "delete from participant where full_name = ?";
		
		try {
			PreparedStatement preparedStatement = DbConn.getConn().prepareStatement(updateSQL);
			
			preparedStatement.setString( 1, name);

			numberRowsDeleted = preparedStatement.executeUpdate();

			// Check to be sure only one row was deleted
			if(numberRowsDeleted != 1) {
				throw new DataBaseDeleteException("Attempt to delete Participant " + name + "\"");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return numberRowsDeleted;
	}
	
	public int deleteParticipant(int id) throws DataBaseDeleteException {
		int numberRowsDeleted = 0;

		// Define String to hold SQL statement to send to database manager
		String updateSQL = "delete from participant where participant_id = ?";
		
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
