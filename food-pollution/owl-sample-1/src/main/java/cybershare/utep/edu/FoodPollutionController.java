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

import org.semanticweb.owlapi.model.OWLOntology;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceDepth;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.util.stream.Collectors;

@RestController
public class FoodPollutionController {
    private static final String FOOD_POLLUTION_NEW = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/ontologies/FoodPollutionNew.owl";

    OWLOntology owlOntology;
    Reasoner reasoner;
    OWLDataFactory dataFactory;
    DLQueryEngine dlQueryEngine; 

    public FoodPollutionController() {
        OWLOntologyManager manager = Util.createManager();
        try {
            this.owlOntology = Util.loadOntologyFromFile(FOOD_POLLUTION_NEW, manager);
            Configuration configuration = new Configuration();
            configuration.reasonerProgressMonitor = new ConsoleProgressMonitor();
            this.reasoner = new Reasoner(configuration, this.owlOntology);
            this.dataFactory = manager.getOWLDataFactory();
            SimpleShortFormProvider simpleShortFormProvider = new SimpleShortFormProvider(); 
            this.dlQueryEngine = new DLQueryEngine(this.reasoner, simpleShortFormProvider); 
            System.out.println("Ontology Settings Succesfully loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/")
    public String index() {
        return this.owlOntology.toString(); 
    }

    @GetMapping("/meals")
    public Meals getMeals(@RequestParam(value = "query", defaultValue = "Meal") String query) {
        return new Meals(this.reasoner.getInstances(
                this.dataFactory.getOWLClass(
                    Util.getExpression(query)), InferenceDepth.ALL).entities().collect(Collectors.toSet()), 
                this.owlOntology, this.dataFactory, this.reasoner);
    }

    @GetMapping("/meals/query")
    public Meals getMealsQuery(@RequestParam(value = "query", defaultValue="Meal-and-isBased-some-Food") String query) {
        String expression = query.replace("-", " "); 
        System.out.println("Expression is: " + expression);
        return new Meals(
                this.dlQueryEngine.getQueryResults(expression.trim()),
                this.owlOntology, this.dataFactory, this.reasoner);
    }

    @GetMapping("/foods")
    public Foods getFoods(@RequestParam(value = "query", defaultValue = "Food") String query) {
        return new Foods(this.reasoner.getInstances(
                this.dataFactory.getOWLClass(
                    Util.getExpression(query))).entities().collect(Collectors.toSet()), 
                this.owlOntology, this.dataFactory, this.reasoner);
    }

    @GetMapping("/foods/query")
    public Foods getFoodsQuery(@RequestParam(value = "query", defaultValue = "Food-and-CO2perGram-some-xsd:double[>=50]") String query) {
        String expression = query.replace("-", " "); 
        System.out.println("Expression is: " + expression);
        return new Foods(
                this.dlQueryEngine.getQueryResults(expression.trim()),
                this.owlOntology, this.dataFactory, this.reasoner);
    }
}