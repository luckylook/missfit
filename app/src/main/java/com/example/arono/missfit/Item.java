package com.example.arono.missfit;

import android.graphics.Bitmap;


import com.backendless.BackendlessUser;


import java.io.Serializable;


/**
 * Created by arono on 13/02/2016.
 */
public class Item implements  Serializable{

    private String name;
    private BackendlessUser user;
    private double price;
    private Bitmap pictures[];
    private String type,brand,color,photoOne,photoTwo,photoThird,phoneNumber;
    private Size size;



    public void setItem(String name,double price,String type,BackendlessUser user,Size size,String color,String brand){
        setName(name);
        setPrice(price);
        setPictures(pictures);
        setType(type);
        setUser(user);
        setSize(size);
        setColor(color);
        setBrand(brand);
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public Bitmap[] getPictures() {
        return pictures;
    }

    public void setPictures(Bitmap[] pictures) {
        this.pictures = pictures;
    }

    public BackendlessUser getUser() {
        return user;
    }

    public void setUser(BackendlessUser user) {
        this.user = user;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoOne() {
        return photoOne;
    }

    public void setPhotoOne(String photosOne) {
        this.photoOne = photosOne;
    }
    public String getPhotoTwo() {
        return photoTwo;
    }

    public void setPhotoTwo(String photoTwo) {
        this.photoTwo = photoTwo;
    }
    public String getPhotoThird() {
        return photoThird;
    }

    public void setPhotoThird(String photoThird) {
        this.photoThird = photoThird;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Size {
        XS, XXS, SMALL, MEDIUM, LARGE, XL, XXL
    }
    public static Size stringToSize(String temp){
        Size size = null;
        switch(temp){
            case "XXS": size = Size.XXS;
                break;
            case "XS": size = Size.XS;
                break;
            case "S": size = Size.SMALL;
                break;
            case "M": size = Size.MEDIUM;
                break;
            case "L": size = Size.LARGE;
                break;
            case "XL": size = Size.XL;
                break;
            case "XXL": size = Size.XXL;
                break;
        }
        return size;
    }

    public void setSize(Size size){
        this.size= size;
    }
    public Size getSize(){
        return size;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
