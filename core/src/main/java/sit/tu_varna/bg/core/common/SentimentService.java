package sit.tu_varna.bg.core.common;

import com.vader.sentiment.analyzer.SentimentAnalyzer;
import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.enums.Sentiment;

import java.io.IOException;
import java.util.Map;

@ApplicationScoped
public class SentimentService {
    public Sentiment analyzeSentiment(String text) throws IOException {
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer(text);
        sentimentAnalyzer.analyze();
        Map<String, Float> polarity = sentimentAnalyzer.getPolarity();
        Float compound = polarity.get("compound");
        return detectEmotion(compound);
    }

    private Sentiment detectEmotion(float compoundScore) {
        if (compoundScore >= 0.6) {
            return Sentiment.HAPPY;
        } else if (compoundScore >= 0.05) {
            return Sentiment.POSITIVE;
        } else if (compoundScore <= -0.6) {
            return Sentiment.ANGRY;
        } else if (compoundScore <= -0.05) {
            return Sentiment.NEGATIVE;
        } else {
            return Sentiment.NEUTRAL;
        }
    }
}
