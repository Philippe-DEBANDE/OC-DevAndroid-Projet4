package com.lamzone.mareu.model;

public class StateVO implements Comparable<StateVO>{
    private String title;
    private boolean selected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    /*
     * Sorting by Title;
     */
    @Override
    public int compareTo(StateVO compareStateVO) {
        return getTitle().compareTo(compareStateVO.getTitle());

    }




}



