package batch;

import java.util.Objects;

public class Batch {
	private int id;
	private int timeslot;
	private boolean morning;
	
	public Batch() {}
	
	public Batch(int id, int timeslot, boolean morning) {
		super();
		this.id = id;
		this.timeslot = timeslot;
		this.morning = morning;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(int timeslot) {
		this.timeslot = timeslot;
	}

	public boolean isMorning() {
		return morning;
	}

	public void setMorning(boolean morning) {
		this.morning = morning;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, morning, timeslot);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Batch other = (Batch) obj;
		return id == other.id && morning == other.morning && timeslot == other.timeslot;
	}

	@Override
	public String toString() {
		return "batch [id=" + id + ", timeslot=" + timeslot + ", morning=" + morning + "]";
	}
	
}
