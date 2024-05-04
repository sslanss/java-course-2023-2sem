package edu.java.util;

import java.net.URI;
import java.util.regex.Pattern;

public class LinkValidator {

    private LinkValidator() {

    }

    public static final Pattern STACK_OVER_FLOW_PATTERN =
        Pattern.compile("^https://stackoverflow.com/questions/(\\d{1,8})([/]\\S*)?$");
    public static final Pattern GIT_HUB_PATTERN = Pattern.compile("^https://github.com/(\\S+)/(\\S+)/?$");

    public enum LinkType {
        STACKOVERFLOW,
        GITHUB,
        UNSUPPORTED//????
    }

    public static boolean isLinkValid(URI link) {
        return STACK_OVER_FLOW_PATTERN.matcher(link.toString()).find() || GIT_HUB_PATTERN.matcher(link.toString())
            .find();
    }

    public static LinkType checkLinkType(URI link) {
        if (link.toString().matches("^https://stackoverflow.com.*")) {
            return LinkType.STACKOVERFLOW;
        } else if (link.toString().matches("^https://github.com.*")) {
            return LinkType.GITHUB;
        } else {
            return LinkType.UNSUPPORTED;
        }
    }
}
