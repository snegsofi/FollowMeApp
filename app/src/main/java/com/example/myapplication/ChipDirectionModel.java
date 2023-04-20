package com.example.myapplication;

// класс, необходимый для программного создания компонентов Chips
public class ChipDirectionModel {

    private String title;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ChipDirectionModel(String title, boolean isChecked) {
        this.title = title;
        this.isChecked=isChecked;

    }
}
