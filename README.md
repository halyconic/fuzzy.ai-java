# Fuzzy.ai Client for Java

Client library for the fuzzy.ai RESTful API

## Installation

You can get this library by forking it from our Github repository:

After that, you can import the project via gradle, using the included gradle wrapper.

## Testing

```
git clone https://github.com/fuzzy-ai/fuzzy.ai-python.git
```

## Usage

This gem handles the most basic usage of Fuzzy.ai.

```java
# Require the module

import fuzzy.ai.FuzzyAIClient.java

# Your API key (get one from https://fuzzy.io/)

private static final String API_KEY = "YOUR_API_KEY_HERE"

# Create a client

FuzzyAIClient client = new FuzzyAIClient(API_KEY)

# ID of the agent you want to call; get it from https://fuzzy.ai/

private static final String AGENT_ID = "YOUR_AGENT_ID_HERE";

# Inputs; map of string or symbol to numbers

inputs = {
  input1: 10,
  input2: 30
}

Map<String, Integer> inputMap = new HashMap<>();
inputMap.put("input1", 10);
inputMap.put("input2", 30);

# Ask the agent to evaluate the inputs; returns two values!

Evaluation evaluation = client.evaluate(AGENT_ID, true, inputMap);

# Evaluation contains all the outputs

System.out.println(evaluation.getMap().get("output1"));

# An opaque ID for the evaluation

System.out.println(evaluation.getId());

# For feedback, provide a performance metric

Map<String, Double> metricMap = new HashMap<>();
metricMap.put("performance1", 3);

Feedback feedback = client.feedback(evaluationId, metricMap);
```

### FuzzyAiClient

Class representing a single account; you can use it to do evaluations and give feedback. Takes a string representing the API key. You can get the key on the top of your
account page on https://fuzzy.ai/ .

#### evaluate(agentId, meta, inputMap)

Takes a string representing the agent ID, a boolean to ask for metadata, and a map mapping input names (strings or symbols) to numbers.

#### feedback(evaluation_id, peformance)

Takes a string for the evaluation ID, and a map mapping performance metrics to numbers. You can provide more than one metric.

The metrics will be optimized to increase -- so, number of clicks,
profit on sale, etc. If you have a number that will go towards negative infinity,
multiple it by -1. If you have a number that will go toward zero, give its
inverse (1/x).

## Contributing

1. Fork it ( https://github.com/fuzzy-ai/fuzzy.ai-java/fork )
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request
