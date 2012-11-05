package tuwien.aic.crowdsourcing.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.CompanyRatingManager;
import tuwien.aic.crowdsourcing.persistence.TaskManager;
import tuwien.aic.crowdsourcing.persistence.WorkerManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

@Service
public class CompanyRatingService {

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private WorkerManager workerManager;

    @Autowired
    private CompanyRatingManager companyRatingManager;

    public void addCompanySentiment(String taskId, String workerId,
            String companyName, Integer result) {
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

        CompanyRating rating = companyRatingManager
                .findByTaskAndWorkerAndCompany(task, worker, company);

        if (rating == null) {
            rating = new CompanyRating(task, worker, company, result);
            rating = companyRatingManager.save(rating);
        } else {
            rating.setRatingValue(result);
            companyRatingManager.save(rating);
        }
    }

    public double getCompanySentiment(String companyName) {
        long count = 0;
        long totalSum = 0;

        Company company = companyManager.findByName(companyName);
        if (company == null) {
            throw new IllegalArgumentException("No such company: "
                    + companyName);
        }
        List<CompanyRating> ratings = companyRatingManager
                .findByCompany(company);
        Collections.sort(ratings, new Comparator<CompanyRating>() {
            @Override
            public int compare(CompanyRating o1, CompanyRating o2) {
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
            CompanyRating rating = ratings.get(i);
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
