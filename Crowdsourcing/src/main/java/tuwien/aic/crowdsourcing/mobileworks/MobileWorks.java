
package tuwien.aic.crowdsourcing.mobileworks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tuwien.aic.crowdsourcing.mobileworks.environment.Environment;
import tuwien.aic.crowdsourcing.mobileworks.environment.SandboxEnvironment;
import tuwien.aic.crowdsourcing.mobileworks.task.ResponseResult;
import tuwien.aic.crowdsourcing.mobileworks.task.TaskResult;
import tuwien.aic.crowdsourcing.mobileworks.task.WorkflowType;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.Worker;
import tuwien.aic.crowdsourcing.rss.ArticleFetcher;
import tuwien.aic.crowdsourcing.util.HttpUtil;
import tuwien.aic.crowdsourcing.util.HttpUtil.HttpRequestMethod;
import tuwien.aic.crowdsourcing.util.HttpUtil.HttpResponse;
import tuwien.aic.crowdsourcing.util.StringUtil;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "mobileWorksService")
@Scope(value = "singleton")
public class MobileWorks {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ArticleFetcher.class);

    private static final String API_VERSION = "api/v2/";
    private static final String URL_TASK = "task/";
    private static final String URL_RESPONSE = "response/";

    private static final String MULTIPLE_CHOICE_FIELD_TYPE = "m";
    private static final String LINK_RESOURCE_TYPE = "l";

    private Environment environment;
    private String username;
    private String password;

    @PostConstruct
    public void init() {
        environment = new SandboxEnvironment();
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpUtil.HeaderFields.Request.CONTENT_TYPE,
                "application/json");

        // authorization header
        StringBuilder credentials = new StringBuilder();
        credentials.append(username);
        credentials.append(":");
        credentials.append(password);
        String encodedCredentials =
                Base64.encodeBase64String(StringUtils.getBytesUtf8(credentials
                        .toString()));
        StringBuilder authValue = new StringBuilder();
        authValue.append("Basic ");
        authValue.append(encodedCredentials);
        headers.put(HttpUtil.HeaderFields.Request.AUTHORIZATION,
                authValue.toString());

        return headers;
    }

    private void validateMWTask(MWTask task) throws IllegalArgumentException {
        if (task == null) {
            throw new IllegalArgumentException("MWTask must be provided.");
        }

        if (task.getTaskId() == null) {
            throw new IllegalArgumentException(
                    "MWTask must have a taskId. This taskId must be application unique.");
        }
    }

    // returns whether the post was successful or not
    // takes a list of fieldNames == questions
    // list of choices. choices are same for each fieldName
    public boolean postTask(MWTask task, String instructions,
            List<String> fieldNames, List<String> choices,
            WorkflowType workflowType) throws IllegalArgumentException {
    	return postTask(task, instructions, fieldNames, choices, workflowType, -1, -1F, null, null, null);
    }
    
    // additional parameters:
    // redundancy must be greater than 0 if not default value is used
    // payment (in cents) must be greater than 0 if not defaut vakue is used 
    // if no blocked, location, language is wanted, put null instead
    // languages need to be specified in ISO 639-1 http://en.wikipedia.org/wiki/ISO_639-1
    // locations need to be specified in ISO 3166-1 alpha 2 http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
    public boolean postTask(MWTask task, String instructions, List<String> fieldNames, List<String> choices, WorkflowType workflowType, int redundancy, float payment, List<Worker> blocked, List<String> languages, List<String> locations) throws IllegalArgumentException {
        // validations
        validateMWTask(task);
        if (instructions == null) {
            throw new IllegalArgumentException("instructions can not be null.");
        }
        if ((fieldNames == null) || fieldNames.isEmpty()) {
            throw new IllegalArgumentException(
                    "fieldNames can not be null or empty.");
        }
        if ((choices == null) || (choices.size() < 2)) {
            throw new IllegalArgumentException(
                    "choices can not be null and must have at least 2 elements.");
        }

        // prepare url
        StringBuilder url = new StringBuilder();
        url.append(environment.getBaseUrl());
        url.append(API_VERSION);
        url.append(URL_TASK);

        StringWriter sw = new StringWriter();
        try {
            JsonFactory fac = new JsonFactory();
            JsonGenerator gen = fac.createJsonGenerator(sw);

            gen.writeStartObject();

            gen.writeStringField("instructions", instructions);
            gen.writeStringField("taskid", task.getTaskId());
            Article a = task.getArticle();
            if ((a != null) && (a.getAddress() != null)) {
                gen.writeStringField("resource", a.getAddress());
                gen.writeStringField("resourcetype", LINK_RESOURCE_TYPE);
            }

            // fields
            gen.writeArrayFieldStart("fields");
            // build choices string
            StringBuilder choicesString = new StringBuilder();
            boolean first = true;
            for (String s : choices) {
                if (!first) {
                    choicesString.append(",");
                } else {
                    first = false;
                }
                choicesString.append(s);
            }

            // multiple choice fields
            for (String fieldName : fieldNames) {
                gen.writeStartObject();
                gen.writeStringField(fieldName, MULTIPLE_CHOICE_FIELD_TYPE);
                gen.writeStringField("choices", choicesString.toString());
                gen.writeEndObject();
            }
            gen.writeEndArray();

            // workflow
            String wft;
            if (WorkflowType.ITERATIVE.equals(workflowType)) {
                wft = "i";
            } else if (WorkflowType.MANUAL.equals(workflowType)) {
                wft = "m";
            } else if (WorkflowType.SURVEY.equals(workflowType)) {
                wft = "s";
            } else {
                // default == parallel
                wft = "p";
            }
            gen.writeStringField("workflow", wft);
            
            if(redundancy > 0) {
            	gen.writeNumberField("redundancy", redundancy);
            }
            
            if(payment > 0F) {
            	gen.writeNumberField("payment", payment);
            }
            
            // worker filtering fields
            addBlocked(gen, blocked);
            addStringArray(gen, "location", locations);
            addStringArray(gen, "language", languages);

            gen.writeEndObject();
            gen.flush();
            gen.close();
        } catch (IOException e) {
            LOGGER.error("could not generate JSON for posting a task ("
                    + task.getTaskId() + ")");
            return false;
        }

        HttpResponse response =
                HttpUtil.request(url.toString(), HttpRequestMethod.POST,
                        getHeaders(), null, new ByteArrayInputStream(sw
                                .toString().getBytes()),
                        HttpUtil.DEFAULT_TIMEOUT, false);

        if (response == null) {
            LOGGER.error("response is null");
            return false;
        }

        if (201 == response.getResponseCode()) {
            // success
            try {
                LOGGER.debug("Success: "
                        + StringUtil.streamToString(response.getBody()));
                System.out.println("Just posted task " + fieldNames);
            } catch (IOException e) {
                ;
            }
            return true;
        } else {
            // failure
            try {
                LOGGER.debug("Failure: "
                        + StringUtil.streamToString(response.getBody()));
            } catch (IOException e) {
                ;
            }
            return false;
        }
    }
    
    private void addBlocked(JsonGenerator gen, List<Worker> blocked) throws JsonGenerationException, IOException {
    	if(blocked == null || blocked.isEmpty()) {
    		return;
    	}
    	gen.writeArrayFieldStart("blocked");
    	for(Worker w : blocked) {
    		gen.writeString(String.valueOf(w.getId()));
    	}
    	gen.writeEndArray();
    }
    
    private void addStringArray(JsonGenerator gen, String label, List<String> strings) throws JsonGenerationException, IOException {
    	if(label == null || label.trim().isEmpty()) {
    		throw new IllegalArgumentException("label is required");
    	}
    	if(strings == null) {
    		return;
    	}
    	gen.writeArrayFieldStart(label);
    	for(String str : strings) {
    		gen.writeString(str);
    	}
    	gen.writeEndArray();
    }
    

    public TaskResult getResultsForTask(MWTask task)
            throws IllegalArgumentException {
        validateMWTask(task);

        // prepare url
        StringBuilder url = new StringBuilder();
        url.append(environment.getBaseUrl());
        url.append(API_VERSION);
        url.append(URL_TASK);
        url.append(task.getTaskId());
        url.append("/");

        HttpResponse response =
                HttpUtil.request(url.toString(),
                        HttpUtil.HttpRequestMethod.GET, getHeaders(), null,
                        null, HttpUtil.DEFAULT_TIMEOUT, false);

        if (response == null) {
            // some connection error occurred
            LOGGER.debug("response is null");
            return null;
        }

        if (response.getResponseCode() == 200) {
            LOGGER.debug("Response Code 200");
            // success
            try {
                ObjectMapper om = new ObjectMapper();
                TaskResult tr =
                        om.readValue(response.getBody(),
                                new TypeReference<TaskResult>() {
                                });
                return tr;
            } catch (JsonParseException e) {
                LOGGER.debug("Error while parsing response: " + e.getMessage());
            } catch (JsonMappingException e) {
                LOGGER.debug("Error while mapping response: " + e.getMessage());
            } catch (IOException e) {
                LOGGER.debug("IOException: " + e.getMessage());
            }
        } else {
            // failure
            LOGGER.debug("Response Code: " + response.getResponseCode());
            try {
                LOGGER.debug("Failure: "
                        + StringUtil.streamToString(response.getBody()));
            } catch (IOException e) {
                ;
            }
        }
        return null;
    }

    public ResponseResult getWorkerResultsForTask(MWTask task)
            throws IllegalArgumentException {
        validateMWTask(task);

        // prepare url
        StringBuilder url = new StringBuilder();
        url.append(environment.getBaseUrl());
        url.append(API_VERSION);
        url.append(URL_RESPONSE);
        url.append(task.getTaskId());
        url.append("/");

        HttpResponse response =
                HttpUtil.request(url.toString(),
                        HttpUtil.HttpRequestMethod.GET, getHeaders(), null,
                        null, HttpUtil.DEFAULT_TIMEOUT, false);

        if (response == null) {
            // some connection error occurred
            LOGGER.error("response is null");
            return null;
        }

        if ((response.getResponseCode() >= 200)
                && (response.getResponseCode() < 300)) {
            // success

            ObjectMapper om = new ObjectMapper();
            try {
                ResponseResult rr =
                        om.readValue(response.getBody(),
                                new TypeReference<ResponseResult>() {
                                });
                return rr;
            } catch (JsonParseException e) {
                LOGGER.debug("Error while parsing response: " + e.getMessage());
            } catch (JsonMappingException e) {
                LOGGER.debug("Error while mapping response: " + e.getMessage());
            } catch (IOException e) {
                LOGGER.debug("IOException: " + e.getMessage());
            }
        } else {
            // failure
            LOGGER.error("Response Code: " + response.getResponseCode());
        }
        return null;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
        LOGGER.debug("Environmont changed to: " + environment.toString());
    }
}
