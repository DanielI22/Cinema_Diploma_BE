package sit.tu_varna.bg.core.operationservice.genre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.genre.delete.DeleteGenreOperation;
import sit.tu_varna.bg.api.operation.genre.delete.DeleteGenreRequest;
import sit.tu_varna.bg.api.operation.genre.delete.DeleteGenreResponse;
import sit.tu_varna.bg.entity.Genre;

@ApplicationScoped
public class DeleteGenreService implements DeleteGenreOperation {

    @Transactional
    @Override
    public DeleteGenreResponse process(DeleteGenreRequest request) {
        Genre genre = (Genre) Genre.findByIdOptional(request.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre does not exist"));
        if (genre.isPersistent()) {
            genre.delete();
        }
        return DeleteGenreResponse.builder().deleted(true).build();
    }
}
