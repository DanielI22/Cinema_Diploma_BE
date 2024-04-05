package sit.tu_varna.bg.core.operationservice.movie;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.movie.add.AddMovieOperation;
import sit.tu_varna.bg.api.operation.movie.add.AddMovieRequest;
import sit.tu_varna.bg.api.operation.movie.add.AddMovieResponse;
import sit.tu_varna.bg.entity.Genre;
import sit.tu_varna.bg.entity.Movie;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class AddMovieService implements AddMovieOperation {

    @Transactional
    @Override
    public AddMovieResponse process(AddMovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .posterImageUrl(request.getImageUrl())
                .releaseYear(request.getReleaseYear())
                .duration(request.getDuration())
                .trailerUrl(request.getTrailerUrl())
                .build();

        List<Genre> newGenres = request.getGenres().stream()
                .map(genreDto -> {
                    Genre genre;
                    if (genreDto.getId() != null) {
                        genre = (Genre) Genre.findByIdOptional(UUID.fromString(genreDto.getId()))
                                .orElseThrow(() -> new ResourceNotFoundException("Invalid genre id: " + genreDto.getId()));
                    } else {
                        genre = (Genre) Genre.find("name", genreDto.getName()).firstResultOptional()
                                .orElseGet(() -> {
                                    Genre newGenre = new Genre();
                                    newGenre.setName(genreDto.getName());
                                    newGenre.persist();
                                    return newGenre;
                                });
                    }
                    genre.getMovies().add(movie);
                    return genre;
                })
                .collect(Collectors.toList());

        movie.getGenres().addAll(newGenres);
        movie.persist();

        return AddMovieResponse.builder()
                .movieId(movie.getId().toString())
                .build();
    }
}
