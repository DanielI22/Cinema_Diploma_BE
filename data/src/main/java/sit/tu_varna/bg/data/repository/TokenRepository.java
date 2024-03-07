package sit.tu_varna.bg.data.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sit.tu_varna.bg.data.entity.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query(value = """
        select t from Token t join User u
        on t.user.id = u.id
        where u.id = :id and t.revoked = false
    """)
    List<Token> findAllValidTokenByUser(UUID id);

    Optional<Token> findByToken(String token);

    @Transactional
    @Modifying
    @Query("delete from Token t where t.revoked = true ")
    void deleteRevokedTokens();
}
