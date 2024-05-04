package participant;

import java.util.List;

import exception.DataBaseDeleteException;
import exception.DataBaseInsertException;
import exception.DataBaseUpdateException;

public interface ParticipantDao {

	// Get all Participants
	public List<Participant> getAllParticipant();
	
	// Get a specific Participant by name
	public Participant getParticipantByName(String name);
	
	// Add a new Participant
	public void addParticipant(Participant participant) throws DataBaseInsertException;
	
	// Update a specific Participant
	public boolean updateParticipant(Participant aParticipant) throws DataBaseUpdateException;
	
	// Delete a specific Participant
	public void deleteParticipant(String name) throws DataBaseDeleteException;
}
