
package tuwien.aic.crowdsourcing.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.ProductManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingManager;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Product;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;

@Service
public class ProductRatingService {

    @Autowired
    private ProductManager productManager;

    @Autowired
    private ProductRatingManager productRatingManager;

    @Transactional
    public void addProductSentiment(MWTask task,
            String productName, Integer result, String date) {
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
    }

    @Transactional
    public double getProductSentiment(Product product) {
        List<ProductRating> ratings = productRatingManager
                .findByProduct(product);
        return getProductSentiment(ratings);
    }

    @Transactional
    public double getProductSentiment(Product product,
            Date start) {
        List<ProductRating> ratings = productRatingManager
                .findByProduct(product, start);
        return getProductSentiment(ratings);
    }

    @Transactional
    public double getProductSentiment(Product product,
            Date start, Date limit) {
        List<ProductRating> ratings = productRatingManager
                .findByProduct(product, start, limit);
        return getProductSentiment(ratings);
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

}
