package tuwien.aic.crowdsourcing.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tuwien.aic.crowdsourcing.persistence.entities.*;

@Repository
@Transactional
public class SentimentManagerImpl implements SentimentManager {

    @PersistenceContext
    private EntityManager entityManager;

    public SentimentManagerImpl() {

    }

    @Override
    public void addCompanySentiment(String taskId, String workerId,
                                    String companyName, Integer result) {

        TaskManager taskManager = 
            new TaskManagerImpl();
        
        CompanyManager companyManager = 
            new CompanyManagerImpl();
        
        MWTask task = 
            taskManager.getTaskByTaskId(taskId);

        Worker worker = getWorkerByWorkerId(workerId);

        Company company = 
            companyManager.getCompanyByName(companyName);

        if (task == null) {
            throw new IllegalArgumentException
                ("The provided task does not exist!");
        }

        if (worker == null) {
            worker = new Worker(workerId);

            entityManager.persist(worker);

            entityManager.refresh(worker);
        }

        if (company == null) {
            company = new Company(companyName);

            entityManager.persist(company);

            entityManager.refresh(company);
        }

        CompanyRating rating = getCompanyRating(task, worker, company);

        if (rating == null) {
            rating = new CompanyRating(task, worker, company, result);

            entityManager.persist(rating);
        } else {
            rating.setRatingValue(result);

            entityManager.merge(rating);
        }
    }

    @Override
    public void addProductSentiment(String taskId, 
                                    String workerId,
                                    String companyName, 
                                    String productName, Integer result) {

        TaskManager taskManager = 
            new TaskManagerImpl();
        
        CompanyManager companyManager = 
            new CompanyManagerImpl();
        
        ProductManager productManager = 
            new ProductManagerImpl();
        
        MWTask task = 
            taskManager.getTaskByTaskId(taskId);

        Worker worker = getWorkerByWorkerId(workerId);

        Company company = 
            companyManager.getCompanyByName(companyName);

        Product product = 
            productManager.getProductByName(companyName,
                                            productName);

        if (task == null) {
            throw new IllegalArgumentException(
                    "The provided task does not exist!");
        }

        if (worker == null) {
            worker = new Worker(workerId);

            entityManager.persist(worker);

            entityManager.refresh(worker);
        }

        if (company == null) {
            company = new Company(companyName);

            entityManager.persist(company);

            entityManager.refresh(company);
        }

        if (product == null) {
            product = new Product(company,
                                  productName);

            entityManager.persist(product);

            entityManager.refresh(product);
        }

        ProductRating rating = getProductRating(task, worker, product);

        if (rating == null) {
            rating = new ProductRating(task, worker, product, result);

            entityManager.persist(rating);
        } 
        else {
            rating.setRatingValue(result);

            entityManager.merge(rating);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public double getCompanySentiment(String companyName) {

        double ret = 0;

        long count = 0;
        long errors = 0;
        long totalSum = 0;

        List<CompanyRating> ratings = entityManager
                .createQuery(
                        "SELECT r FROM CompanyRating r " +
                        "WHERE r.company.name = :companyName")
                .setParameter("companyName", companyName)
                .getResultList();

        for (CompanyRating rating : ratings) {
            if (rating.getRatingValue() != null) {
                totalSum += rating.getRatingValue();
            } 
            else {
                errors++;
            }

            count++;
        }

        if ((count - errors) > 0) {
            ret = (double) totalSum / (double) (count - errors);
        }

        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public double getProductSentiment(String companyName,
                                      String productName) {

        double ret = 0;

        long count = 0;
        long errors = 0;
        long totalSum = 0;

        List<ProductRating> ratings = entityManager
                .createQuery(
                        "SELECT r FROM ProductRating r " +
                        "WHERE r.product.name = :productName AND " +
                        "      r.product.company.name = :companyName")
                .setParameter("companyName", companyName)
                .setParameter("productName", productName)
                .getResultList();

        for (ProductRating rating : ratings) {
            if (rating.getRatingValue() != null) {
                totalSum += rating.getRatingValue();
            }
            else {
                errors++;
            }

            count++;
        }

        if ((count - errors) > 0) {
            ret = (double) totalSum / (double) (count - errors);
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    private Worker getWorkerByWorkerId(String workerId) {
        Worker ret = null;

        List<Worker> workers = entityManager
                .createQuery(
                        "SELECT w FROM Worker w WHERE w.workerId = :workerId")
                .setParameter("workerId", workerId).getResultList();

        if (!workers.isEmpty()) {
            ret = workers.get(0);
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    private CompanyRating getCompanyRating(MWTask task, 
                                           Worker worker,
                                           Company company) {

        CompanyRating ret = null;

        List<CompanyRating> ratings = entityManager
                .createQuery(
                        "SELECT r FROM CompanyRating r WHERE "
                                + "r.mwTask.id = :taskId AND "
                                + "r.worker.id = :workerId AND "
                                + "r.company.id = :companyId")
                .setParameter("taskId", task.getId())
                .setParameter("workerId", worker.getId())
                .setParameter("companyId", company.getId())
                .getResultList();

        if (!ratings.isEmpty()) {
            ret = ratings.get(0);
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    private ProductRating getProductRating(MWTask task, 
                                           Worker worker,
                                           Product product) {

        ProductRating ret = null;

        List<ProductRating> ratings = entityManager
                .createQuery(
                        "SELECT r FROM ProductRating r WHERE "
                                + "r.mwTask.id = :taskId AND "
                                + "r.worker.id = :workerId AND "
                                + "r.product.id = :productId")
                .setParameter("taskId", task.getId())
                .setParameter("workerId", worker.getId())
                .setParameter("productId", product.getId())
                .getResultList();

        if (!ratings.isEmpty()) {
            ret = ratings.get(0);
        }

        return ret;
    }
}
