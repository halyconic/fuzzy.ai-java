package fuzzy.ai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class FuzzyAIClientTest {
	private static final String API_KEY = "test-api-key";
	private static final String AGENT_ID = "test-agent-id";
	
	@Test
	public void testEvaluate() throws IOException {
		FuzzyAIClient client = new FuzzyAIClient(API_KEY);
		
		Map<String, Integer> inputMap = new HashMap<>();
		inputMap.put("Temperature", 90);
		
		Evaluation evaluation = client.evaluate(AGENT_ID, true, inputMap);
		
		Assert.assertNotNull(evaluation.getId());
		Assert.assertNotNull(evaluation.getMap());
		Assert.assertNotNull(evaluation.getMap().get("Price per cup"));
	}

	@Test
	public void testFeedback() {
		FuzzyAIClient client = new FuzzyAIClient(API_KEY);
		
		Map<String, Integer> inputMap = new HashMap<>();
		inputMap.put("Temperature", 90);
		
		Evaluation evaluation = new FuzzyAIClient(API_KEY).evaluate(AGENT_ID, true, inputMap);

		Assert.assertNotNull(evaluation.getId());
		Assert.assertNotNull(evaluation.getMap());
		Assert.assertNotNull(evaluation.getMap().get("Price per cup"));
		
		Map<String, Double> metricMap = new HashMap<>();
		metricMap.put("Price per cup", 6.0);
		
		Feedback feedback = client.feedback(evaluation.getId(), metricMap);
		
		Assert.assertEquals("OK", feedback.getStatus());
		Assert.assertNotNull(feedback.getMap());
		Assert.assertNotNull(feedback.getEvaluationLog());
		Assert.assertNotNull(feedback.getCreatedDateTime());
		Assert.assertNotNull(feedback.getId());
	}
}
