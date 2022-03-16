package com.example.tolan.models;

import java.util.List;

public class ModelRecyclerItemNivel {

    private int idNivel;
    private String sectionName;
    private String image ;



    private List<ModelRecyclerItemSubnivel> sectionItems;

    public ModelRecyclerItemNivel(String sectionName, String image, List<ModelRecyclerItemSubnivel> sectionItems, int idNivel) {
        this.sectionName = sectionName;
        this.image = image;
        this.sectionItems = sectionItems;
        this.idNivel=idNivel;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ModelRecyclerItemSubnivel> getSectionItems() {
        return sectionItems;
    }

    public void setSectionItems(List<ModelRecyclerItemSubnivel> sectionItems) {
        this.sectionItems = sectionItems;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionName='" + sectionName + '\'' +
                ", sectionItems=" + sectionItems +
                '}';
    }


}
