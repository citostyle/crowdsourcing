
package tuwien.aic.crowdsourcing.qs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyRatingIndividualManager;
import tuwien.aic.crowdsourcing.persistence.CompanyRatingManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingIndividualManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingManager;
import tuwien.aic.crowdsourcing.persistence.TaskManager;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRatingIndividual;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRatingIndividual;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;
import tuwien.aic.crowdsourcing.rss.PeriodicArticleService;
import tuwien.aic.crowdsourcing.util.MathUtil;

@Component
public class PeriodicQualityManagement {

    @Autowired
    private TaskManager taskManager;
    
    @Autowired
    private CompanyRatingManager companyRatingManager;
    
    @Autowired
    private CompanyRatingIndividualManager companyRatingIndividualManager;
    
    @Autowired
    private ProductRatingManager productRatingManager;
    
    @Autowired
    private ProductRatingIndividualManager productRatingIndividualManager;
    
    private int getRatingSizeBoundary() {
        return 7;
    }
    
    private int getRatingDifference() {
        return 2;
    }

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void fetchResults() {
        List<MWTask> finishedTasks = taskManager.findByTaskState(TaskState.FINISHED);
        for (MWTask task : finishedTasks) {
            if (task.getType().equals(PeriodicArticleService.RATE_COMPANIES)) {
                processCompanyRatings(companyRatingManager.findByTask(task));
            } else if (task.getType().equals(PeriodicArticleService.RATE_PRODUCTS)) {
                processProductRatings(productRatingManager.findByTask(task));
            }
            setTaskState(task, TaskState.PROCESSED);
        }
    }

    @Async
    @Transactional
    private void processCompanyRatings(List<CompanyRating> ratings) {
        for (CompanyRating rating : ratings) {
            processCompanyRatingIndividuals(rating.getIndividualRatings(), rating.getRatingValue());
        }
    }
    
    private void processCompanyRatingIndividuals(List<CompanyRatingIndividual> ratings, Integer ratingValue) {
        if (ratings.size() > 1) {
            if (ratings.size() >= getRatingSizeBoundary()) {
                checkCompanyRatingIndividualsByIQR(ratings);
            } else {
                checkCompanyRatingIndividualsByParameters(ratings, ratingValue);
            }
        }
    }

    private void checkCompanyRatingIndividualsByIQR(List<CompanyRatingIndividual> ratings) {
        // check values by IQR
        int[] quartiles = MathUtil.quartiles(getCompanyRatingValues(ratings));
        for (CompanyRatingIndividual individual : ratings) {
            if (quartiles.length > 0 && (individual.getRatingValue() < quartiles[0] || quartiles[2] < individual.getRatingValue())) {
                // mark rating as bad
                saveBadCompanyRatingIndividual(individual);
            }
        }
    }
    
    private List<Integer> getCompanyRatingValues(List<CompanyRatingIndividual> ratings) {
        List<Integer> ratingValues = new ArrayList<Integer>();
        for (CompanyRatingIndividual i : ratings) {
            ratingValues.add(i.getRatingValue());
        }
        return ratingValues;
    }

    private void checkCompanyRatingIndividualsByParameters(List<CompanyRatingIndividual> ratings, Integer ratingValue) {
        // check values by rating difference
        for (CompanyRatingIndividual individual : ratings) {
            if (Math.abs(ratingValue - individual.getRatingValue()) > getRatingDifference()) {
                // mark rating as bad
                saveBadCompanyRatingIndividual(individual);
            }
        }
    }
    
    private void saveBadCompanyRatingIndividual(CompanyRatingIndividual individual) {
        individual.setBad(true);
        individual = companyRatingIndividualManager.save(individual);
    }

    @Async
    @Transactional
    private void processProductRatings(List<ProductRating> ratings) {
        for (ProductRating rating : ratings) {
            processProductRatingIndividuals(rating.getIndividualRatings(), rating.getRatingValue());
        }
    }
    
    private void processProductRatingIndividuals(List<ProductRatingIndividual> ratings, Integer ratingValue) {
        if (ratings.size() > 1) {
            if (ratings.size() >= getRatingSizeBoundary()) {
                checkProductRatingIndividualsByIQR(ratings);
            } else {
                checkProductRatingIndividualsByParameters(ratings, ratingValue);
            }
        }
    }

    private void checkProductRatingIndividualsByIQR(List<ProductRatingIndividual> ratings) {
        // check values by IQR
        int[] quartiles = MathUtil.quartiles(getProductRatingValues(ratings));
        for (ProductRatingIndividual individual : ratings) {
            if (quartiles.length > 0 && (individual.getRatingValue() < quartiles[0] || quartiles[2] < individual.getRatingValue())) {
                // mark rating as bad
                saveBadProductRatingIndividual(individual);
            }
        }
    }
    
    private List<Integer> getProductRatingValues(List<ProductRatingIndividual> ratings) {
        List<Integer> ratingValues = new ArrayList<Integer>();
        for (ProductRatingIndividual i : ratings) {
            ratingValues.add(i.getRatingValue());
        }
        return ratingValues;
    }

    private void checkProductRatingIndividualsByParameters(List<ProductRatingIndividual> ratings, Integer ratingValue) {
        // check values by rating difference
        for (ProductRatingIndividual individual : ratings) {
            if (Math.abs(ratingValue - individual.getRatingValue()) > getRatingDifference()) {
                // mark rating as bad
                saveBadProductRatingIndividual(individual);
            }
        }
    }
    
    private void saveBadProductRatingIndividual(ProductRatingIndividual individual) {
        individual.setBad(true);
        individual = productRatingIndividualManager.save(individual);
    }
    
    private void setTaskState(MWTask task, TaskState state) {
        task.setTaskState(state);
        taskManager.save(task);
    }
}
