
package tuwien.aic.crowdsourcing.resultpull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.mobileworks.MobileWorks;
import tuwien.aic.crowdsourcing.mobileworks.environment.SandboxEnvironment;
import tuwien.aic.crowdsourcing.mobileworks.task.ResponseResult;
import tuwien.aic.crowdsourcing.mobileworks.task.TaskResult;
import tuwien.aic.crowdsourcing.mobileworks.task.WorkerResult;
import tuwien.aic.crowdsourcing.persistence.TaskManager;
import tuwien.aic.crowdsourcing.persistence.WorkerManager;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.ProductRating;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;
import tuwien.aic.crowdsourcing.rss.PeriodicArticleService;
import tuwien.aic.crowdsourcing.service.CompanyRatingService;
import tuwien.aic.crowdsourcing.service.ProductRatingService;

@Component
public class PeriodicResultPuller {

    @Autowired
    private TaskManager taskManager;
    
    @Autowired
    private WorkerManager workerManager;

    @Autowired
    private MobileWorks mobileWorks;

    @Autowired
    private CompanyRatingService companyRatingService;

    @Autowired
    private ProductRatingService productRatingService;

    @PostConstruct
    public void postConstruct() {
        mobileWorks.setEnvironment(new SandboxEnvironment());
        mobileWorks.setCredentials("aic12", "aic12aic");
    }

    @Scheduled(fixedRate = 15000)
    @Transactional
    public void fetchResults() {
        List<MWTask> openTasks = taskManager.findByTaskState(TaskState.ACTIVE);
        for (MWTask t : openTasks) {
            getResult(t);
        }
    }

    @Async
    @Transactional
    private void getResult(MWTask t) {
        TaskResult result = mobileWorks.getResultsForTask(t);
        if (result == null) {
            return;
        }
        if (!result.getStatus().equals("done")) {
            return;
        }
        System.out.println("Task " + t.getTaskId() + " is done. Answer: " + result.getAnswer());
        processAnswer(result.getAnswer(), result.getTimeFinished(), t);
    }

    private void processAnswer(List<Map<String, String>> answer, String timeFinished, MWTask task) {
        for (Map<String, String> res : answer) {
            fillInResults(res, timeFinished, task);
        }
    }

    private void fillInResults(Map<String, String> res, String timeFinished, MWTask task) {
        for (Entry<String, String> entry : res.entrySet()) {
            String name = extractName(entry.getKey());
            if (task.getType().equals(PeriodicArticleService.RATE_COMPANIES)) {
                rateCompany(name, entry.getValue(), timeFinished, task);
            } else if (task.getType().equals(PeriodicArticleService.RATE_PRODUCTS)) {
                rateProduct(name, entry.getValue(), timeFinished, task);
            }
        }
    }

    private String extractName(String key) {
        Pattern p = Pattern.compile("^(.*?) \\(also kown as: .*$");
        Matcher m = p.matcher(key);
        if (m.find()) {
            return m.group(1);
        }
        return key;
    }

    private void rateCompany(String name, String result, String timeFinished, MWTask task) {
        Scanner scanner = new Scanner(result);
        if (scanner.hasNextInt()) {
            int rating = scanner.nextInt();
            CompanyRating companyRating = companyRatingService.addCompanySentiment(task, name, rating, timeFinished);
            addIndividualCompanyRating(task, companyRating, rating);
        }
        scanner.close();
        setTaskState(task, TaskState.FINISHED);
    }

    private void addIndividualCompanyRating(MWTask task, CompanyRating companyRating, Integer rating) {
        ResponseResult result = mobileWorks.getWorkerResultsForTask(task);
        if (result == null) {
            return;
        }
        if (!result.getStatus().equals("d")) { // done
            return;
        }
        // process each workers answer
        for (WorkerResult workerResult : result.getResults()) {
            companyRatingService.addCompanySentimentIndividual(companyRating, rating, workerResult.getTimeTaken(), workerResult.getWorkerId());
        }
    }

    private void rateProduct(String name, String result, String timeFinished, MWTask task) {
        Scanner scanner = new Scanner(result);
        if (scanner.hasNextInt()) {
            int rating = scanner.nextInt();
            ProductRating productRating = productRatingService.addProductSentiment(task, name, rating, timeFinished);
            addIndividualProduct(task, productRating, rating);
        }
        scanner.close();
        setTaskState(task, TaskState.FINISHED);
    }

    private void addIndividualProduct(MWTask task, ProductRating productRating, Integer rating) {
        ResponseResult result = mobileWorks.getWorkerResultsForTask(task);
        if (result == null) {
            return;
        }
        if (!result.getStatus().equals("d")) { // done
            return;
        }
        // process each workers answer
        for (WorkerResult workerResult : result.getResults()) {
            productRatingService.addProductSentimentIndividual(productRating, rating, workerResult.getTimeTaken(), workerResult.getWorkerId());
        }
    }
    
    private void setTaskState(MWTask task, TaskState state) {
        task.setTaskState(state);
        taskManager.save(task);
    }
}
