import org.apache.log4j.PropertyConfigurator;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Properties;

public class MainClass {
    public static void main(String[] args){
//        Properties log4jProp = new Properties();
//        log4jProp.setProperty("log4j.rootLogger", "WARN");
//        PropertyConfigurator.configure(log4jProp);

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new CurrencyBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
