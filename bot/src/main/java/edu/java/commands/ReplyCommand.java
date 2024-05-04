package edu.java.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface ReplyCommand {
    SendMessage handleLink(Update update);
}
