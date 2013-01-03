package tuwien.aic.crowdsourcing.mobileworks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tuwien.aic.crowdsourcing.mobileworks.environment.SandboxEnvironment;
import tuwien.aic.crowdsourcing.mobileworks.task.TaskResult;
import tuwien.aic.crowdsourcing.mobileworks.task.WorkflowType;
import tuwien.aic.crowdsourcing.persistence.entities.Article;
import tuwien.aic.crowdsourcing.persistence.entities.MWTask;
import tuwien.aic.crowdsourcing.persistence.entities.TaskState;

public class PostTaskTest {
	
	private static String username;
	private static String password;
	
	private static MobileWorks mw;
	private static List<String> choices;
	
	private MWTask task;
	private List<String> fieldNames;
	
	public PostTaskTest() {
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		username = "aic12";
		password = "aic12aic";
		
		mw = new MobileWorks();
		mw.setEnvironment(new SandboxEnvironment());
		mw.setCredentials(username, password);
		
		choices = new ArrayList<String>();
		choices.add("1");
		choices.add("2");
		choices.add("3");
		choices.add("4");
		choices.add("5");
	}
	
	@Before
	public void setUp() throws Exception {
		Article a = new Article("Super Artikle", "http://www.google.at/");
		UUID id = UUID.randomUUID();
		task = new MWTask(a, id.toString(), "type", TaskState.ACTIVE);
		
		fieldNames = new ArrayList<String>();
		fieldNames.add("Good Article");
	}
	
	@Test
	public void additionalParameters() throws Exception {
		int redundancy = 5;
		float payment = 1.7F;
		
		List<String> languages = new ArrayList<String>();
		languages.add("en");
		
		List<String> locations = new ArrayList<String>();
		locations.add("GB");
		
		boolean posted = mw.postTask(task, "follow the link", fieldNames, choices, WorkflowType.PARALLEL, redundancy, payment, null, languages, locations);
		assert posted;
		
		TaskResult tr = mw.getResultsForTask(task);
		assert tr.getTaskid() == task.getTaskId();
		assert tr.getRedundancy() == redundancy;
	}
	
	@After
	public void tearDown() throws Exception {
	}
}
