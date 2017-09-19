package fuzzy.ai;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;

public class FuzzyAIClient {
	private static final String root = "https://api.fuzzy.ai";	
	private static final String version = "0.0.1";
	
	private final ObjectMapper mapper;
	private final Client client;
	private final String key;
	
	public FuzzyAIClient(String key) {
		this.key = key;
		
		mapper = new ObjectMapper();
		
		ClientConfig config = new DefaultClientConfig();
		config.getSingletons().add(new JacksonJsonProvider(mapper));
		
		this.client = Client.create(config);
		client.addFilter(new LoggingFilter(System.out));
	}
	
	public Evaluation evaluate(String agentId, boolean meta, Map<String, Integer> inputMap) {
		String url = root + "/agent/" + agentId;

		WebResource webResource = client.resource(url);

		Builder builder = webResource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + key)
				.header("User-Agent", "fuzzy.ai-java/" + version);
		
		if (meta)
			builder.header("meta", "true");
		
		ClientResponse response = builder.post(ClientResponse.class, inputMap);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		
		String evaluationId = response.getHeaders().getFirst("X-Evaluation-ID");
		
		@SuppressWarnings("unchecked")
		Map<String, Double> map = response.getEntity(Map.class);
		
		return new Evaluation(evaluationId, map);
	}
	
	public Feedback feedback(String evaluationId, Map<String, Double> metricMap) {
		String url = root + "/evaluation/" + evaluationId + "/feedback";

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + key)
				.header("User-Agent", "fuzzy.ai-java/" + version)
				.post(ClientResponse.class, metricMap);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		@SuppressWarnings("unchecked")
		Map<String, String> responseMap = response.getEntity(Map.class);
		
		String id = responseMap.get("id");
		
		String status = responseMap.get("status");
		
		ZonedDateTime dateTime = ZonedDateTime.parse(responseMap.get("createdAt"));
		
		String data = responseMap.get("data");
		Map<String, Double> map;
		try {
			@SuppressWarnings("unchecked")
			Map<String, Double> m = mapper.readValue(data, Map.class);
			map = m;
		} catch (IOException e) {
			throw new RuntimeException("Failed : Data : " + data);
		}
		
		String log = responseMap.get("evaluationLog");
				
		return new Feedback(id, status, dateTime, map, log);
	}
}
