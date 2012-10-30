package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

public interface SentimentManager {

    void addCompanySentiment(String taskId, String workerId,
            String companyName, Integer result);

    void addProductSentiment(String taskId, String workerId,
            String productName, Integer result);

    void addCorrelation(String taskId, String workerId, String companyName,
            String productName);

    double getCompanySentiment(String companyName);

    double getProductSentiment(String productName);

    List<String> getProductNames(String companyName);
}
