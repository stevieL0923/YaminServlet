package participant;

import java.util.Objects;

public class Participant {

	private int participantId = 0;
	private String fullName = null;
	private String email = null;
	private String phone = null;
	private boolean morningSession = false;


	public Participant() {}


	public Participant(String fullName, String email, String phone, boolean morningSession) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.morningSession = morningSession;
	}

	public int getParticipantId() {
		return participantId;
	}


	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}


	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public boolean isMorningSession() {
		return morningSession;
	}

	public void setMorningSession(boolean morningSession) {
		this.morningSession = morningSession;
	}


	@Override
	public int hashCode() {
		return Objects.hash(email, fullName, morningSession, phone);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participant other = (Participant) obj;
		return Objects.equals(email, other.email) && Objects.equals(fullName, other.fullName)
				&& morningSession == other.morningSession
				&& Objects.equals(phone, other.phone);
	}


	@Override
	public String toString() {
		return "Participant [fullName=" + fullName + ", email=" + email + ", phone="
				+ phone + ", morningSession=" + morningSession + "]";
	}
	
	
}
