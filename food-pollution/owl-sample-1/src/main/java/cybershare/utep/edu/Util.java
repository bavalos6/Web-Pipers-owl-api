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

import java.io.File;

import org.semanticweb.owlapi.model.OWLOntology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import java.util.*;

/**
 * Class to convert to JSON. 
 * 
 * Built using Hermit Reasoner API, licensed under http://www.hermit-reasoner.com/license.html
 * Built using OWL API, licensed under https://www.gnu.org/licenses/lgpl-3.0.en.html. 
 * Built using SpringBoot, licensed under https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt
 *
 */
public class Util {

    public static final String BASE = "http://www.semanticweb.org/Team11/ontologies/2022/3/FoodPollution";
    public static final String GRAMS_OF_PROTEIN = "GramsOfProtein";
    public static final String CO2_PER_GRAM = "CO2perGram";
    public static final String HAS_CALORIES = "hasCalories";
    public static final String IS_FROM_RESTURANT = "isFromRestaurant";
    public static final String IS_BASED = "isBased";
    public static final String IS_PLANT_BASED = "isPlantBased";
    public static final String IS_MEAT_BASED = "isMeatBased";

	public static String getNameFromOntology(String ontologyName) {
        return ontologyName.substring(ontologyName.toString().indexOf("#")+1, ontologyName.toString().indexOf(">")); 
    }

	public static OWLOntology loadOntologyFromFile(String path, OWLOntologyManager manager) throws OWLOntologyCreationException {
        File file = new File(path);
        // Load local ontology
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        System.out.println("Loaded ontology: " + ontology);
        // We can always obtain the location where an ontology was loaded from
        IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
        System.out.println("    from: " + documentIRI);
        return ontology;
    }

	/**
     * Creates a new ontology manager
     * @return
     */
    public static OWLOntologyManager createManager(){
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        return manager;
    }

    public static IRI getExpression(String ex) {
        return IRI.create(BASE + "#" + ex);
    }

    public static OWLDataProperty getDataProperty(OWLDataFactory dataFactory, String ex) {
        return dataFactory.getOWLDataProperty(getExpression(ex)); 
    }

    public static OWLObjectProperty getObjectProperty(OWLDataFactory dataFactory, String ex) {
        return dataFactory.getOWLObjectProperty(getExpression(ex));
    }

    public static <E> boolean sizeIsOne(Set<E> value) {
        return value.size() == 1; 
    }
}
