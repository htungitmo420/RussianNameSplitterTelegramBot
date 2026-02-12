package vn.nhtw420.russiannamesplitter.presentation.telegram.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import vn.nhtw420.russiannamesplitter.presentation.telegram.view.ReplyFactory;
import vn.nhtw420.russiannamesplitter.presentation.telegram.view.ReplyTemplates;

@Component
public class CommandHandler {

    private final ReplyFactory replyFactory;

    public CommandHandler(ReplyFactory replyFactory) {
        this.replyFactory = replyFactory;
    }

    public void handle(Update update, TelegramClient client) {
        String text = update.getMessage().getText().trim();
        String cmd = text.split("\\s+")[0];

        long chatId = update.getMessage().getChatId();

        switch (cmd) {
            case "/start" -> replyFactory.sendText(client, chatId, ReplyTemplates.START);
            case "/help" -> replyFactory.sendText(client, chatId, ReplyTemplates.HELP);
            default -> replyFactory.sendText(client, chatId, ReplyTemplates.UNKNOWN_COMMAND);
        }
    }
}
