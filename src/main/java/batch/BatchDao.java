package batch;

import java.util.List;

import exception.DataBaseDeleteException;
import exception.DataBaseInsertException;
import exception.DataBaseUpdateException;

public interface BatchDao {
	// Get all Batches
	public List<Batch> getAllBatch();
	
	// Get a specific Batch by name
	public Batch getBatchById(int id);
	
	// Add a new Batch
	public void addBatch(Batch Batch) throws DataBaseInsertException;
	
	// Update a specific Batch
	public boolean updateBatch(Batch aBatch) throws DataBaseUpdateException;
	
	// Delete a specific Batch
	public int deleteBatch(int id) throws DataBaseDeleteException;
}
