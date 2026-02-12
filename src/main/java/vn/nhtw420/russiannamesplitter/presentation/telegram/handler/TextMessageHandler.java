package vn.nhtw420.russiannamesplitter.presentation.telegram.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import vn.nhtw420.russiannamesplitter.application.dto.request.SplitFioRequest;
import vn.nhtw420.russiannamesplitter.application.dto.response.SplitFioResponse;
import vn.nhtw420.russiannamesplitter.application.usecase.SplitFioUseCase;
import vn.nhtw420.russiannamesplitter.common.exception.BadInputException;
import vn.nhtw420.russiannamesplitter.presentation.telegram.view.ReplyFactory;
import vn.nhtw420.russiannamesplitter.presentation.telegram.view.ReplyTemplates;

@Component
public class TextMessageHandler {

    private final SplitFioUseCase splitFioUseCase;
    private final ReplyFactory replyFactory;

    public TextMessageHandler(SplitFioUseCase splitFioUseCase, ReplyFactory replyFactory) {
        this.splitFioUseCase = splitFioUseCase;
        this.replyFactory = replyFactory;
    }

    public void handle(Update update, TelegramClient client) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        try {
            SplitFioResponse response = splitFioUseCase.execute(new SplitFioRequest(text));
            String reply = ReplyTemplates.formatSplitResult(response);
            replyFactory.sendText(client, chatId, reply);
        } catch (BadInputException ex) {
            replyFactory.sendText(client, chatId, ReplyTemplates.BAD_INPUT);
        } catch (Exception ex) {
            ex.printStackTrace();
            replyFactory.sendText(client, chatId, ReplyTemplates.ERROR);
        }
    }
}
