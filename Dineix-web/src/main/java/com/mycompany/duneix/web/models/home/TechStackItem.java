package com.mycompany.duneix.web.models.home;

public class TechStackItem {
    private String name;
    private String icon;
    private String description;

    public TechStackItem(String name, String icon, String description) {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
}