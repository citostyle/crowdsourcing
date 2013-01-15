
package tuwien.aic.crowdsourcing.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingIndividualManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingManager;
import tuwien.aic.crowdsourcing.persistence.WorkerManager;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRatingIndividual;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

@Service
public class ProductRatingService {

    @Autowired
    private ProductManager productManager;

    @Autowired
    private ProductRatingManager productRatingManager;
    
    @Autowired
    private ProductRatingIndividualManager productRatingIndividualManager;
    
    @Autowired
    private WorkerManager workerManager;

    @Transactional
    public ProductRating addProductSentiment(MWTask task, String productName, Integer result, String date) {
        if (task == null) {
            throw new IllegalArgumentException(
                    "The provided task does not exist!");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date d = null;
        try {
            d = formatter.parse(date);
        } catch (ParseException e) {
            System.out.println("Finished date wasn't parsed: " + e.getMessage());
            d = Calendar.getInstance().getTime();
        }

        Product product = productManager.findByName(productName);

        if (product == null) {
            System.out.println("Have to create product " + productName);
            product = new Product(productName);
            product = productManager.save(product);
        }

        ProductRating rating = new ProductRating(task, product, result, d);
        rating = productRatingManager.save(rating);
        return rating;
    }

    @Transactional
    public ProductRatingIndividual addProductSentimentIndividual(ProductRating rating, Integer result, Integer timeTaken, String workerId) {
        rating = productRatingManager.findOne(rating.getId());
        if (rating == null)
            throw new IllegalArgumentException("The provided ProductRating does not exist!");
        
        Worker worker = workerManager.findByWorkerId(workerId);
        if (worker == null) {
            System.out.println("Have to create worker " + workerId);
            worker = new Worker(workerId);
            worker = workerManager.save(worker);
        }
        
        ProductRatingIndividual individual = new ProductRatingIndividual();
        individual.setRating(rating);
        individual.setRatingValue(result);
        individual.setTimeTaken(timeTaken);
        individual.setWorker(worker);
        
        rating.getIndividualRatings().add(individual);
        
        individual = productRatingIndividualManager.save(individual);
        productRatingManager.save(rating);
        
        return individual;
    }

    @Transactional
    public List<ProductRating> getProductSentiments(Product product) {
        return productRatingManager.findByProduct(product);
    }

    @Transactional
    public List<ProductRating> getProductSentiments(Product product, Date start) {
        return productRatingManager.findByProduct(product, start);
    }

    @Transactional
    public List<ProductRating> getProductSentiments(Product product, Date start, Date limit) {
        return productRatingManager.findByProduct(product, start, limit);
    }

    @Transactional
    public double getProductSentiment(Product product) {
        return getProductSentiment(getProductSentiments(product));
    }

    @Transactional
    public double getProductSentiment(Product product,
            Date start) {
        return getProductSentiment(getProductSentiments(product, start));
    }

    @Transactional
    public double getProductSentiment(Product product,
            Date start, Date limit) {
        return getProductSentiment(getProductSentiments(product, start, limit));
    }

    @Transactional
    private double getProductSentiment(List<ProductRating> ratings) {
        long count = 0;
        long totalSum = 0;

        if ((ratings == null) || ratings.isEmpty()) {
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
        upper = Math.min(upper, ratings.size() - 1);

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

    @Transactional
    public List<ProductRatingIndividual> getProductSentimentIndividuals(Product product) {
        List<ProductRatingIndividual> individuals = new ArrayList<ProductRatingIndividual>();
        for (ProductRating rating : getProductSentiments(product)) {
            individuals.addAll(productRatingIndividualManager.findByRating(rating));
        }
        return individuals;
    }

    @Transactional
    public List<ProductRatingIndividual> getProductSentimentIndividuals(Product product, Date start) {
        List<ProductRatingIndividual> individuals = new ArrayList<ProductRatingIndividual>();
        for (ProductRating rating : getProductSentiments(product, start)) {
            individuals.addAll(productRatingIndividualManager.findByRating(rating));
        }
        return individuals;
    }

    @Transactional
    public List<ProductRatingIndividual> getProductSentimentIndividuals(Product product, Date start, Date limit) {
        List<ProductRatingIndividual> individuals = new ArrayList<ProductRatingIndividual>();
        for (ProductRating rating : getProductSentiments(product, start, limit)) {
            individuals.addAll(productRatingIndividualManager.findByRating(rating));
        }
        return individuals;
    }

}
