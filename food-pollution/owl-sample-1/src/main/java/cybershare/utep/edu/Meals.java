package cybershare.utep.edu;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.io.File;
import java.io.IOException;

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

    public Meals(Set<OWLNamedIndividual> meals, OWLOntology ontology) {
        System.out.println(meals);
        this.meals = new ArrayList<Meal>();
        OWLReasoner reasoner = DLQueryEngine.createReasoner(ontology);
        ArrayList<HashMap<String, String>> values = new ArrayList<>();

        for (OWLNamedIndividual meal : meals) {
            HashMap<String, String> map = new HashMap<>();
            Meal m = new Meal();
            m.setName(getNameFromOntology(meal.toString()));

            ontology.getDataPropertyAssertionAxioms(meal)
                .forEach(
                    property -> map.put(property.getProperty().toString(), 
                                            property.getObject().getLiteral()));

            ArrayList<String> nodes = new ArrayList<>();
            ArrayList<String> nodeCO2 = new ArrayList<>();
            ontology.getObjectPropertiesInSignature()
                .stream()
                .forEach(property -> 
                    reasoner.getObjectPropertyValues(meal, property)
                        .getNodes()
                        .stream()
                        .forEach(node -> 
                            nodes.add(node.getRepresentativeElement().toString())));

            ontology.getObjectPropertiesInSignature()
                .stream()
                .forEach(property -> 
                    reasoner.getObjectPropertyValues(meal, property)
                        .getNodes()
                        .stream()
                        .forEach(node -> 
                            ontology
                                .getDataPropertyAssertionAxioms(
                                    node.getRepresentativeElement())
                                    .stream()
                                    .forEach(
                                        co2Property -> nodeCO2
                                            .add(co2Property
                                                .getObject().getLiteral()))));
            
            
            m.setCalories(map.get(RESTURANT));
            m.setProtein(map.get(CALORIES));
            m.setRestaurant(map.get(PROTEIN));
            m.setType(getNameFromOntology(nodes.get(0)));
            m.setPollution(nodeCO2.get(0));
            this.meals.add(m);

        }
    }

    public ArrayList<Meal> getMeals() {
        return meals; 
    }

    public static String getNameFromOntology(String ontologyName) {
        return ontologyName.substring(ontologyName.toString().indexOf("#")+1, ontologyName.toString().indexOf(">")); 
    }
}