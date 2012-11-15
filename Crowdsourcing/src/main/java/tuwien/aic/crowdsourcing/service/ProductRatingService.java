package tuwien.aic.crowdsourcing.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingManager;
import tuwien.aic.crowdsourcing.persistence.TaskManager;
import tuwien.aic.crowdsourcing.persistence.WorkerManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

@Service
public class ProductRatingService {

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private WorkerManager workerManager;

    @Autowired
    private ProductRatingManager productRatingManager;

    @Autowired
    private CompanyManager companyManager;

    @Transactional
    public void addProductSentiment(String taskId, String workerId,
            String productName, String companyName, Integer result) {
        MWTask task = taskManager.findByTaskId(taskId);
        Worker worker = workerManager.findByWorkerId(workerId);
        Company company = companyManager.findByName(companyName);

        if (task == null) {
            throw new IllegalArgumentException(
                    "The provided task does not exist!");
        }

        if (worker == null) {
            worker = new Worker(workerId);
            worker = workerManager.save(worker);
        }

        if (company == null) {
            company = new Company(companyName);
            company = companyManager.save(company);
        }
        
        Product product = productManager.findByCompanyAndName(company,
                                                              productName);

        if (product == null) {
            product = new Product(company, productName);
            product = productManager.save(product);
        }

        ProductRating rating = productRatingManager
                .findByTaskAndWorkerAndProduct(task, worker, product);

        if (rating == null) {
            rating = new ProductRating(task, worker, product, result);
            rating = productRatingManager.save(rating);
        } else {
            rating.setRatingValue(result);
            productRatingManager.save(rating);
        }
    }

    @Transactional
    public double getProductSentiment(String companyName, 
                                      String productName) {
        Company company = companyManager.findByName(companyName);

        if (company == null) {
            company = new Company(companyName);
            company = companyManager.save(company);
        }

        Product product = productManager.findByCompanyAndName(company,
                productName);
        
        if (product == null) {
            product = new Product(company, productName);
            product = productManager.save(product);
        }
        
        List<ProductRating> ratings = productRatingManager
                .findByProduct(product);
        
        return getProductSentiment(ratings);
    }

    @Transactional
    public double getProductSentiment(String companyName, 
                                      String productName, 
                                      Date start) {
        Company company = companyManager.findByName(companyName);

        if (company == null) {
            company = new Company(companyName);
            company = companyManager.save(company);
        }

        Product product = productManager.findByCompanyAndName(company,
                productName);
        
        if (product == null) {
            product = new Product(company, productName);
            product = productManager.save(product);
        }
        
        List<ProductRating> ratings = productRatingManager
                .findByProduct(product, start);
        
        return getProductSentiment(ratings);
    }

    @Transactional
    public double getProductSentiment(String companyName, 
                                      String productName, 
                                      Date start, Date limit) {
        Company company = companyManager.findByName(companyName);

        if (company == null) {
            company = new Company(companyName);
            company = companyManager.save(company);
        }

        Product product = productManager.findByCompanyAndName(company,
                productName);
        
        if (product == null) {
            product = new Product(company, productName);
            product = productManager.save(product);
        }
        
        List<ProductRating> ratings = productRatingManager
                .findByProduct(product, start, limit);
        
        return getProductSentiment(ratings);
    }

    @Transactional
    private double getProductSentiment(List<ProductRating> ratings) {
        long count = 0;
        long totalSum = 0;
        
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }

        Collections.sort(ratings, new Comparator<ProductRating>() {
            @Override
            public int compare(ProductRating o1, ProductRating o2) {
                if (o1.getRatingValue() == null) {
                    return Integer.MIN_VALUE;
                }
                if (o2.getRatingValue() == null) {
                    return Integer.MAX_VALUE;
                }
                return o1.getRatingValue().compareTo(o2.getRatingValue());
            }

        });
        
        int lower = (int) Math.floor(((double) ratings.size()) / 4);
        int upper = (int) Math.ceil((3.0 * ratings.size()) / 4);

        for (int i = lower; i <= upper; i++) {
            ProductRating rating = ratings.get(i);
            if (rating != null) {
                count++;
                totalSum += rating.getRatingValue();
            }
        }
        
        if (count > 0) {
            return ((double) totalSum) / count;
        }
        
        return 0;
    }

}
