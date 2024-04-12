package sit.tu_varna.bg.core.operationservice.genre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.genre.edit.EditGenreOperation;
import sit.tu_varna.bg.api.operation.genre.edit.EditGenreRequest;
import sit.tu_varna.bg.api.operation.genre.edit.EditGenreResponse;
import sit.tu_varna.bg.entity.Genre;

@ApplicationScoped
public class EditGenreService implements EditGenreOperation {

    @Transactional
    @Override
    public EditGenreResponse process(EditGenreRequest request) {
        Genre genre = (Genre) Genre.findByIdOptional(request.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre does not exist"));
        if (genre.isPersistent()) {
            genre.setName(request.getName());
            genre.persist();
        }
        return EditGenreResponse.builder().genreId(genre.getId().toString()).build();
    }
}
