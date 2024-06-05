package sit.tu_varna.bg.core.operationservice.movie;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.movie.edit.EditMovieOperation;
import sit.tu_varna.bg.api.operation.movie.edit.EditMovieRequest;
import sit.tu_varna.bg.api.operation.movie.edit.EditMovieResponse;
import sit.tu_varna.bg.entity.Genre;
import sit.tu_varna.bg.entity.Movie;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class EditMovieService implements EditMovieOperation {

    @Transactional
    @Override
    public EditMovieResponse process(EditMovieRequest request) {
        UUID movieId = request.getMovieId();
        Movie movie = (Movie) Movie.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + movieId + " not found"));
        if (movie.isPersistent()) {
            movie.setTitle(request.getTitle());
            movie.setDescription(request.getDescription());
            movie.setPosterImageUrl(request.getImageUrl());
            movie.setReleaseYear(request.getReleaseYear());
            movie.setTrailerUrl(request.getTrailerUrl());

            movie.getGenres().stream()
                    .filter(m -> !request.getGenres().contains(m.getId()))
                    .forEach(genre -> {
                        genre.getMovies().remove(movie);
                        genre.persist();
                    });

            movie.getGenres().clear();
            List<Genre> newGenres = request.getGenres().stream()
                    .map(genreId -> {
                        Genre genre = (Genre) Genre.findByIdOptional(genreId)
                                .orElseThrow(() -> new ResourceNotFoundException("Genre with id " + genreId + " not found"));
                        genre.getMovies().add(movie);
                        return genre;
                    })
                    .collect(Collectors.toList());

            movie.getGenres().addAll(newGenres);
            movie.persist();
        }
        return EditMovieResponse.builder().movieId(movie.getId().toString()).build();
    }
}
