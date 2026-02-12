package vn.nhtw420.russiannamesplitter.presentation.telegram.view;

import vn.nhtw420.russiannamesplitter.application.dto.response.SplitFioResponse;

import java.util.StringJoiner;

public final class ReplyTemplates {

    private ReplyTemplates() {}

    public static final String START = """
            Привет! Я помогу разделить русское ФИО (Фамилия Имя Отчество).
            
            Просто отправь, например:
            Иванов Александр Сергеевич
            
            Команды:
            /help — помощь
            """;

    public static final String HELP = """
            Отправь строку с ФИО (1–3 слова).
            
            Примеры:
            • Иванов Александр Сергеевич
            • Иванов Александр
            • Александр
            
            Я также попробую подсказать короткое имя (например: Саша).
            """;

    public static final String UNKNOWN_COMMAND = "Неизвестная команда. Напиши /help.";
    public static final String BAD_INPUT = "Я не вижу текста. Отправь ФИО строкой \uD83D\uDE42";
    public static final String ERROR = "Упс! Ошибка на моей стороне. Попробуй ещё раз позже.";

    public static String formatSplitResult(SplitFioResponse response) {
        StringJoiner stringJoiner = new StringJoiner("\n");

        stringJoiner.add("Фамилия: " + nullToDash(response.surname()));
        stringJoiner.add("Имя: " + nullToDash(response.givenName()));
        stringJoiner.add("Отчество: " + nullToDash(response.patronymic()));

        if (response.shortNames() != null && !response.shortNames().isEmpty()) {
            stringJoiner.add("Короткое имя: " + String.join(", ", response.shortNames()));
        }

        stringJoiner.add("Confidence: " + String.format("%.2f", response.confidence()));
        return stringJoiner.toString();
    }

    private static String nullToDash(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }
}
