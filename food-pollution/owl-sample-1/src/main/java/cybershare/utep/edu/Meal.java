package cybershare.utep.edu;

public class Meal {
    private String name = "Sample";
    private String type = "Sample";
    private String calories = "Sample";
    private String pollution = "Sample";;
    private String protein = "Sample";
    private String restaurant = "Sample";;
  
    public String getName() {
        return this.name; 
    }
  
    public String getType() {
      return this.type;
    }
  
    public String getCalories() {
      return this.calories; 
    }
  
    public String getPollution() {
      return this.pollution; 
    }
  
    public String getProtein() {
      return this.protein; 
    }
  
    public String getResturant() {
        return this.restaurant; 
    }
  
  
    public void setName(String name) {
        this.name = name;
    }
  
    public void setType(String type) {
        this.type = type;
    }
  
    public void setCalories(String calories) {
        this.calories = calories;
    }
  
    public void setPollution(String pollution) {
        this.pollution = pollution;
    }
  
    public void setProtein(String protein) {
        this.protein = protein;
    }
  
    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
  }