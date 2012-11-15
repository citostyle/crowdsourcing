package tuwien.aic.crowdsourcing.mobileworks.task;

public class TaskResult {
	
	private Integer id;
	private String answer;
	private String status;
	private String instructions;
	private Integer redundancy;
	private String workflow;
	private String resource;
	private String resourcetype;
	private String resource_url;
	private String timeCreated;
	private String timeFinished;
	private Integer priority;
	private String taskid;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getInstructions() {
		return instructions;
	}
	
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	public Integer getRedundancy() {
		return redundancy;
	}

	public void setRedundancy(Integer redundancy) {
		this.redundancy = redundancy;
	}

	public String getWorkflow() {
		return workflow;
	}
	
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public String getResourcetype() {
		return resourcetype;
	}
	
	public void setResourcetype(String resourcetype) {
		this.resourcetype = resourcetype;
	}
	
	public String getResource_url() {
		return resource_url;
	}
	
	public void setResource_url(String resource_url) {
		this.resource_url = resource_url;
	}
	
	public String getTimeCreated() {
		return timeCreated;
	}
	
	public void setTimeCreated(String timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	public String getTimeFinished() {
		return timeFinished;
	}
	
	public void setTimeFinished(String timeFinished) {
		this.timeFinished = timeFinished;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TaskResult [answer=" + answer + ", status=" + status
				+ ", instructions=" + instructions + ", workflow=" + workflow
				+ ", resource=" + resource + ", resourcetype=" + resourcetype
				+ ", resource_url=" + resource_url + ", timeCreated="
				+ timeCreated + ", timeFinished=" + timeFinished + "]";
	}
}
