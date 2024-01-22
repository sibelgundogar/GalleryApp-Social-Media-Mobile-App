package com.example.galleryapp.model;

public class LabelModel {
    String label;
    String description;
    boolean isChecked;

    public LabelModel(String label, String description, boolean isChecked) {
        this.label = label;
        this.description = description;
        this.isChecked = isChecked;
    }

    public LabelModel(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public LabelModel() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
