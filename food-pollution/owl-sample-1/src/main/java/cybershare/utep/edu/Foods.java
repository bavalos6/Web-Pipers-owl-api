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

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Builds Response for Foods query
 * 
 * Built using Hermit Reasoner API, licensed under http://www.hermit-reasoner.com/license.html
 * Built using OWL API, licensed under https://www.gnu.org/licenses/lgpl-3.0.en.html. 
 * Built using SpringBoot, licensed under https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt
 *
 */
public class Foods {
    private ArrayList<Food> foods;

    public Foods() {
        // Testing purposes 
        this.foods = new ArrayList<>();
        this.foods.add(new Food());
        this.foods.add(new Food());
        this.foods.add(new Food());
        this.foods.add(new Food());
    }

    public Foods(Set<OWLNamedIndividual> foodQuery, OWLOntology ontology,
            OWLDataFactory dataFactory, Reasoner reasoner) {
        System.out.println(foods);
        this.foods = foodQuery.stream().collect(
                Collectors.mapping(foodq -> setFood(foodq, dataFactory, reasoner), Collectors.toCollection(ArrayList::new)));
    }

    public ArrayList<Food> getFoods() {
        return this.foods;
    }

    public Food setFood(OWLNamedIndividual food, OWLDataFactory dataFactory, Reasoner reasoner) {
        Food f = new Food();
        try {
            f.setName(Util.getNameFromOntology(food.toString()));
            f.setPollution(reasoner.getDataPropertyValues(food, Util.getDataProperty(dataFactory, Util.CO2_PER_GRAM))
                    .stream().collect(Collectors.toList()).get(0).getLiteral());
        } catch (Exception e) {
            System.out.println(f.toString());
            e.printStackTrace();
        }
        return f;
    }
}
