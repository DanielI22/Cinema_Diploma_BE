package sit.tu_varna.bg.core.operationservice.genre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.operation.genre.add.AddGenreOperation;
import sit.tu_varna.bg.api.operation.genre.add.AddGenreRequest;
import sit.tu_varna.bg.api.operation.genre.add.AddGenreResponse;
import sit.tu_varna.bg.entity.Genre;

@ApplicationScoped
public class AddGenreService implements AddGenreOperation {

    @Transactional
    @Override
    public AddGenreResponse process(AddGenreRequest request) {
        Genre genre = Genre.builder()
                .name(request.getName())
                .build();

        genre.persist();

        return AddGenreResponse.builder()
                .genreId(genre.getId().toString())
                .build();
    }
}
