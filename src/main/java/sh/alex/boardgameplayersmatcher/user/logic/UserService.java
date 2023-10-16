package sh.alex.boardgameplayersmatcher.user.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.alex.boardgameplayersmatcher.user.data.User;
import sh.alex.boardgameplayersmatcher.user.data.UserRepository;
import sh.alex.boardgameplayersmatcher.user.data.UserState;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserByTelegramUserId(Long telegramId) {
        return userRepository.getUserByTelegramUserId(telegramId);
    }

    @Transactional
    public User storeNewUser(Long telegramUserId, Long telegramChatId, String telegramName) {
        User user = new User();
        user.setTelegramUserId(telegramUserId);
        user.setTelegramChatId(telegramChatId);
        user.setTelegramName(telegramName);
        user.setDisplayName(telegramName);
        user.setState(UserState.DEFAULT);
        user.setShareContactEnabled(false);
        user.setProfileActive(false);

        return userRepository.save(user);
    }

    @Transactional
    public void patchActive(User user, boolean isActive) {
        user.setProfileActive(isActive);
        userRepository.save(user);
    }

    @Transactional
    public void patchShareLinkEnabled(User user, boolean isEnabled) {
        user.setShareContactEnabled(isEnabled);
        userRepository.save(user);
    }

    @Transactional
    public void patchUserState(User user, UserState userState) {
        user.setState(userState);
        userRepository.save(user);
    }

    @Transactional
    public void patchDisplayName(User user, String userDisplayName) {
        user.setDisplayName(userDisplayName);
        userRepository.save(user);
    }
}