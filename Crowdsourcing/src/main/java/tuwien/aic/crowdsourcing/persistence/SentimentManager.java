package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import tuwien.aic.crowdsourcing.persistence.entities.MWTask;

public interface SentimentManager {

    void addCompanySentiment(String taskId, String workerId,
            String companyName, Integer result);

    void addProductSentiment(String taskId, String workerId,
            String productName, Integer result);

    void addCorrelation(String taskId, String workerId, String companyName,
            String productName);

    Double getCompanySentiment(String companyName);

    Double getProductSentiment(String productName);

    List<MWTask> getActiveTasks();

    List<String> getProductNames(String companyName);
}
