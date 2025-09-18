package com.discryptment.commands.user.auth;

import com.discryptment.bot.conversation.ConversationInterface;
import com.discryptment.bot.conversation.ConversationManager;
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

	public boolean startsConversation() {
		return true;
	}

	@Override
	public void execute(Message msg, String[] args, CommandContext ctx) throws Exception {

		long tgId = msg.getFrom().getId();
		String username = msg.getFrom().getUserName();
		
		// Start login conversation (inject services needed)
		LoginConversation conv = new LoginConversation(tgId, authService, userService);
		convMgr.startConversation(tgId, conv, ctx, msg.getChatId());

		System.out.println(username + " is logging");

	}
}
