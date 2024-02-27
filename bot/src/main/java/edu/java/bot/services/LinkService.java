package edu.java.bot.services;

import edu.java.bot.objects.Link;
import edu.java.bot.repositories.LinkRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkService {
    private final LinkRepository repository;

    @Autowired
    public LinkService(LinkRepository repository) {
        this.repository = repository;
    }

    public String track(Long id, String link) {
        //TODO:проверка на то, что ссылка валидная
        if (repository.add(id, link)) {
            return "Ссылка была добавлена для отслеживания";
        } else {
            return "Данная ссылка уже есть в списке отслеживаемых";
        }
    }

    public String untrack(Long id, String link) {
        if (repository.remove(id, link)) {
            return "Ссылка была удалена из списка отслеживаемых";
        } else {
            return "Ссылка не была в списке отслеживаемых";
        }
    }

    public String list(Long id) {
        Set<Link> linksById = repository.findById(id);
        if (linksById != null) {
            if (!linksById.isEmpty()) {
                return "Список отслеживаемых ссылок:\n" + linksById.stream()
                    .map(Link::toString)
                    .collect(Collectors.joining("\n"));
            }
        }
        return "Список ссылок для отслеживания пуст!";
    }
}
