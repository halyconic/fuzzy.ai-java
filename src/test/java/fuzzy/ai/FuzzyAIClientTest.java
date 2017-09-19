package fuzzy.ai;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;

public class FuzzyAIClientTest {
	private static final String API_KEY = "test-api-key";
	private static final String AGENT_ID = "test-agent-id";
	
	@Test
	public void testEvaluate() throws ClientProtocolException, IOException {
		FuzzyAIClient client = new FuzzyAIClient(API_KEY);
		
		Map<String, Integer> inputMap = new HashMap<>();
		inputMap.put("Temperature", 90);
		
		Evaluation evaluation = client.evaluate(AGENT_ID, true, inputMap);
		
		String evaluationId = evaluation.getId();
		System.out.println(evaluationId);
		Assert.assertNotNull(evaluationId);

		Map<String, Double> evaluationMap = evaluation.getMap();
		System.out.println(evaluationMap);
		Assert.assertNotNull(evaluationMap);

		Double pricePerCup = evaluationMap.get("Price per cup");
		System.out.println(pricePerCup);
		Assert.assertNotNull(pricePerCup);
	}

	@Test
	public void testFeedback() {
		FuzzyAIClient client = new FuzzyAIClient(API_KEY);
		
		Map<String, Integer> inputMap = new HashMap<>();
		inputMap.put("Temperature", 90);
		
		Evaluation evaluation = new FuzzyAIClient(API_KEY).evaluate(AGENT_ID, true, inputMap);

		String evaluationId = evaluation.getId();
		System.out.println(evaluationId);
		Assert.assertNotNull(evaluationId);

		Map<String, Double> evaluationMap = evaluation.getMap();
		System.out.println(evaluationMap);
		Assert.assertNotNull(evaluationMap);

		Double pricePerCup = evaluationMap.get("Price per cup");
		System.out.println(pricePerCup);
		Assert.assertNotNull(pricePerCup);
		
		Map<String, Double> metricMap = new HashMap<>();
		metricMap.put("Price per cup", 6.0);
		
		Feedback feedback = client.feedback(evaluationId, metricMap);
		System.out.println(feedback);
		
		String feedbackStatus = feedback.getStatus();
		System.out.println(feedbackStatus);
		Assert.assertEquals("OK", feedbackStatus);
		
		Map<String, Double> feedbackMap = feedback.getMap();
		System.out.println(feedbackMap);
		Assert.assertNotNull(feedbackMap);
		
		String feedbackEvaluationLog = feedback.getEvaluationLog();
		System.out.println(feedbackEvaluationLog);
		Assert.assertNotNull(feedbackEvaluationLog);
		
		ZonedDateTime createdDateTime = feedback.getCreatedDateTime();
		System.out.println(createdDateTime);
		Assert.assertNotNull(createdDateTime);
		
		String id = feedback.getId();
		System.out.println(id);
		Assert.assertNotNull(id);
	}
}
