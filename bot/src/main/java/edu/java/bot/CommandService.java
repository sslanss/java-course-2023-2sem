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
        Command currentCommand = null;
        if (hasUpdateCorrectMessage(update)) {
            String text = update.message().text();
            currentCommand = switch (text) {
                case "/start" -> new StartCommand();
                case "/help" -> new HelpCommand();
                case "/track" -> new TrackCommand();
                case "/untrack" -> new UntrackCommand();
                case "/list" -> new ListCommand();
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
