package edu.java.domain.model;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Link {
    private Long linkId;
    private URI url;
    private OffsetDateTime lastChecked;

    @Override
    public int hashCode() {
        return 31 * url.hashCode() + lastChecked.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(url, link.url) && Objects.equals(lastChecked, link.lastChecked);
    }
}
