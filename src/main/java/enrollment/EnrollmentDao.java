package enrollment;

import java.util.List;

import exception.DataBaseDeleteException;
import exception.DataBaseInsertException;
import exception.DataBaseUpdateException;

public interface EnrollmentDao {
	// Get all Enrollment
	public List<Enrollment> getAllEnrollment();
	
	// Get a specific Enrollment by name
	public Enrollment getEnrollmentById(int id);
	
	// Add a new Enrollment
	public void addEnrollment(Enrollment aEnrollment) throws DataBaseInsertException;
	
	// Update a specific Enrollment
	public boolean updateEnrollment(Enrollment aEnrollment) throws DataBaseUpdateException;
	
	// Delete a specific Enrollment
	public int deleteEnrollment(int id) throws DataBaseDeleteException;
}
