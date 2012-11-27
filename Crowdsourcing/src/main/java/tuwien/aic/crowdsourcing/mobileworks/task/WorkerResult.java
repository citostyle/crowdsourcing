
package tuwien.aic.crowdsourcing.mobileworks.task;

import java.util.List;
import java.util.Map;

public class WorkerResult {

    private String workerId;
    private String timestamp;
    private Location location;
    private List<Map<String, String>> answers;
    private Integer timeTaken;

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Map<String, String>> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Map<String, String>> answers) {
        this.answers = answers;
    }

    public Integer getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Integer timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        return "WorkerResult [workerId=" + workerId + ", timestamp="
                + timestamp + ", location=" + location + ", answers=" + answers
                + ", timeTaken=" + timeTaken + "]";
    }
}
