package com.example.c_changing.main;

public class item {
    public String itemContent;
    public int itemImageResId;
    public int Img;
    public int isclicked;
    public CanvasView canvasView;
    public item(int img) {
        this.itemImageResId = 0;
        this.itemContent = "";
        this.Img = img;
        this.isclicked = 0;
    }
}
