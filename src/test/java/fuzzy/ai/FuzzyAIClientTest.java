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
		Assert.assertNotNull(evaluationId);

		Map<String, Double> evaluationMap = evaluation.getMap();
		Assert.assertNotNull(evaluationMap);

		Double pricePerCup = evaluationMap.get("Price per cup");
		Assert.assertNotNull(pricePerCup);
	}

	@Test
	public void testFeedback() {
		FuzzyAIClient client = new FuzzyAIClient(API_KEY);
		
		Map<String, Integer> inputMap = new HashMap<>();
		inputMap.put("Temperature", 90);
		
		Evaluation evaluation = new FuzzyAIClient(API_KEY).evaluate(AGENT_ID, true, inputMap);

		String evaluationId = evaluation.getId();
		Assert.assertNotNull(evaluationId);

		Map<String, Double> evaluationMap = evaluation.getMap();
		Assert.assertNotNull(evaluationMap);

		Double pricePerCup = evaluationMap.get("Price per cup");
		Assert.assertNotNull(pricePerCup);
		
		Map<String, Double> metricMap = new HashMap<>();
		metricMap.put("Price per cup", 6.0);
		
		Feedback feedback = client.feedback(evaluationId, metricMap);
		
		String feedbackStatus = feedback.getStatus();
		Assert.assertEquals("OK", feedbackStatus);
		
		Map<String, Double> feedbackMap = feedback.getMap();
		Assert.assertNotNull(feedbackMap);
		
		String feedbackEvaluationLog = feedback.getEvaluationLog();
		Assert.assertNotNull(feedbackEvaluationLog);
		
		ZonedDateTime createdDateTime = feedback.getCreatedDateTime();
		Assert.assertNotNull(createdDateTime);
		
		String id = feedback.getId();
		Assert.assertNotNull(id);
	}
}
