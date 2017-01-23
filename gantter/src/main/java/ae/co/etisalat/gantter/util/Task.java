package ae.co.etisalat.gantter.util;

public class Task {
	private String id;
	private String title;
	private String raisedBy;
	private String hspmCRNumber;
	private String hspmCRTaskNumber;
	
	public Task(String id, String title, String raisedBy, String hspmCRNumber, String hspmCRTaskNumber) {
		this.id = id;
		this.title = title;
		this.raisedBy = raisedBy;
		this.hspmCRNumber = hspmCRNumber;
		this.hspmCRTaskNumber = hspmCRTaskNumber;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRaisedBy() {
		return raisedBy;
	}
	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	public String getHspmCRNumber() {
		return hspmCRNumber;
	}

	public void setHspmCRNumber(String hspmCRNumber) {
		this.hspmCRNumber = hspmCRNumber;
	}

	public String getHspmCRTaskNumber() {
		return hspmCRTaskNumber;
	}

	public void setHspmCRTaskNumber(String hspmCRTaskNumber) {
		this.hspmCRTaskNumber = hspmCRTaskNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Task [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", raisedBy=");
		builder.append(raisedBy);
		builder.append(", hspmCRNumber=");
		builder.append(hspmCRNumber);
		builder.append(", hspmCRTaskNumber=");
		builder.append(hspmCRTaskNumber);
		builder.append("]");
		return builder.toString();
	}
	
}
