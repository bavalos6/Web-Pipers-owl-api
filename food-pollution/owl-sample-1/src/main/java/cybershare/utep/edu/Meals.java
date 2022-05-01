package cybershare.utep.edu;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.io.File;
import java.io.IOException;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import java.util.*;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Meals {
    String RESTURANT = "<http://www.semanticweb.org/Team11/ontologies/2022/3/FoodPollution#isFromRestaurant>";
    String CALORIES = "<http://www.semanticweb.org/Team11/ontologies/2022/3/FoodPollution#isFromRestaurant>";
    String PROTEIN = "<http://www.semanticweb.org/Team11/ontologies/2022/3/FoodPollution#GramsOfProtein>";
    private ArrayList<Meal> meals;

    public Meals() {
        this.meals = new ArrayList<Meal>();
        this.meals.add(new Meal());
        this.meals.add(new Meal());
        this.meals.add(new Meal());
    }

    public Meals(Set<OWLNamedIndividual> meals, OWLOntology ontology, OWLDataFactory dataFactory, Reasoner reasoner) {
        System.out.println(meals);
        this.meals = new ArrayList<Meal>();
        for (OWLNamedIndividual meal : meals) {
            Meal m = new Meal();
            try { 
                List<OWLNamedIndividual> foods = reasoner
                        .getObjectPropertyValues(meal, Util.getObjectProperty(dataFactory, Util.IS_BASED)).entities()
                        .collect(Collectors.toList());
                m.setName(Util.getNameFromOntology(meal.toString()));
                m.setType(Util.getNameFromOntology(foods.get(0).toString()));
                m.setPollution(reasoner
                        .getDataPropertyValues(foods.get(0), Util.getDataProperty(dataFactory, Util.CO2_PER_GRAM))
                        .stream().collect(Collectors.toList()).get(0).getLiteral());
                m.setRestaurant(
                        reasoner.getDataPropertyValues(meal, Util.getDataProperty(dataFactory, Util.IS_FROM_RESTURANT))
                                .stream().collect(Collectors.toList()).get(0).getLiteral());
                m.setCalories(reasoner.getDataPropertyValues(meal, Util.getDataProperty(dataFactory, Util.HAS_CALORIES))
                        .stream().collect(Collectors.toList()).get(0).getLiteral());
                m.setProtein(
                        reasoner.getDataPropertyValues(meal, Util.getDataProperty(dataFactory, Util.GRAMS_OF_PROTEIN))
                                .stream().collect(Collectors.toList()).get(0).getLiteral());
            } catch (Exception e) {
                System.out.println(Util.getNameFromOntology(meal.toString()) + " not configured properly!");
                e.printStackTrace();
            }
            this.meals.add(m);
        }
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public static String getNameFromOntology(String ontologyName) {
        return ontologyName.substring(ontologyName.toString().indexOf("#") + 1, ontologyName.toString().indexOf(">"));
    }

}