package edu.java.bot.services;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.ArrayList;
import java.util.List;

public class CommandService {
    private final LinkService service;

    private final List<Command> commands;

    //здесь исправить работу с командами
    public CommandService(LinkService service) {
        this.service = service;
        commands = new ArrayList<>() {{
            add(new StartCommand());
            add(new HelpCommand());
            add(new TrackCommand(service));
            add(new UntrackCommand(service));
            add(new ListCommand(service));
        }};
    }

    private boolean hasUpdateCorrectMessage(Update update) {
        return update.message() != null && update.message().text() != null;
    }

    private Command identifyCommand(Update update) {
        Command currentCommand = null;
        if (hasUpdateCorrectMessage(update)) {
            String text = update.message().text();
            currentCommand = switch (text) {
                case "/start" -> new StartCommand();
                case "/help" -> new HelpCommand(commands);
                case "/track" -> new TrackCommand(service);
                case "/untrack" -> new UntrackCommand(service);
                case "/list" -> new ListCommand(service);
                default -> currentCommand;
            };
        }
        return currentCommand;
    }

    public SendMessage processUpdate(Update update) {
        Command command = identifyCommand(update);
        if (command != null) {
            return command.handle(update);
        }
        return new SendMessage(update.message().chat().id(), "Команда не распознана!");
    }

}
