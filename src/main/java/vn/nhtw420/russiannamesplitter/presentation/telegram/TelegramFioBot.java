package vn.nhtw420.russiannamesplitter.presentation.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import vn.nhtw420.russiannamesplitter.config.TelegramBotProperties;
import vn.nhtw420.russiannamesplitter.presentation.telegram.handler.UpdateRouter;

@Component
public class TelegramFioBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramBotProperties properties;
    private final TelegramClient telegramClient;
    private final UpdateRouter updateRouter;

    public TelegramFioBot(TelegramBotProperties properties, UpdateRouter updateRouter) {
        this.properties = properties;
        this.telegramClient = new OkHttpTelegramClient(properties.token());
        this.updateRouter = updateRouter;
    }

    @Override
    public String getBotToken() {
        return properties.token();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        updateRouter.route(update, telegramClient);
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Bot registered, Running: " + botSession.isRunning());
    }
}
