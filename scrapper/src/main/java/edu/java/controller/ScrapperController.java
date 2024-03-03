package edu.java.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class ScrapperController {
    @PostMapping("/{id}")
    public String chatRegistration(@Valid @PathVariable Integer id) {
        //проверка
        return "Чат зарегистрирован";
    }

    @DeleteMapping("/{id}")
    public String chatDelete(@PathVariable Integer id) {
        //проверка
        return "Чат успешно удалён";
    }
}
