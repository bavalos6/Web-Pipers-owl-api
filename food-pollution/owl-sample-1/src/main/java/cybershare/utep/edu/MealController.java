package cybershare.utep.edu;

import org.semanticweb.owlapi.model.OWLOntology;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

import org.semanticweb.HermiT.Configuration;
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
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import java.util.*;
import java.util.Scanner;
import java.util.stream.Collectors;

@RestController
public class MealController {
    private static final String FOOD_POLLUTION_NEW = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/ontologies/FoodPollutionNew.owl";
    private static final String BASE = "http://www.semanticweb.org/Team11/ontologies/2022/3/FoodPollution";

    OWLOntology owlOntology;
    Reasoner reasoner;
    OWLDataFactory dataFactory;;

    public MealController() {
        OWLOntologyManager manager = Util.createManager();
        try {
            this.owlOntology = Util.loadOntologyFromFile(FOOD_POLLUTION_NEW, manager);
            Configuration configuration = new Configuration();
            configuration.reasonerProgressMonitor = new ConsoleProgressMonitor();
            this.reasoner = new Reasoner(configuration, this.owlOntology);
            this.dataFactory = manager.getOWLDataFactory();
            System.out.println("Ontology Settings Succesfully loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/meals")
    public Meals index(@RequestParam(value = "query", defaultValue = "Meal") String query) {

        return new Meals(this.reasoner.getInstances(this.dataFactory.getOWLClass(Util.getExpression(query))).entities()
                .collect(Collectors.toSet()), this.owlOntology, this.dataFactory, this.reasoner);
    }
}