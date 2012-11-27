
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

import tuwien.aic.crowdsourcing.persistence.CompanyManager;
import tuwien.aic.crowdsourcing.persistence.CompanyRatingManager;
import tuwien.aic.crowdsourcing.persistence.entities.Company;
import tuwien.aic.crowdsourcing.persistence.entities.CompanyRating;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;

@Service
public class CompanyRatingService {

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private CompanyRatingManager companyRatingManager;

    @Transactional
    public void addCompanySentiment(MWTask task, String companyName, Integer result, String date) {
        Company company = companyManager.findByName(companyName);
        if (task == null) {
            throw new IllegalArgumentException(
                    "The provided task does not exist!");
        }

        if (company == null) {
            System.out.println("Have to create company " + companyName);
            company = new Company(companyName);
            company = companyManager.save(company);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date d = null;
        try {
            d = formatter.parse(date);
        } catch (ParseException e) {
            System.out.println("Finished date wasn't parsed: " + e.getMessage());
            d = Calendar.getInstance().getTime();
        }

        CompanyRating rating = new CompanyRating(task, company, result, d);
        rating = companyRatingManager.save(rating);
    }

    @Transactional
    public double getCompanySentiment(Company company) {
        List<CompanyRating> ratings = companyRatingManager
                .findByCompany(company);
        return getCompanySentiment(ratings);
    }

    @Transactional
    public double getCompanySentiment(Company company, Date start) {
        List<CompanyRating> ratings = companyRatingManager
                .findByCompany(company, start);
        return getCompanySentiment(ratings);
    }

    @Transactional
    public double getCompanySentiment(Company company, Date start, Date limit) {
        List<CompanyRating> ratings = companyRatingManager
                .findByCompany(company, start, limit);
        return getCompanySentiment(ratings);
    }

    @Transactional
    private double getCompanySentiment(List<CompanyRating> ratings) {
        long count = 0;
        long totalSum = 0;

        if ((ratings == null) || ratings.isEmpty()) {
            return 0;
        }

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
        upper = Math.min(upper, ratings.size() - 1);

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
