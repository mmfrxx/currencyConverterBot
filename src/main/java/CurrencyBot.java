import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CurrencyBot extends TelegramLongPollingBot {
    public void sendMsg(Message message, String to){
        SendMessage reply = new SendMessage();
        reply.enableMarkdown(true);
        reply.setChatId(message.getChatId());
        reply.setReplyToMessageId(message.getMessageId());

        try {
            String rate = getRate(to);
            if(rate.equals("0.0")) reply.setText("Not valid currency.");
            else reply.setText(getRate(to));
        } catch (IOException e) {
            reply.setText("Error occurred.");
        }

        try {
            execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getRate(String to) throws IOException{
        URL url = new URL("https://free.currconv.com/api/v7/convert?q=" + to
                        + "_KZT&compact=ultra&apiKey=1f1bc6ea49c88e45ac54");
        Scanner output = new Scanner((InputStream) url.getContent());
        String result = "";
        while (output.hasNext())
            result+= output.nextLine();

        JSONObject object = new JSONObject(result);
        double rate = 0.0;
        if(object.has(to + "_KZT"))
            rate = object.getDouble(to + "_KZT");
        return Double.toString(rate);
    }

    public void sendHelpOrStart(Message msg,Boolean start){
        SendMessage reply = new SendMessage();
        reply.enableMarkdown(true);
        reply.setChatId(msg.getChatId());
        reply.setReplyToMessageId(msg.getMessageId());
        if(!start)
            reply.setText("Please choose the currency.");
        else reply.setText("Hello! Let's check the exchange rate of tenge.");
        try {
            setButtons(reply);
            execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage reply){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        reply.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("USD"));
        keyboardFirstRow.add(new KeyboardButton("RUB"));
        keyboardFirstRow.add(new KeyboardButton("EUR"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }


    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
        Message message = update.getMessage();
        if(message!= null && message.hasText()){
            String text = message.getText();
            if ("/start".equals(text)) {
                sendHelpOrStart(message, true);
            } else if ("/help".equals(text)) {
                sendHelpOrStart(message, false);
            } else {
                sendMsg(message, message.getText());
            }
        }
    }

    public String getBotUsername() {
        return "MyForJobCurrencyBot";
    }

    public String getBotToken() {
        return "1093595836:AAFxkZl7I0kGCF43u781yw4ByCd8z4V5O5I";
    }
}
