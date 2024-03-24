package edu.java.domain.model;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Link {
    private Long linkId;
    private URI url;
    private OffsetDateTime lastChecked;
}
