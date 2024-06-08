package sit.tu_varna.bg.core.common;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import sit.tu_varna.bg.api.client.sentiment.SentimentAnalysisClient;
import sit.tu_varna.bg.api.client.sentiment.SentimentRequest;
import sit.tu_varna.bg.api.client.sentiment.SentimentResponse;

@ApplicationScoped
public class SentimentAnalysisService {
    @Inject
    @RestClient
    SentimentAnalysisClient sentimentAnalysisClient;

    public String analyzeSentiment(String text) {
        SentimentRequest request = new SentimentRequest(text);
        SentimentResponse response = sentimentAnalysisClient.analyze(request);
        return response.getSentiment();
    }
}
