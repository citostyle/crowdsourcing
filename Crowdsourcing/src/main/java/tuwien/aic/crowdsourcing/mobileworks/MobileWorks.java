package tuwien.aic.crowdsourcing.mobileworks;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tuwien.aic.crowdsourcing.mobileworks.environment.Environment;
import tuwien.aic.crowdsourcing.mobileworks.environment.SandboxEnvironment;
import tuwien.aic.crowdsourcing.mobileworks.task.TaskResult;
import tuwien.aic.crowdsourcing.mobileworks.task.WorkerResult;
import tuwien.aic.crowdsourcing.mobileworks.task.WorkflowType;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.util.HttpUtil;
import tuwien.aic.crowdsourcing.util.HttpUtil.HttpRequestMethod;
import tuwien.aic.crowdsourcing.util.HttpUtil.HttpResponse;

@Service(value="mobileWorksService")
@Scope(value="singleton")
public class MobileWorks {
	
	private static final Logger LOGGER = Logger.getLogger(MobileWorks.class);
	
	private static final String API_VERSION = "api/v2/";
	private static final String URL_TASK = "task/";
	private static final String URL_RESPONSE = "response/";
	
	private static final String MULTIPLE_CHOICE_FIELD_TYPE = "m";
	private static final String LINK_RESOURCE_TYPE = "l";
	
	private Environment environment;
	private WorkflowType workflowType;
	private String username;
	private String password;
	
	public static void main(String[] args) {
		MobileWorks mw = new MobileWorks();
		mw.setCredentials("chrstphlbr", "12061988");
		mw.setEnvironment(new SandboxEnvironment());
		mw.setWorkflowType(WorkflowType.PARALELL);
		
		MWTask t = new MWTask();
		t.setTaskId("task1");
		t.setArticle(new Article("cooler artikel", "http://www.google.at/"));
		
		List<String> choices = new ArrayList<String>();
		choices.add("1");
		choices.add("2");
		choices.add("3");
		choices.add("4");
		choices.add("5");
		String resp;
		List<TaskResult> tr = mw.getResultsForTask(t);
		System.out.println(tr);
//		try {
//			resp = mw.postTask(t, "ist diese seite cool?", "Rating", choices);
//			System.out.println(resp);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@PostConstruct
    public void init() {
		environment = new SandboxEnvironment();
		workflowType = WorkflowType.PARALELL;
    }
	
	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpUtil.HeaderFields.Request.CONTENT_TYPE, "application/json");
		
		// authorization header
		StringBuilder credentials = new StringBuilder();
		credentials.append(username);
		credentials.append(":");
		credentials.append(password);
		String encodedCredentials = Base64.encodeBase64String(StringUtils.getBytesUtf8(credentials.toString()));
		StringBuilder authValue = new StringBuilder();
		authValue.append("Basic ");
		authValue.append(encodedCredentials);
		headers.put(HttpUtil.HeaderFields.Request.AUTHORIZATION, authValue.toString());
		
		return headers;
	}
	
	// returns location of the task
	public String postTask(MWTask task, String instructions, String fieldName, List<String> choices) throws IOException {
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
			if(a != null && a.getAddress() != null) {
				gen.writeStringField("resource", a.getAddress());
				gen.writeStringField("resourcetype", LINK_RESOURCE_TYPE);
			}
			
			// fields
			gen.writeArrayFieldStart("fields");
			// multiple choice field
			gen.writeStartObject();
			gen.writeStringField(fieldName, MULTIPLE_CHOICE_FIELD_TYPE);
			StringBuilder choicesString = new StringBuilder();
			boolean first = true;
			for(String s : choices) {
				if(!first) {
					choicesString.append(",");
				} else {
					first = false;
				}
				choicesString.append(s);
			}
			gen.writeStringField("choices", choicesString.toString());
			gen.writeEndObject();
			gen.writeEndArray();
			
			// workflow
			String wft;
			if(WorkflowType.ITERATIVE.equals(workflowType)) {
				wft = "i";
			} else if(WorkflowType.MANUAL.equals(workflowType)) {
				wft = "m";
			} else if(WorkflowType.SURVEY.equals(workflowType)) {
				wft = "s";
			} else {
				// default == parallel
				wft = "p";
			}
			gen.writeStringField("workflow", wft);
			
			gen.writeEndObject();
			gen.flush();
			gen.close();
		} catch (IOException e) {
			LOGGER.error("could not generate JSON for posting a task (" + task.getTaskId() + ")");
			return null;
		}
		
		
		HttpResponse response = HttpUtil.request(url.toString(), HttpRequestMethod.POST, getHeaders(), null, new ByteArrayInputStream(sw.toString().getBytes()), HttpUtil.DEFAULT_TIMEOUT, false);
		
		if(201 == response.getResponseCode()) {
			// success
			return streamToString(response.getBody());
		} else {
			// failure
			return streamToString(response.getBody());
		}
	}
	
	public static String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        bufferedReader.close();
        return stringBuilder.toString();
    }
	
	public void rejectTask(MWTask task, String reason) {
		
	}
	
	public void acceptTask(MWTask task) {
		
	}
	
	public List<TaskResult> getResultsForTask(MWTask task) {
		// prepare url
		StringBuilder url = new StringBuilder();
		url.append(environment.getBaseUrl());
		url.append(API_VERSION);
		url.append(URL_TASK);
		
		HttpResponse response = HttpUtil.request(url.toString(), HttpUtil.HttpRequestMethod.GET, getHeaders(), null, null, HttpUtil.DEFAULT_TIMEOUT, false);
		
		if(response == null) {
			// some connection error occurred
			LOGGER.error("reponse is null");
			return null;
		}
		
		if(response.getResponseCode() == 200) {
			LOGGER.debug("Response Code 200");
			// success
			ObjectMapper om = new ObjectMapper();
			try {
				List<TaskResult> tr = om.readValue(response.getBody(), new TypeReference<List<TaskResult>>() {});
				return tr;
			} catch (JsonParseException e) {
				LOGGER.info("Error while parsing response: " + e.getMessage());
				e.printStackTrace();
			} catch (JsonMappingException e) {
				LOGGER.info("Error while mapping response: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				LOGGER.info("IOException: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			LOGGER.info("Response Code: " + response.getResponseCode());
		}
		try {
			System.out.println(streamToString(response.getBody()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// failure
		return null;
	}
	
	public List<WorkerResult> getWorkerResultsForTask(MWTask task) {
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

	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(WorkflowType workflowType) {
		this.workflowType = workflowType;
	}
}
