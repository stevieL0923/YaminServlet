package enrollment;

import java.util.Objects;

public class Enrollment {
	private int id;
	private int participantId;
	private int batchId;
	
	Enrollment() {}

	public Enrollment(int participantId, int batchId) {
		super();
		this.participantId = participantId;
		this.batchId = batchId;
	}

	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(batchId, participantId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Enrollment other = (Enrollment) obj;
		return batchId == other.batchId && participantId == other.participantId;
	}

	@Override
	public String toString() {
		return "Enrollment [participantId=" + participantId + ", batchId=" + batchId + "]";
	}
}
