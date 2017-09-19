package fuzzy.ai;

import java.time.ZonedDateTime;
import java.util.Map;

public class Feedback {
	public String id;
	public String status;
	public ZonedDateTime createdDateTime;
	public Map<String, Double> map;
	public String evaluationLog;
	
	public Feedback(String id, String status, ZonedDateTime createdDateTime, Map<String, Double> map,
			String evaluationLog) {
		this.id = id;
		this.status = status;
		this.createdDateTime = createdDateTime;
		this.map = map;
		this.evaluationLog = evaluationLog;
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public ZonedDateTime getCreatedDateTime() {
		return createdDateTime;
	}
	
	public Map<String, Double> getMap() {
		return map;
	}

	public String getEvaluationLog() {
		return evaluationLog;
	}
}
