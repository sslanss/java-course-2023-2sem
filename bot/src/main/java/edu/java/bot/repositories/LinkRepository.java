package edu.java.bot.repositories;

import edu.java.bot.objects.Link;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepository {
    private final ConcurrentMap<Long, Set<Link>> links;

    public LinkRepository() {
        links = new ConcurrentHashMap<>();
    }

    public Set<Link> findById(Long id) {
        Set<Link> linksById = links.get(id);
        if (linksById == null) {
            return new HashSet<>();
        } else {
            return new HashSet<>(linksById);
        }
    }

    public boolean add(Long id, String link) {
        links.putIfAbsent(id, new CopyOnWriteArraySet<>());
        Set<Link> linksById = links.get(id);
        return linksById.add(new Link(link));
    }

    public boolean remove(Long id, String link) {
        Set<Link> linksById = links.get(id);
        if (linksById != null) {
            return linksById.remove(new Link(link));
        }
        return false;
    }
}
