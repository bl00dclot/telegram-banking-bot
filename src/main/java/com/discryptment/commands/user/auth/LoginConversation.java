package com.discryptment.commands.user.auth;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.meta.api.objects.message.Message;

import com.discryptment.bot.conversation.ConversationInterface;
import com.discryptment.bot.conversation.ConversationManager;
import com.discryptment.commands.CommandContext;
import com.discryptment.model.User;
import com.discryptment.service.AuthService;
import com.discryptment.service.UserService;

public class LoginConversation implements ConversationInterface {
	private static final int MAX_ATTEMPTS = 3;

	private final long tgId;
	private final AuthService authService;
	private final UserService userService;

	private volatile Status status = Status.ACTIVE;
	private int attemptsLeft = MAX_ATTEMPTS;

	public LoginConversation(long tgId, AuthService authService, UserService userService) {
		this.tgId = tgId;
		this.authService = authService;
		this.userService = userService;
		
	}

	@Override
	public void onStart(CommandContext ctx) {
		ctx.bot.sendText(tgId, "Please enter your password. You have" + attemptsLeft + " attempts. /cancel to exit");
	}

	@Override
	public boolean handleMessage(Message msg, CommandContext ctx, ConversationManager convMgr) {
		String text = msg.getText() == null ? "" : msg.getText().trim();
		String username = msg.getFrom().getUserName();

		// Allow /cancel to abort

		if (text.equalsIgnoreCase("/cancel")) {
			ctx.bot.sendText(msg.getChatId(), "Login cancelled");
		}

		// Other slash commands should be handled by CommandDispatcher
		// Treat empty input

		if (text.isEmpty()) {
			ctx.bot.sendText(msg.getChatId(), "Password cannot be empty. Please enter password or /cancel");
			return true; // do not consume attempt
		}

		try {
			boolean ok = ctx.authService.verifyPassword(text);

			if (ok) {
				
				// Success: reset attempts, build user POJO, mark user authenticated
				User user = new User();
				user.setTelegramId(tgId);
				user.setUsername(username);
				user.setGoldBalance(0.0);
				user.setExpectedUsdTotal(0.0);
				user.setRealUsdBalance(0.0);
				user.setAuthorized(false);
				System.out.println(user.getUsername() + " is logged in");

				userService.registerUser(user);
				authService.markUserAuthorizedByTelegramId(tgId, true);
				
                ctx.bot.sendText(msg.getChatId(), "✅ Login successful.");

                status = Status.COMPLETED;
                return true;
			} else {
				//Failed attempt
				attemptsLeft--;
				// TODO: failed password attempt should be recorded in DB
				if (attemptsLeft > 0) {
                    ctx.bot.sendText(msg.getChatId(), "❌ Incorrect password. Attempts remaining: " + attemptsLeft + ". Try again or send /cancel to abort.");
                    return true;
				} else {
					// No attempts left: lockout and finish conversation
					// TODO: implement lockout
                    ctx.bot.sendText(msg.getChatId(), "⛔ Too many failed attempts. Login disabled for a while. Contact an admin if you need help.");
                    status = Status.FAILED;
                    return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
            ctx.bot.sendText(msg.getChatId(), "Internal error while verifying credentials. Please try again later.");
            status = Status.FAILED;
            return true;
		}

	}
	@Override public Status getStatus() { return status; }
    @Override public long getOwnerTelegramId() { return tgId; }

    @Override
    public void onCancel(CommandContext ctx) {
        ctx.bot.sendText(tgId, "Login cancelled.");
    }

    @Override
    public Map<String, Object> snapshotState() {
        Map<String,Object> m = new HashMap<>();
        m.put("attemptsLeft", attemptsLeft);
        m.put("status", status.name());
        return m;
    }
}
