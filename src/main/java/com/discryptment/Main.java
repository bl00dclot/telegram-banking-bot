package com.discryptment;
import com.discryptment.db.Database;
import com.discryptment.util.EnvReader;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Database.initSchema();
        EnvReader.init("src/main/resources", ".env");
        String botToken = EnvReader.get("BOT_TOKEN");
        // Using try-with-resources to allow autoclose to run upon finishing
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
//            botsApplication.registerBot(botToken, new MyAmazingBot(botToken));
//            System.out.println("MyAmazingBot successfully started!");
            // Ensure this prcess wait forever
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("Let the uprising begin");
        }
    }