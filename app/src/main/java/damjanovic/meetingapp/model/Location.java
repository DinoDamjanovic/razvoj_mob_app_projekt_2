package damjanovic.meetingapp.model;

public enum Location {

    ZADAR("Zadarski ured"),
    ZAGREB("Zagrebački ured"),
    KARLOVAC("Karlovački ured"),
    OSIJEK("Osječki ured"),
    SPLIT("Splitski ured");

    private final String description;

    Location(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
