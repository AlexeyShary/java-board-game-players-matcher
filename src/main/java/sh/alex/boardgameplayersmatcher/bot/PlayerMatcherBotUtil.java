package sh.alex.boardgameplayersmatcher.bot;

import lombok.experimental.UtilityClass;
import sh.alex.boardgameplayersmatcher.user.data.User;

@UtilityClass
public class PlayerMatcherBotUtil {
    public String getWelcomeMessage(String userName) {
        return "Welcome, " + userName + "!" +
                "\nI can help you find a group to play board games with." +
                "\n\nYou can check your profile using the /myinfo command." +
                "\n\nUsing the /activate and /deactivate commands, you can activate or deactivate your profile. If your profile is active, I will send you notifications about upcoming game sessions that you're subscribed to, and display your profile to other users in the subscribers list for the game. You can let me know whether to display a link to your Telegram account in the subscribers list for the game using the /enableShareContact and /disableShareContact commands.";
    }

    public String getUserInfoMessage(User user) {
        return "Display name: " + user.getDisplayName() +
                "\nProfile state: " + (user.getProfileActive() ? "active" : "inactive") +
                "\nContact link: " + getUserContactLink(user) +
                "\nEnable share contact link: " + (user.getShareContactEnabled() ? "enabled" : "disabled");
    }

    public String getHelpMessage() {
        return "Profile Management: " +
                "\n/myinfo - Show information about your profile" +
                "\n/activate - Set your account as active. You will receive notifications about planned game sessions for games you are subscribed to and will be displayed in the subscribers list for the game" +
                "\n/deactivate - Deactivate your account. You can still view information about games and active subscribers" +
                "\n/enableShareContact - Allow displaying a link to your Telegram account" +
                "\n/disableShareContact - Prevent displaying a link to your Telegram account" +
                "\n/rename - Change your display name";
    }

    public String getUserContactLink(User user) {
        return "https://t.me/" + user.getTelegramName();
    }
}
