package sit.tu_varna.bg.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesOperation;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesRequest;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesResponse;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final GetAllMoviesOperation moviesOperation;

    @GetMapping("/movies")
    public ResponseEntity<GetAllMoviesResponse> getMovies() {
        return new ResponseEntity<>(moviesOperation.process(new GetAllMoviesRequest()), HttpStatus.OK);
    }
}
