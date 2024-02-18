package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;

public class CommandService {

    public CommandService() {
    }

    private boolean hasUpdateCorrectMessage(Update update) {
        return update.message() != null && update.message().text() != null;
    }

    private Command identifyCommand(Update update) {
        if (hasUpdateCorrectMessage(update)) {
            String text = update.message().text();
            switch (text) {
                case "/start":
                    return new StartCommand();
                case "/help":
                    return new HelpCommand();
                case "/track":
                    return new TrackCommand();
                case "/untrack":
                    return new UntrackCommand();
                case "/list":
                    return new ListCommand();
            }
        }
        return null;
    }

    public SendMessage processUpdate(Update update) {
        Command command = identifyCommand(update);
        if (command != null) {
            return command.handle(update);
        }
        return new SendMessage(update.message().chat().id(), "Команда не распознана!");
    }

}
