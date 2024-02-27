package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


@Component
public class HelpCommand implements Command {
    private List<Command> commands;

    public HelpCommand() {
    }

    public HelpCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "вывести окно с командами";
    }

    @Override
    public SendMessage handle(Update update) {
        String allCommandsDescription = commands.stream()
            .map((command) -> command.command() + " - " + command.description())
            .collect(Collectors.joining("\n"));
        return new SendMessage(update.message().chat().id(), "Список доступных команд:\n"
            + allCommandsDescription);
    }
}
