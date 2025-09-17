package com.discryptment.commands.user.auth;

import com.discryptment.bot.conversation.Conversation;
import com.discryptment.bot.conversation.ConversationInterface;
import com.discryptment.bot.conversation.ConversationManager;
import com.discryptment.bot.conversation.ConversationState;
import com.discryptment.commands.BotCommand;
import com.discryptment.commands.CommandContext;
import com.discryptment.service.AuthService;
import com.discryptment.service.UserService;

import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.HashMap;

public class LoginCommand implements BotCommand {
	private final ConversationManager convMgr;
	private final AuthService authService;
	private final UserService userService;
	public LoginCommand(ConversationManager convMgr, AuthService authService, UserService userService) {
		this.convMgr = convMgr;
		this.authService = authService;
		this.userService = userService;
	}

	@Override
	public String name() {
		return "/login";
	}

	@Override
	public String description() {
		return "Login - start login flow";
	}

	@Override
	public void execute(Message msg, String[] args, CommandContext ctx) throws Exception {

		long tgId = msg.getFrom().getId();
		String username = msg.getFrom().getUserName();

		// If already in conversation, inform user
		// !!! TEMPORARY FIX
		ConversationInterface existing = convMgr.getConversation(tgId);
		if (existing != null) {
			ctx.bot.sendText(msg.getChatId(), "You are already in a conversation. Use /cancel to stop it first.");
			return;
		}

		// Start login conversation (inject services needed)
		LoginConversation conv = new LoginConversation(tgId, authService, userService);
		convMgr.startConversation(tgId, conv, ctx);

		System.out.println(username + " is logging");

//        long chatId = message.getChatId();
//
//
//        Conversation conv = new Conversation();
//        conv.setTelegramId(telegramId);
//        conv.setState(ConversationState.AWAITING_PASSWORD);
//        conv.setAttemptsLeft(maxAttempts);
//        conv.setStartedAt(System.currentTimeMillis());
//        conv.setData(new HashMap<>());
//
//        convMgr.startConversation(telegramId, conv);
//        System.out.println(username + " is logging");
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("Enter password (you have ").append(maxAttempts).append(" attempts)");
//
//        ctx.bot.sendText(chatId, sb.toString());
	}
}
