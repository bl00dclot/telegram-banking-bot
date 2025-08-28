package com.discryptment;

import com.discryptment.bot.BankingBot;
import com.discryptment.commands.CommandContext;
import com.discryptment.commands.CommandDispatcher;
import com.discryptment.commands.user.profile.AddGoldCommand;
import com.discryptment.commands.user.profile.ProfileCommand;
import com.discryptment.commands.user.StartCommand;
import com.discryptment.db.Database;
import com.discryptment.db.dao.UserDao;
import com.discryptment.db.dao.UserDaoImpl;
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

            // Services init
            UserService userService = new UserService(userDao);

            // Bot init
            BankingBot bot = new BankingBot(botToken);
            CommandContext ctx = new CommandContext(bot, userService);
            CommandDispatcher dispatcher = new CommandDispatcher(/*adminIdFromConfig*/ 123456789L);


            //Register cmds
            dispatcher.register(new StartCommand());
            dispatcher.register(new ProfileCommand());
            dispatcher.register(new AddGoldCommand());

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