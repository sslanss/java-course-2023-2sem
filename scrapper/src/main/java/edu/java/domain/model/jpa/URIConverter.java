package edu.java.domain.model.jpa;

import jakarta.persistence.AttributeConverter;
import java.net.URI;

public class URIConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI url) {
        return url == null ? null : url.toString();
    }

    @Override
    public URI convertToEntityAttribute(String url) {
        return URI.create(url);
    }
}
