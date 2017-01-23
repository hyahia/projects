package ae.co.etisalat.gantter.util;

public class Defect {
	private String id;
	private String projectId;
	private String title;
	private String status;
	private String environment;
	
	public Defect(String id, String title, String status, String environment) {
		this.id = id;
		this.title = title;
		this.status = status;
		this.environment = environment;
	}
	
	public Defect(String id, String title, String status, String environment, String projectId) {
		this.id = id;
		this.title = title;
		this.status = status;
		this.environment = environment;
		this.projectId = projectId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Defect [id=");
		builder.append(id);
		builder.append(", projectId=");
		builder.append(projectId);
		builder.append(", title=");
		builder.append(title);
		builder.append(", status=");
		builder.append(status);
		builder.append(", environment=");
		builder.append(environment);
		builder.append("]");
		return builder.toString();
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
