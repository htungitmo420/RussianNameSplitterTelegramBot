package vn.nhtw420.russiannamesplitter.presentation.telegram.view;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class ReplyFactory {

    public void sendText(TelegramClient client, long chatId, String text) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            client.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
