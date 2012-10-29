package tuwien.aic.crowdsourcing.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tuwien.aic.crowdsourcing.persistence.entities.*;

public class SentimentManagerImpl implements SentimentManager {

    @PersistenceContext
    private EntityManager entityManager = null;

    public SentimentManagerImpl() {
        
    }
    
    @Override
    public void addCompanySentiment(String taskId, String workerId, 
                                    String companyName, Integer result) {
        
        MWTask task = 
            getTaskByTaskId(taskId);
        
        Worker worker = 
            getWorkerByWorkerId(workerId);
        
        Company company = getCompanyByName(companyName);
        
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
        
        CompanyRating rating = 
            getCompanyRating(task, worker, company);
        
        if (rating == null) {
            rating = new CompanyRating(task, worker, company, result);
            
            entityManager.persist(rating);
        }
        else {
            rating.setRatingValue(result);
            
            entityManager.merge(rating);
        }
    }

    @Override
    public void addProductSentiment(String taskId, String workerId, 
                                    String productName, Integer result) {
        
        MWTask task = 
            getTaskByTaskId(taskId);
        
        Worker worker = 
            getWorkerByWorkerId(workerId);
        
        Product product = getProductByName(productName);
        
        if (task == null) {
            throw new IllegalArgumentException
                ("The provided task does not exist!");
        }
        
        if (worker == null) {
            worker = new Worker(workerId);
            
            entityManager.persist(worker);
            
            entityManager.refresh(worker);
        }
        
        if (product == null) {
            product = new Product(productName);
            
            entityManager.persist(product);
            
            entityManager.refresh(product);
        }
        
        ProductRating rating = 
            getProductRating(task, worker, product);
        
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
    public void addCorrelation(String taskId, String workerId, 
                               String companyName, String productName) {
        
        MWTask task = 
            getTaskByTaskId(taskId);
        
        Worker worker = 
            getWorkerByWorkerId(workerId);
        
        Company company = getCompanyByName(companyName);
        
        Product product = getProductByName(productName);
        
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
        
        if (product == null) {
            product = new Product(productName);
            
            entityManager.persist(product);
            
            entityManager.refresh(product);
        }
        
        Correlation correlation = 
            getCorrelation(task, worker, company, product);
        
        if (correlation == null) {
            correlation = new Correlation(task, worker, company, product);
            
            entityManager.persist(correlation);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double getCompanySentiment(String companyName) {
        
        Double ret = null;
        
        long count = 0;
        long errors = 0;
        long totalSum = 0;
        
        List<CompanyRating> ratings =
            (List<CompanyRating>)entityManager.createQuery
                ("SELECT r FROM CompanyRating r " +
                 "WHERE r.company.name = :companyName")
                    .setParameter("companyName", companyName).getResultList();
        
        for (CompanyRating rating : ratings) {
            if (rating.getRatingValue() != null) {
                totalSum += rating.getRatingValue();
            }
            else {
                errors++;
            }
            
            count++;
        }
        
        if (count - errors > 0) {
            ret = (double)totalSum / (double)(count - errors);
        }
        
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double getProductSentiment(String productName) {
        
        Double ret = null;
        
        long count = 0;
        long errors = 0;
        long totalSum = 0;
        
        List<ProductRating> ratings =
            (List<ProductRating>)entityManager.createQuery
                ("SELECT r FROM ProductRating r " +
                 "WHERE r.product.name = :productName")
                    .setParameter("productName", productName).getResultList();
        
        for (ProductRating rating : ratings) {
            if (rating.getRatingValue() != null) {
                totalSum += rating.getRatingValue();
            }
            else {
                errors++;
            }
            
            count++;
        }
        
        if (count - errors > 0) {
            ret = (double)totalSum / (double)(count - errors);
        }
        
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MWTask> getActiveTasks() {
        
        List<MWTask> tasks =
            (List<MWTask>)entityManager.createQuery
                ("SELECT t FROM mwTask t WHERE " +
                 "t.taskState = TaskState.ACTIVE").getResultList();
        
        return tasks;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getProductNames(String companyName) {
        
        List<String> ret =
            (List<String>)entityManager.createQuery
                ("SELECT DISTINCT p.name FROM Product p " +
                 "JOIN Correlation c JOIN Company c2 " +
                 "WHERE c2.name = :companyName " +
                 "ORDER BY p.name")
                    .setParameter("companyName", companyName)
                    .getResultList();
        
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    private MWTask getTaskByTaskId(String taskId) {
        MWTask ret = null;
        
        List<MWTask> tasks =
            (List<MWTask>)entityManager.createQuery
                ("SELECT t FROM MWTask t WHERE t.taskId = :taskId")
                    .setParameter("taskId", taskId).getResultList();
        
        if (!tasks.isEmpty()) {
            ret = tasks.get(0);
        }
        
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    private Worker getWorkerByWorkerId(String workerId) {
        Worker ret = null;
        
        List<Worker> workers =
            (List<Worker>)entityManager.createQuery
                ("SELECT w FROM Worker w WHERE w.workerId = :workerId")
                    .setParameter("workerId", workerId).getResultList();
        
        if (!workers.isEmpty()) {
            ret = workers.get(0);
        }
        
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    private Company getCompanyByName(String companyName) {
        Company ret = null;
        
        List<Company> companies =
            (List<Company>)entityManager.createQuery
                ("SELECT c FROM Company c WHERE c.name = :companyName")
                    .setParameter("companyName", companyName).getResultList();
        
        if (!companies.isEmpty()) {
            ret = companies.get(0);
        }
        
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    private Product getProductByName(String productName) {
        Product ret = null;
        
        List<Product> products =
            (List<Product>)entityManager.createQuery
                ("SELECT p FROM Product p WHERE p.name = :productName")
                    .setParameter("productName", productName).getResultList();
        
        if (!products.isEmpty()) {
            ret = products.get(0);
        }
        
        return ret;
    }    
    
    @SuppressWarnings("unchecked")
    private CompanyRating getCompanyRating(MWTask task, 
                                           Worker worker, 
                                           Company company) {
        
        CompanyRating ret = null;
        
        List<CompanyRating> ratings =
            (List<CompanyRating>)entityManager.createQuery
                ("SELECT r FROM CompanyRating r WHERE " +
                    "r.mwTask.id = :taskId AND " +
                    "r.worker.id = :workerId AND " +
                    "r.company.id = :companyId")
                    .setParameter("taskId", task.getId())
                    .setParameter("workerId", worker.getId())
                    .setParameter("companyId", company.getId()).getResultList();
        
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
        
        List<ProductRating> ratings =
            (List<ProductRating>)entityManager.createQuery
                ("SELECT r FROM ProductRating r WHERE " +
                    "r.mwTask.id = :taskId AND " +
                    "r.worker.id = :workerId AND " +
                    "r.product.id = :productId")
                    .setParameter("taskId", task.getId())
                    .setParameter("workerId", worker.getId())
                    .setParameter("productId", product.getId()).getResultList();
        
        if (!ratings.isEmpty()) {
            ret = ratings.get(0);
        }
        
        return ret;
    }    
    
    @SuppressWarnings("unchecked")
    private Correlation getCorrelation(MWTask task, 
                                       Worker worker, 
                                       Company company,
                                       Product product) {
        
        Correlation ret = null;
        
        List<Correlation> correlations =
            (List<Correlation>)entityManager.createQuery
                ("SELECT c FROM Correlation c WHERE " +
                    "c.mwTask.id = :taskId AND " +
                    "c.worker.id = :workerId AND " +
                    "c.company.id = :companyId AND " +
                    "c.product.id = :productId")
                    .setParameter("taskId", task.getId())
                    .setParameter("workerId", worker.getId())
                    .setParameter("companyId", company.getId())
                    .setParameter("productId", product.getId()).getResultList();
        
        if (!correlations.isEmpty()) {
            ret = correlations.get(0);
        }
        
        return ret;
    }    
}
