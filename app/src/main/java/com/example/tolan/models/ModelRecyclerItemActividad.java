package com.example.tolan.models;

public class ModelRecyclerItemActividad {

    private int id;
    private String activityName;

    public ModelRecyclerItemActividad(int id, String activityName) {
        this.id = id;
        this.activityName = activityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
