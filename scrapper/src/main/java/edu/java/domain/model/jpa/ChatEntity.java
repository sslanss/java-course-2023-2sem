package edu.java.domain.model.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@Data
public class ChatEntity {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
        name = "trackings",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private List<LinkEntity> trackedLinks;

    public ChatEntity(Long chatId) {
        this.chatId = chatId;
        trackedLinks = new ArrayList<>();
    }
}
