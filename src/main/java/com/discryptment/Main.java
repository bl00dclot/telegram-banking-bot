package com.discryptment;

import com.discryptment.bot.BankingBot;
import com.discryptment.bot.SessionManager;
import com.discryptment.bot.conversation.ConversationManager;
import com.discryptment.bot.conversation.ConversationRouter;
import com.discryptment.commands.CancelCommand;
import com.discryptment.commands.CommandContext;
import com.discryptment.commands.CommandDispatcher;
import com.discryptment.commands.user.auth.LoginCommand;
import com.discryptment.commands.user.profile.AddGoldCommand;
import com.discryptment.commands.user.profile.ProfileCommand;
import com.discryptment.commands.StartCommand;
import com.discryptment.commands.admin.vault.SetVaultCommand;
import com.discryptment.db.Database;
import com.discryptment.db.dao.ConfigDao;
import com.discryptment.db.dao.UserDao;
import com.discryptment.db.dao.impl.ConfigDaoImpl;
import com.discryptment.db.dao.impl.UserDaoImpl;
import com.discryptment.service.AdminService;
import com.discryptment.service.AuthService;
import com.discryptment.service.UserService;
import com.discryptment.util.EnvReader;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        EnvReader.init("src/main/resources", ".env");
        String botToken = EnvReader.get("BOT_TOKEN");

        //Start Polling
        TelegramBotsLongPollingApplication botsApi = new TelegramBotsLongPollingApplication();
        // Using try-with-resources to allow autoclose to run upon finishing
        try {
            //DB init
            Database.init();

            // DAO init
            UserDao userDao = new UserDaoImpl();
            ConfigDao configDao = new ConfigDaoImpl();

            // Services init
            UserService userService = new UserService(userDao, configDao);
            AuthService authService = new AuthService();
            AdminService adminService = new AdminService();

            //Conversation init
            ConversationManager convMgr = new ConversationManager(30);
            ConversationRouter convRouter = new ConversationRouter(authService, userService, adminService);

            //Session init
            SessionManager sessionManager = new SessionManager();

            // Bot init
            BankingBot bot = new BankingBot(botToken);
            CommandContext ctx = new CommandContext(bot, convMgr, userService, authService);
            CommandDispatcher dispatcher = new CommandDispatcher(adminService.adminList(), convMgr, convRouter, sessionManager);


            //Register cmds
            dispatcher.register(new StartCommand());
            dispatcher.register(new CancelCommand());
            dispatcher.register(new LoginCommand(convMgr, 3));
            dispatcher.register(new ProfileCommand());
            dispatcher.register(new AddGoldCommand());
            dispatcher.register(new SetVaultCommand(convMgr));


            //Inject in bot
            bot.setDispatcher(dispatcher);
            bot.setContext(ctx);

            // Register bot
            botsApi.registerBot(botToken, bot);
            System.out.println("Banking bot started");
            // Ensure this prcess wait forever
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}