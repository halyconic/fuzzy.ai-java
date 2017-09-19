package fuzzy.ai;

import java.util.Map;

public class Evaluation {
	private String id;
	private Map<String, Double> map;
	
	public Evaluation(String id, Map<String, Double> map) {
		this.id = id;
		this.map = map;
	}
	
	public String getId() {
		return id;
	}
	
	public Map<String, Double> getMap() {
		return map;
	}
}
