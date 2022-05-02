/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, The University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package cybershare.utep.edu;

/**
 * Class to convert to JSON. 
 * 
 * Built using Hermit Reasoner API, licensed under http://www.hermit-reasoner.com/license.html
 * Built using OWL API, licensed under https://www.gnu.org/licenses/lgpl-3.0.en.html. 
 * Built using SpringBoot, licensed under https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt
 *
 */
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