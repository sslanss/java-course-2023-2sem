package edu.java.commands.impls;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.commands.Command;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {

    private final String allCommands;

    public HelpCommand(List<Command> commands) {
        commands.add(this);
        allCommands = commands.stream()
            .map((command) -> command.command() + " - " + command.description())
            .sorted(String::compareTo)
            .collect(Collectors.joining("\n"));
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "вывести спиок доступных команд";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Список доступных команд:\n"
            + allCommands);
    }
}
