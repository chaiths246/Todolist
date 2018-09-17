package com.antra.android.todolist.model;

public class ListItems {
    String listName;
    int listImage;
    long id;

    public ListItems(long id,String listName,int listImage) {
        this.listName = listName;
        this.id = id;
        this.listImage=listImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getListImage() {
        return listImage;
    }

    public void setListImage(int listImage) {
        this.listImage = listImage;
    }


}
