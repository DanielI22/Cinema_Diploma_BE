package sit.tu_varna.bg.data.entity;

import jakarta.persistence.*;
import lombok.*;
import sit.tu_varna.bg.data.enums.TokenType;

import java.util.UUID;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Token {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private Boolean revoked;

    private Boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
