package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.HashSet;
import java.util.Set;

public class CommandService {
    private final Set<Command> commands;

    public CommandService() {
        this.commands = new HashSet<>() {{
            add(new StartCommand());
            add(new HelpCommand());
            add(new TrackCommand());
            add(new ListCommand());
            add(new UntrackCommand());
        }};
    }

    private boolean hasUpdateCorrectMessage(Update update) {
        return update.message() != null && update.message().text() != null;
    }

    //херь
    private Command identifyCommand(Update update) {
        if (hasUpdateCorrectMessage(update)) {
            String text = update.message().text();
            switch (text) {
                case "/start":
                    return new StartCommand();
                case "/help":
                    return new HelpCommand();

            }
        }
        return null;
    }

    public SendMessage processUpdate(Update update) {
        Command command = identifyCommand(update);
        if (command!=null) {
            return command.handle(update);
        }
        return  new SendMessage(update.message().chat().id(), "Команда не распознана!");
    }

}
