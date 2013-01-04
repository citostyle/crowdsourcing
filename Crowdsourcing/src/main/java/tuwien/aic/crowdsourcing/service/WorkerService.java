
package tuwien.aic.crowdsourcing.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuwien.aic.crowdsourcing.persistence.CompanyRatingIndividualManager;
import tuwien.aic.crowdsourcing.persistence.ProductRatingIndividualManager;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;

@Service
public class WorkerService {

    @Autowired
    private CompanyRatingIndividualManager companyRatingIndividualManager;

    @Autowired
    private ProductRatingIndividualManager productRatingIndividualManager;

    @Transactional
    public List<Worker> getBadWorkers() {
        // the set is used to remove duplicates
        Set<Worker> workers = new HashSet<Worker>();
        workers.addAll(getBadWorkersFromCompanyRatings());
        workers.addAll(getBadWorkersFromProductRatings());
        
        return new ArrayList<Worker>(workers);
    }
    
    private List<Worker> getBadWorkersFromCompanyRatings() {
        return companyRatingIndividualManager.findBadWorkers();
    }
    
    private List<Worker> getBadWorkersFromProductRatings() {
        return productRatingIndividualManager.findBadWorkers();
    }

}
