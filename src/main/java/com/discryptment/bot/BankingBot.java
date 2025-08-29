package com.discryptment.bot;

import com.discryptment.commands.CommandContext;
import com.discryptment.commands.CommandDispatcher;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class BankingBot implements LongPollingSingleThreadUpdateConsumer {
    private TelegramClient telegramClient;
    private CommandDispatcher dispatcher;
    private CommandContext context;

    public BankingBot(String token) {
        this.telegramClient = new OkHttpTelegramClient(token);
    }
    public void setDispatcher(CommandDispatcher d) { this.dispatcher = d; }
    public void setContext(CommandContext c) { this.context = c; }

    @Override
    public void consume(Update update) {
        // first let dispatcher handle commands; if it returns false you can do other handling
        boolean handled = dispatcher.dispatch(update, context);
        if (!handled) {
            // non-command messages (conversation states, inline queries etc.)
        }
    }
    public void sendText(long chatId, String text) {
        SendMessage message = SendMessage // Create a message object
                    .builder()
                    .chatId(chatId)
                    .text(text)
                    .build();
        SendMessage out = new SendMessage(String.valueOf(chatId), text);
        try { telegramClient.execute(message); } catch (TelegramApiException e) { e.printStackTrace(); }
    }
//    @Override
//    public void consume(Update update) {
//        // We check if the update has a message and the message has text
////        if (update.hasMessage() && update.getMessage().hasText()) {
////            // Set variables
////            String message_text = update.getMessage().getText();
////            long chat_id = update.getMessage().getChatId();
////            long telegramId = update.getMessage().getFrom().getId();
////            String username = update.getMessage().getFrom().getUserName();
////
////            System.out.println(chat_id);
//////            System.out.println(telegramId);
////            System.out.println(message_text);
//
//
//
////            if (message_text.equals("/start")) {
////
////
////                //Build User POJO
////                User user = new User();
////                user.setTelegramId(telegramId);
////                user.setUsername(username);
////                user.setGoldBalance(0.0);
////                user.setExpectedUsdTotal(0.0);
////                user.setRealUsdBalance(0.0);
////                user.setAuthorized(true);
////                System.out.println(user.getUsername());
////
////                userService.registerUser(user);
//////            SendMessage message = SendMessage // Create a message object
//////                    .builder()
//////                    .chatId(chat_id)
//////                    .text(message_text)
//////                    .build();
////                SendMessage msg = SendMessage
////                        .builder()
////                        .chatId(chat_id)
////                        .text("Welcome " + user.getUsername())
////                        .build();
//                try {
//                    telegramClient.execute(msg); // Sending our message object to user
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

}

