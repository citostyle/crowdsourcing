package tuwien.aic.crowdsourcing.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRecognition;
import tuwien.aic.crowdsourcing.persistence.entities.Correlation;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRecognition;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;

@Service
public class TaskManagerImpl implements TaskManager {

    @PersistenceContext
    private EntityManager entityManager = null;

    public TaskManagerImpl() {

    }

    @Override
    public int getResponseCount(String taskId) {
        int ret = 0;

        ret += getCompanyRecognitionCount(taskId);
        ret += getProductRecognitionCount(taskId);

        ret += getCompanyRatingCount(taskId);
        ret += getProductRatingCount(taskId);

        ret += getCorrelationCount(taskId);

        return ret;
    }

    @Override
    public void setTaskState(String taskId, TaskState state) {

        MWTask task = getTaskByTaskId(taskId);

        if (task != null) {
            task.setTaskState(state);

            entityManager.merge(task);
        } else {
            throw new IllegalArgumentException(
                    "The provided task does not exist!");
        }
    }

    @SuppressWarnings("unchecked")
    private MWTask getTaskByTaskId(String taskId) {
        MWTask ret = null;

        List<MWTask> tasks = entityManager
                .createQuery("SELECT t FROM MWTask t WHERE t.taskId = :taskId")
                .setParameter("taskId", taskId).getResultList();

        if (!tasks.isEmpty()) {
            ret = tasks.get(0);
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    private int getCompanyRatingCount(String taskId) {

        List<CompanyRating> ratings = entityManager
                .createQuery(
                        "SELECT r FROM CompanyRating r WHERE r.task.id = :taskId")
                .setParameter("taskId", taskId).getResultList();

        return ratings.size();
    }

    @SuppressWarnings("unchecked")
    private int getProductRatingCount(String taskId) {

        List<ProductRating> ratings = entityManager
                .createQuery(
                        "SELECT r FROM ProductRating r WHERE r.task.id = :taskId")
                .setParameter("taskId", taskId).getResultList();

        return ratings.size();
    }

    @SuppressWarnings("unchecked")
    private int getCompanyRecognitionCount(String taskId) {

        List<CompanyRecognition> ratings = entityManager
                .createQuery(
                        "SELECT r FROM CompanyRecognition r WHERE r.task.id = :taskId")
                .setParameter("taskId", taskId).getResultList();

        return ratings.size();
    }

    @SuppressWarnings("unchecked")
    private int getProductRecognitionCount(String taskId) {

        List<ProductRecognition> ratings = entityManager
                .createQuery(
                        "SELECT r FROM ProductRecognition r WHERE r.task.id = :taskId")
                .setParameter("taskId", taskId).getResultList();

        return ratings.size();
    }

    @SuppressWarnings("unchecked")
    private int getCorrelationCount(String taskId) {

        List<Correlation> correlations = entityManager
                .createQuery(
                        "SELECT c FROM Correlation c WHERE c.task.id = :taskId")
                .setParameter("taskId", taskId).getResultList();

        return correlations.size();
    }
}
