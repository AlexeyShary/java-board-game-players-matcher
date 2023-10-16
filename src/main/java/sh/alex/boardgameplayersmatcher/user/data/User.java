package sh.alex.boardgameplayersmatcher.user.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_telegram_user_id")
    private Long telegramUserId;

    @Column(name = "user_telegram_chat_id")
    private Long telegramChatId;

    @Column(name = "user_telegram_name")
    private String telegramName;

    @Column(name = "user_display_name")
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_state")
    private UserState state;

    @Column(name = "user_share_contact_enabled")
    private Boolean shareContactEnabled;

    @Column(name = "user_profile_active")
    private Boolean profileActive;
}