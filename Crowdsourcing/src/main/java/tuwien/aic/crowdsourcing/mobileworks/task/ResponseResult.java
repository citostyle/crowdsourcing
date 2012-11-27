
package tuwien.aic.crowdsourcing.mobileworks.task;

import java.util.List;

public class ResponseResult {
    private String taskid;
    private String status;
    private List<WorkerResult> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<WorkerResult> getResults() {
        return results;
    }

    public void setResults(List<WorkerResult> results) {
        this.results = results;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Override
    public String toString() {
        return "ResponseResult [taskid=" + taskid + ", status=" + status
                + ", results=" + results + "]";
    }
}
