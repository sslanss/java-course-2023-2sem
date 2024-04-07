package edu.java.domain.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "links")
@NoArgsConstructor
@Data
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long linkId;

    @Column
    @Convert(converter = URIConverter.class)
    private URI url;
    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt;

    @ManyToMany(mappedBy = "trackedLinks")
    private List<ChatEntity> trackingChats;

    public LinkEntity(URI url, OffsetDateTime lastCheckedAt) {
        this.url = url;
        this.lastCheckedAt = lastCheckedAt;
        trackingChats = new ArrayList<>();
    }

    public LinkEntity(Long linkId, URI url, OffsetDateTime lastChecked) {
        this.linkId = linkId;
        this.url = url;
        this.lastCheckedAt = lastChecked;
        trackingChats = new ArrayList<>();
    }
}
