package com.discryptment.bot.conversation;

import com.discryptment.commands.CommandContext;
import com.discryptment.model.User;
import com.discryptment.service.AdminService;
import com.discryptment.service.AuthService;
import com.discryptment.service.UserService;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.sql.SQLException;

public class ConversationRouter {
	    public boolean handleMessage(Message msg, ConversationInterface conv, CommandContext ctx, ConversationManager convMgr) {
	        try {
	            boolean handled = conv.handleMessage(msg, ctx, convMgr);
	            // If conversation finished, remove it
	            if (conv.getStatus() != ConversationInterface.Status.ACTIVE) {
	                convMgr.endConversation(conv.getOwnerTelegramId());
	            } else {
	                convMgr.touch(conv.getOwnerTelegramId());
	            }
	            return handled;
	        } catch (Exception e) {
	            e.printStackTrace();
	            ctx.bot.sendText(msg.getChatId(), "Internal error while processing your reply. Try /cancel and start again.");
	            convMgr.endConversation(conv.getOwnerTelegramId()); // avoid stuck flow
	            return true;
	        }
	    }
	

//    private final AuthService authService;
//    private final UserService userService;
//    private final AdminService adminService;
//
//    public ConversationRouter(AuthService authService, UserService userService, AdminService adminService) {
//        this.authService = authService;
//        this.userService = userService;
//        this.adminService = adminService;
//    }
//
//    public void handleMessage(Message message, Conversation conv, CommandContext ctx, ConversationManager convMgr) throws Exception {
//        if (conv.getState() == ConversationState.AWAITING_PASSWORD) {
//            handlePassword(message, conv, ctx, convMgr);
//            return;
//        }
//        if (conv.getState() == ConversationState.SETTING_VAULT) {
//            handleSettingVault(message, conv, ctx, convMgr);
//            return;
//        }
//    }
//
//    public void handlePassword(Message message, Conversation conv, CommandContext ctx, ConversationManager convMgr) throws SQLException {
//        long tgId = conv.getTelegramId();
//        long chatId = message.getChatId();
//        String attempt = message.getText().trim();
//        String username = message.getFrom().getUserName();
//
//        boolean ok = ctx.authService.verifyPassword(attempt);
//
//        if (ok) {
//            // mark user as authorized in DB
//
//            //Build User POJO
//            User user = new User();
//            user.setTelegramId(tgId);
//            user.setUsername(username);
//            user.setGoldBalance(0.0);
//            user.setExpectedUsdTotal(0.0);
//            user.setRealUsdBalance(0.0);
//            user.setAuthorized(false);
//            System.out.println(user.getUsername() + " is logged in");
//
//            userService.registerUser(user);
//            authService.markUserAuthorizedByTelegramId(tgId, true);
//            convMgr.endConversation(tgId);
//            ctx.bot.sendText(chatId, "✅ Authorized. You can use protected commands.");
//        } else {
//            conv.setAttemptsLeft(conv.getAttemptsLeft() - 1);
//            if (conv.getAttemptsLeft() <= 0) {
//                convMgr.endConversation(tgId);
//                // Optional: temporary block recorded in DB
//                ctx.bot.sendText(chatId, "Too many failed attempts. Contact admin.");
//            } else {
//                ctx.bot.sendText(chatId, "Wrong password. Attempts left: " + conv.getAttemptsLeft() + ". Try again or send /cancel.");
//                // conversation remains active
//            }
//        }
//    }
//    public void handleSettingVault(Message message, Conversation conv, CommandContext ctx, ConversationManager convMgr) throws SQLException {
//        long tgId = message.getFrom().getId();
//        long chatId = message.getChatId();
//
//        //check if user is admin
//        ctx.bot.sendText(chatId, "exiting");
//
//        convMgr.endConversation(tgId);
//
//        }
}
