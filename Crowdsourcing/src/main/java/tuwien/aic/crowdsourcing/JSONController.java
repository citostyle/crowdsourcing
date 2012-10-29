package tuwien.aic.crowdsourcing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tuwien.aic.crowdsourcing.persistence.entities.TestEntity;

/**
 * Handles requests for the application home page.
 */
@Controller
public class JSONController {

    private static final Logger logger = LoggerFactory
            .getLogger(JSONController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/json/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TestEntity get(@PathVariable String id) {

        logger.info("Generating JSON response for id " + id);

        TestEntity testEntity = new TestEntity();

        testEntity.setName(id);
        try {
            testEntity.setId(Long.valueOf(id));
        } catch (NumberFormatException e) {
        }

        return testEntity;
    }

}
