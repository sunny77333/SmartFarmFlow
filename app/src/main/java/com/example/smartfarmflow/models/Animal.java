package com.example.smartfarmflow.models;

import java.io.Serializable;

public class Animal implements Serializable {
    private String id;
    private String name;
    private String species;
    private String gender;
    private String status;
    private String ageYears;
    private String ageMonths;
    private String temperature;
    private String weight;
    private String activityDate;
    private String standTime;
    private String walkTime;
    private String sitTime;
    private String eatTime;
    private String drinkTime;
    private String tagNumber;
    private String latitude;
    private String longitude;

    public Animal() {
    }

    public Animal(String id, String name, String species, String gender, String status, String ageYears, String ageMonths, String temperature, String weight, String activityDate, String standTime, String walkTime, String sitTime, String eatTime, String drinkTime, String tagNumber, String latitude,
                  String longitude) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.status = status;
        this.ageYears = ageYears;
        this.ageMonths = ageMonths;
        this.temperature = temperature;
        this.weight = weight;
        this.activityDate = activityDate;
        this.standTime = standTime;
        this.walkTime = walkTime;
        this.sitTime = sitTime;
        this.eatTime = eatTime;
        this.drinkTime = drinkTime;
        this.tagNumber = tagNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgeYears() {
        return ageYears;
    }

    public void setAgeYears(String ageYears) {
        this.ageYears = ageYears;
    }

    public String getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(String ageMonths) {
        this.ageMonths = ageMonths;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getStandTime() {
        return standTime;
    }

    public void setStandTime(String standTime) {
        this.standTime = standTime;
    }

    public String getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(String walkTime) {
        this.walkTime = walkTime;
    }

    public String getSitTime() {
        return sitTime;
    }

    public void setSitTime(String sitTime) {
        this.sitTime = sitTime;
    }

    public String getEatTime() {
        return eatTime;
    }

    public void setEatTime(String eatTime) {
        this.eatTime = eatTime;
    }

    public String getDrinkTime() {
        return drinkTime;
    }

    public void setDrinkTime(String drinkTime) {
        this.drinkTime = drinkTime;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getAge() {
        return ageYears + " years " + ageMonths + " months";
    }

    public String getLatitude() {return latitude;}

    public void setLatitude(String latitude) {this.latitude = latitude;}

    public String getLongitude() {return longitude;}

    public void setLongitude(String longitude) {this.longitude = longitude;}
}
