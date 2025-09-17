package com.discryptment.bot.conversation;

import java.util.Map;

import org.telegram.telegrambots.meta.api.objects.message.Message;

import com.discryptment.commands.CommandContext;

public interface ConversationInterface {
	enum Status {
		ACTIVE, COMPLETED, CANCELLED, FAILED
	}

	// Called whenever a message arrives for this conversation.
	// Return true if the message was handled by the conversation.
	boolean handleMessage(Message msg, CommandContext ctx, ConversationManager convMgr);

	// Optional: invoked when conversation is created to send the first prompt.
	default void onStart(CommandContext ctx) {
	}

	// Optional: called by manager when conversation times out or user cancels.
	default void onCancel(CommandContext ctx) {
	}

	Status getStatus();

	long getOwnerTelegramId(); // owner to route messages

	// Optional metadata for persistence/debugging
	Map<String, Object> snapshotState();
}
