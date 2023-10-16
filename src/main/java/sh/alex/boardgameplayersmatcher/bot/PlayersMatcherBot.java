package sh.alex.boardgameplayersmatcher.bot;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import sh.alex.boardgameplayersmatcher.user.data.User;
import sh.alex.boardgameplayersmatcher.user.data.UserState;
import sh.alex.boardgameplayersmatcher.user.logic.UserService;

@Slf4j
@Component
@PropertySource("classpath:application-secret.properties")
@RequiredArgsConstructor
public class PlayersMatcherBot extends TelegramLongPollingBot {
    private final UserService userService;

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            User user = userService.getUserByTelegramUserId(update.getMessage().getFrom().getId());

            if (user == null) {
                user = userService.storeNewUser(update.getMessage().getFrom().getId(),
                        update.getMessage().getChatId(),
                        update.getMessage().getFrom().getUserName());
            }

            String message = update.getMessage().getText();

            switch (user.getState()) {
                case DEFAULT -> handleDefaultStateInput(user, message);
                case RENAMING -> handleRenameStateInput(user, message);
            }
        }
    }

    private void handleDefaultStateInput(User user, String message) {
        switch (message) {
            case "/start" -> handleStartRequest(user);
            case "/myinfo" -> handleInfoRequest(user);
            case "/activate" -> handleSetActiveRequest(user, true);
            case "/deactivate" -> handleSetActiveRequest(user, false);
            case "/enableShareContact" -> handleSetShareLinkEnabledRequest(user, true);
            case "/disableShareContact" -> handleSetShareLinkEnabledRequest(user, false);
            case "/rename" -> handleRenameRequest(user);
            default -> handleUnknownRequest(user);
        }
    }

    private void handleRenameStateInput(User user, String message) {
        String displayName = message.trim();
        userService.patchDisplayName(user, displayName);
        userService.patchUserState(user, UserState.DEFAULT);
        sendResponse(user.getTelegramChatId(), "Okay, from now on, we'll call you " + displayName);
    }

    private void handleStartRequest(User user) {
        sendResponse(user.getTelegramChatId(),
                PlayerMatcherBotUtil.getWelcomeMessage(user.getDisplayName()));
    }

    private void handleInfoRequest(User user) {
        sendResponse(user.getTelegramChatId(),
                PlayerMatcherBotUtil.getUserInfoMessage(user));
    }

    private void handleSetActiveRequest(User user, boolean isActive) {
        userService.patchActive(user, isActive);
        sendResponse(user.getTelegramChatId(),
                PlayerMatcherBotUtil.getUserInfoMessage(user));
    }

    private void handleSetShareLinkEnabledRequest(User user, boolean isEnabled) {
        userService.patchShareLinkEnabled(user, isEnabled);
        sendResponse(user.getTelegramChatId(),
                PlayerMatcherBotUtil.getUserInfoMessage(user));
    }

    private void handleRenameRequest(User user) {
        userService.patchUserState(user, UserState.RENAMING);
        sendResponse(user.getTelegramChatId(), "Tell me how should we call you?");
    }

    private void handleUnknownRequest(User user) {
        sendResponse(user.getTelegramChatId(), PlayerMatcherBotUtil.getHelpMessage());
    }

    private void sendResponse(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
