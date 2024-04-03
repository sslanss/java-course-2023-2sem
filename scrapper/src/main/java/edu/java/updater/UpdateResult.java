package edu.java.updater;

import java.util.List;

public record UpdateResult<T>(List<T> updates) {
}
