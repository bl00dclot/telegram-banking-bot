package com.discryptment.commands;

import java.util.Comparator;
import java.util.Map;

import org.telegram.telegrambots.meta.api.objects.message.Message;

import com.discryptment.service.AdminService;

public class HelpCommand implements BotCommand {
	private final Map<String, BotCommand> commands;
	private final AdminService adminService;

	public HelpCommand(Map<String, BotCommand> commands, AdminService adminService) {
		this.commands = commands;
		this.adminService = adminService;
	}

	@Override
	public String name() {
		return "/help";
	}

	@Override
	public String description() {
		return "Shows this message";
	}

	@Override
	public void execute(Message msg, String[] args, CommandContext ctx) {
		StringBuilder sb = new StringBuilder("Available commands:\n");
		boolean isAdmin = adminService.adminList().contains((msg.getFrom().getId()));
		commands.values().stream().filter(c -> isAdmin || !c.adminOnly()) // hide admin-only for non-admins
				.sorted(Comparator.comparing(BotCommand::name)).forEach(c -> sb.append(c.name()).append(" — ")
						.append(c.description().isEmpty() ? "no description" : c.description()).append("\n"));
		ctx.bot.sendText(msg.getChatId(), sb.toString());
	}
}
