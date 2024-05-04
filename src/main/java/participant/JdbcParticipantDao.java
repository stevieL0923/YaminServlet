package participant;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

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
	
	// Define a reference to a JdbcTemplateObject we can use to access the tables in the database
	private JdbcTemplate participantDB;

	// Instantiate a JdbcTemplate object and assign to reference in the constructor
	public JdbcParticipantDao(DataSource aDataSource) {
		participantDB = new JdbcTemplate(aDataSource);
	}


	@Override
	public List<Participant> getAllParticipant() {
		// Define a String with SQL to be run
		String allParticipants = "select * from participant"; // Note ; not required in at then end of SQL string
		
		// Send the SQL String to the database manage and store result in an SqlRowSet
		SqlRowSet rowSet = participantDB.queryForRowSet(allParticipants);
		
		// Define the object containing all participants
		List<Participant> participantList = new ArrayList<Participant>();
		
		// "Parse the result" - convert the rows from select into participant objects object
		//                      using a method to retrieve each row, create a participant
		//                         then add the participant object to the list
		
		// Loop through the SqlRowset converting the rows in it, one at a time
		while(rowSet.next()) { // Position at the next row (if there is one)
			participantList.add(participantHelper(rowSet)); // Convert current row to an object
		}
		
		return participantList;  // Return the object containing all the participants
	}

	// Convert a row in the results from a Select into a Participant object
	// It's private to prevent others outside this class from being able to use
	// Since its requires an object (SqlRowSet) created in this class it doesn't
	//       make sense to allow other outside the class to use
	
	private Participant participantHelper(SqlRowSet aRow) {
	// Define an object to be returned
		Participant aParticipant = new Participant(); // A Participant object initialized to default values
		
	 // Use the setters for the POJO to set the values from each column the SqlRowSet
		String fullName = aRow.getString("full_name");
		aParticipant.setFullName(fullName);
		
		String email = aRow.getString("email");
		aParticipant.setEmail(email);

		String phone = aRow.getString("phone");
		aParticipant.setEmail(phone);
		
		boolean morning = aRow.getBoolean("morningSession");
		aParticipant.setMorningSession(morning);
		
		// Return the object created in this method
		return aParticipant;
	}
	@Override
	public Participant getParticipantByName(String name) {
		Participant aParticipant = null;
		
		String sqlString = " select * from participant"
	              + " where full_name = ?";
	
		SqlRowSet theParticipantFromDatabase = participantDB.queryForRowSet(sqlString, name);
		
		if(theParticipantFromDatabase.next()) {   // if the Participant was found, position the result at the first row
			aParticipant = participantHelper(theParticipantFromDatabase); // Send the result to conversion method
		}

		return aParticipant;
	}

	@Override
	public void addParticipant(Participant participant) throws DataBaseInsertException {
		// Define a String to hold the SQL to be sent to the database manager
		String sqlString = " insert into participant " 
				              +" (full_name, email, phone, morningSession) "
				              +" values(?, ?, ?, ?)";		
				
		// Send the SQL String to the database manager with data from participant object received
		int numberRowsAdded = participantDB.update(sqlString,
												participant.getFullName(),
												participant.getEmail(),
												participant.getPhone(),
												participant.isMorningSession()
											);
		
		// Check to see if a row was added to the data base
		if(numberRowsAdded != 1) {
			throw new DataBaseInsertException("Attempt to add Participant \"" + participant.getFullName() + "\"");
		}
		// return to caller
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
		
		// Send the SQL String to the database manage and receive number of rows affected
		int numberRowsUpdated = participantDB.update(updateSQL
				                                  ,aParticipant.getEmail()
				                                  ,aParticipant.getPhone()
				                                  ,aParticipant.isMorningSession()
				                                  ,aParticipant.getFullName()
				                                  );
		// Check to be sure only one row was updated		
		if(numberRowsUpdated == 1) {
			return true;
		}
		else {
			throw new DataBaseUpdateException("Attempt to update Participants \"" + aParticipant.getFullName() + "\"");
		}
	}

	@Override
	public void deleteParticipant(String name) throws DataBaseDeleteException {
		// Define String to hold SQL statement to send to database manager
		String updateSQL = "delete from participant where full_name = ?";
		
		// Send SQL string to data base manager and receive how many rows were affected
		int numberRowsDeleted = participantDB.update(updateSQL, name);
		
		// Check to be sure only one row was deleted
		if(numberRowsDeleted != 1) {
			throw new DataBaseDeleteException("Attempt to delete Participant " + name + "\"");
		}
		
	}

}
