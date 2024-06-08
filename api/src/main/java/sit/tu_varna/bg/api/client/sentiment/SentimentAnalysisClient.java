package sit.tu_varna.bg.api.client.sentiment;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/analyze")
@RegisterRestClient(configKey = "sentiment-analysis-api")
public interface SentimentAnalysisClient {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    SentimentResponse analyze(SentimentRequest request);
}
