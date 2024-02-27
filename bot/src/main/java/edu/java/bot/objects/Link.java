package edu.java.bot.objects;

public class Link {
    private final String link;

    public Link(String link) {
        this.link = link;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Link other = (Link) obj;
        if (link == null) {
            if (other.link != null) {
                return false;
            }
        } else if (!link.equals(other.link)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return link;
    }
}
