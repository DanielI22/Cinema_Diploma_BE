package sit.tu_varna.bg.core.operationservice.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sit.tu_varna.bg.api.operation.movie.externalapi.*;
import sit.tu_varna.bg.core.mapper.MovieMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@ApplicationScoped
public class SearchMoviesService implements SearchMoviesOperation {
    @ConfigProperty(name = "api.key")
    String apiKey;
    @SuppressWarnings("all")
    @Inject
    ObjectMapper objectMapper;
    @Inject
    MovieMapper movieMapper;

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public SearchMoviesResponse process(SearchMoviesRequest request) {
        try {
            String query = URLEncoder.encode(request.getQuery(), StandardCharsets.UTF_8.toString());
            boolean isImdbTag = query.startsWith("tt");
            String imdbTagUri = "https://api.themoviedb.org/3/find/%s?external_source=imdb_id&api_key=%s";
            String searchUri = "https://api.themoviedb.org/3/search/movie?query=%s&api_key=%s";

            String baseUri = isImdbTag ? imdbTagUri : searchUri;
            URI uri = new URI(String.format(baseUri, query, apiKey));

            Collection<MovieApiResult> movieApiResults = fetchMovies(uri, isImdbTag);
            return SearchMoviesResponse.builder()
                    .movies(movieApiResults.stream()
                            .map(movieMapper::movieApiToMovieDto)
                            .collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(500).entity("Error fetching movies: " + e.getMessage()).build());
        }
    }

    private Collection<MovieApiResult> fetchMovies(URI uri, boolean isImdbTag) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).header("accept", "application/json").build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new WebApplicationException(Response.status(response.statusCode()).entity("Error fetching movies").build());
        }

        if (isImdbTag) {
            SearchMoviesImdbTagResult searchMoviesResponse = objectMapper.readValue(response.body(), SearchMoviesImdbTagResult.class);
            return mapToMovieApiResults(searchMoviesResponse.getMovie_results());
        } else {
            SearchMoviesApiResult searchMoviesResponse = objectMapper.readValue(response.body(), SearchMoviesApiResult.class);
            return mapToMovieApiResults(searchMoviesResponse.getResults().stream().limit(4).collect(Collectors.toList()));
        }
    }

    private Collection<MovieApiResult> mapToMovieApiResults(Collection<MovieSearchResult> movieSearchResults) throws Exception {
        Collection<MovieApiResult> movieApiResults = new ArrayList<>();
        for (MovieSearchResult movieSearchResult : movieSearchResults) {
            URI detailsUri = new URI("https://api.themoviedb.org/3/movie/" + movieSearchResult.getId() + "?api_key=" + apiKey + "&append_to_response=videos");
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(detailsUri).header("accept", "application/json").build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                movieApiResults.add(objectMapper.readValue(response.body(), MovieApiResult.class));
            } else {
                throw new WebApplicationException(Response.status(response.statusCode()).entity("Error fetching movie details").build());
            }
        }
        return movieApiResults;
    }
}
