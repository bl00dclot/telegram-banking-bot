package com.discryptment;

import com.discryptment.bot.BankingBot;
import com.discryptment.db.Database;
import com.discryptment.db.dao.UserDao;
import com.discryptment.db.dao.UserDaoImpl;
import com.discryptment.service.UserService;
import com.discryptment.util.EnvReader;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import java.io.File;
import java.sql.Connection;


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

            Database.init();
            UserDao userDao = new UserDaoImpl();
            UserService userService = new UserService(userDao);
            BankingBot bot = new BankingBot(botToken, userService, userDao);
            botsApi.registerBot(botToken, bot);
            System.out.println("Banking bot started");
            // Ensure this prcess wait forever
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}