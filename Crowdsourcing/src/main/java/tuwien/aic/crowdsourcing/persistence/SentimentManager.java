package tuwien.aic.crowdsourcing.persistence;

public interface SentimentManager {

    void addCompanySentiment(String taskId, String workerId,
                             String companyName, Integer result);

    void addProductSentiment(String taskId, 
                             String workerId,
                             String companyName,
                             String productName, Integer result);

    double getCompanySentiment(String companyName);

    double getProductSentiment(String companyName,
                               String productName);
}
