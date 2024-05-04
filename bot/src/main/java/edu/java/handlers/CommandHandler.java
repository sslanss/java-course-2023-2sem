package edu.java.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.commands.Command;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {
    private final Map<String, Command> commands;

    @Getter @Setter private Command lastPulledCommand;

    public CommandHandler(List<Command> commands) {
        this.commands = commands.stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));
    }

    private boolean hasUpdateCorrectMessage(Update update) {
        return update.message() != null && update.message().text() != null;
    }

    private Command identifyCommand(Update update) {
        if (hasUpdateCorrectMessage(update)) {
            return commands.get(update.message().text());
        }
        return null;
    }

    public SendMessage tryProcessCommand(Update update) {
        if (hasUpdateCorrectMessage(update)) {
            lastPulledCommand = identifyCommand(update);
            if (lastPulledCommand != null) {
                return lastPulledCommand.handle(update);
            }
        }
        return new SendMessage(update.message().chat().id(), "Команда не распознана!");
    }

}
