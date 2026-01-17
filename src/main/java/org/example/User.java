package org.example;

public class User {
    private String name;
    private Double weight; // kg
    private Integer age;   // years
    private String gender; // "Male" or "Female"

    // ΝΕΟ ΠΕΔΙΟ: Παλμοί Ηρεμίας (Resting Heart Rate)
    private Integer restingHeartRate;

    public User() {}

    // Ενημερωμένος Constructor
    public User(String name, double weight, int age, String gender, int restingHeartRate) {
        this.name = name;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.restingHeartRate = restingHeartRate;
    }

    // Constructor συμβατότητας (για παλιό κώδικα, αν χρειαστεί)
    public User(String name, double weight, int age, String gender) {
        this(name, weight, age, gender, 60); // Default 60 bpm
    }

    // Getters
    public double getWeight() { return weight; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getName() { return name; }
    public int getRestingHeartRate() { return restingHeartRate; }

    // Setters
    public void setWeight(Double weight) { this.weight = weight; }
    public void setAge(Integer age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setName(String name) { this.name = name; }
    public void setRestingHeartRate(Integer restingHeartRate) { this.restingHeartRate = restingHeartRate; }
}