package fuzzy.ai;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.logging.LoggingFeature;

public class FuzzyAIClient {
	private static final String root = "https://api.fuzzy.ai";	
	private static final String version = "0.0.1";
	
	private final Client client;
	private final String key;
	
	public FuzzyAIClient(String key) {
		this.key = key;
		this.client = ClientBuilder.newClient();
		
		// Enable this to log requests
		client.register(new LoggingFeature(Logger.getGlobal(), Level.INFO, null, null));
	}
	
	public Evaluation evaluate(String agentId, boolean meta, Map<String, Integer> inputMap) {
		String url = root + "/agent/" + agentId;

		WebTarget webTarget = client.target(url);

		Invocation.Builder builder = webTarget
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + key)
				.header("User-Agent", "fuzzy.ai-java/" + version);
		
		if (meta)
			builder.header("meta", "true");
		
		Response response = builder.post(Entity.json(inputMap));

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		
		String evaluationId = response.getHeaderString("X-Evaluation-ID");
		
		@SuppressWarnings("unchecked")
		Map<String, Double> map = response.readEntity(Map.class);
		
		return new Evaluation(evaluationId, map);
	}
	
	public Feedback feedback(String evaluationId, Map<String, Double> metricMap) {
		String url = root + "/evaluation/" + evaluationId + "/feedback";

		WebTarget webTarget = client.target(url);

		Response response = webTarget
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + key)
				.header("User-Agent", "fuzzy.ai-java/" + version)
				.post(Entity.json(metricMap));

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> responseMap = response.readEntity(Map.class);
		
		String id = (String) responseMap.get("id");
		
		String status = (String) responseMap.get("status");
		
		ZonedDateTime dateTime = ZonedDateTime.parse((String) responseMap.get("createdAt"));
		
		@SuppressWarnings("unchecked")
		Map<String, Double> map = (Map<String, Double>) responseMap.get("data");
		
		String log = (String) responseMap.get("evaluationLog");
				
		return new Feedback(id, status, dateTime, map, log);
	}
}
