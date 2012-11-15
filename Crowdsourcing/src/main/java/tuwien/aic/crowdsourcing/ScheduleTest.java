package tuwien.aic.crowdsourcing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTest {

    private static final Logger logger = LoggerFactory
            .getLogger(ScheduleTest.class);

    //@Scheduled(fixedRate = 5000)
    public void doWork() {
        logger.info("Arbeit, Arbeit...");
    }
}
