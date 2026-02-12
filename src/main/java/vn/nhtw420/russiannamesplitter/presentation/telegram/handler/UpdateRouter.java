package vn.nhtw420.russiannamesplitter.presentation.telegram.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class UpdateRouter {

    private final CommandHandler commandHandler;
    private final TextMessageHandler textMessageHandler;

    public UpdateRouter(CommandHandler commandHandler, TextMessageHandler textMessageHandler) {
        this.commandHandler = commandHandler;
        this.textMessageHandler = textMessageHandler;
    }

    public void route(Update update, TelegramClient client) {
        if (update == null || !update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText().trim();
        if (text.startsWith("/")) {
            commandHandler.handle(update, client);
            return;
        }

        textMessageHandler.handle(update, client);
    }
}
