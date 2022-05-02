package cybershare.utep.edu;

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
import org.semanticweb.owlapi.model.OWLObject;

import java.util.*;
import java.util.Scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceDepth;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDisjointClassesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * First OWL API exercise
 * Web-based data ingetration
 * 
 * @author Instruction team Spring and Fall 2020 with contributions from L.
 *         Garnica
 * @version 2.0
 *          Description: The purpose of these file is to provide basic elements
 *          for using the OWLAPI
 *          Resources:https://owlcs.github.io/owlapi/ -- OWL-API
 *          Include your name here - ex. Modified by Eduardo Garcia for Assignment 3
 *
 */
@SpringBootApplication
public class MyFirstOWLAPIProgram {
    private static final String FOOD_POLLUTION = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/ontologies/FoodPollution.owl";
    private static final String FOOD_POLLUTION_NEW = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/ontologies/FoodPollutionNew.owl";
    private static final String BASE = "http://www.semanticweb.org/Team11/ontologies/2022/3/FoodPollution";

    private static final String FASTFOOD_DATA = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/owl-sample-1/data/fastfood.csv";
    private static final String EMISSIONS_DATA = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/owl-sample-1/data/ghg_emissions_by_life_cycle_stage_ourworldindata_upload.csv";

    public static void main(String[] args) {
        System.out.println("Running OWL demo");

        OWLOntologyManager manager = createManager();
        try {
            OWLDataFactory dataFactory = manager.getOWLDataFactory();
            OWLOntology ontology = loadOntologyFromFile(FOOD_POLLUTION_NEW, manager);
            // Scanner scanner = new Scanner(new File(EMISSIONS_DATA));
            // scanner.nextLine();
            // while (scanner.hasNextLine()) {
            //     String[] line = scanner.nextLine().split(",");
            //     String food = line[0];
            //     double CO2 = Double.parseDouble(line[1]) +
            //             Double.parseDouble(line[2]) +
            //             Double.parseDouble(line[3]) +
            //             Double.parseDouble(line[4]) +
            //             Double.parseDouble(line[5]) +
            //             Double.parseDouble(line[6]) +
            //             Double.parseDouble(line[7]);

            //     food = food.replaceAll("[^a-zA-Z0-9]", "");

            //     if (food.contains("Beef") || food.contains("Poultry") ||
            //             food.contains("Pig")) {
            //         addClassIndividual(ontology, manager, dataFactory, food, "meat");
            //         addDataPropertyAssertion(ontology, manager, dataFactory, food, CO2,
            //                 "CO2perGram");
            //     }

            //     if (food.contains("Tofu")) {
            //         addClassIndividual(ontology, manager, dataFactory, food, "vegan");
            //         addDataPropertyAssertion(ontology, manager, dataFactory, food, CO2,
            //                 "CO2perGram");
            //     }
            // }

            // scanner.close();

            // scanner = new Scanner(new File(FASTFOOD_DATA));
            // scanner.nextLine();
            // while (scanner.hasNextLine()) {
            //     try {
            //         String[] line = scanner.nextLine().split(",");
            //         String meal = line[1];
            //         String restaurant = line[0];
            //         Double calories = Double.parseDouble(line[2]);

            //         double protein = Double.parseDouble(line[12]);
            //         meal = meal.replaceAll("[^a-zA-Z0-9]", "");
            //         addClassIndividual(ontology, manager, dataFactory, meal, "Meal");

            //         if (meal.toLowerCase().contains("chicken")) {
            //             addObjectPropertyAssertion(ontology, manager, dataFactory, meal,
            //                     "PoultryMeat", "isMeatBased");
            //             addDataPropertyAssertion(ontology, manager, dataFactory, meal, protein,
            //                     "GramsOfProtein");
            //         } else if (meal.toLowerCase().contains("pork")) {
            //             addObjectPropertyAssertion(ontology, manager, dataFactory, meal, "PigMeat",
            //                     "isMeatBased");
            //             addDataPropertyAssertion(ontology, manager, dataFactory, meal, protein,
            //                     "GramsOfProtein");
            //         } else if (meal.toLowerCase().contains("veggie")) {
            //             addObjectPropertyAssertion(ontology, manager, dataFactory, meal, "Tofu",
            //                     "isPlantBased");
            //             addDataPropertyAssertion(ontology, manager, dataFactory, meal, protein,
            //                     "GramsOfProtein");
            //         } else {
            //             addObjectPropertyAssertion(ontology, manager, dataFactory, meal, "Beef",
            //                     "isMeatBased");
            //             addDataPropertyAssertion(ontology, manager, dataFactory, meal, protein,
            //                     "GramsOfProtein");
            //         }

            //         addDataPropertyAssertion(ontology, manager, dataFactory, meal, restaurant,
            //                 "isFromRestaurant");
            //         addDataPropertyAssertion(ontology, manager, dataFactory, meal, calories,
            //                 "hasCalories");
            //     } catch (NumberFormatException e) {

            //     }

            // }

            // scanner.close();

            // saveOntology(FOOD_POLLUTION_NEW, manager, ontology);

            // Marissas code!

            // ontology = loadOntologyFromFile(FOOD_POLLUTION_NEW, manager);
            Configuration configuration = new Configuration();
            configuration.reasonerProgressMonitor = new ConsoleProgressMonitor();

            Reasoner reasoner = new Reasoner(configuration, ontology);
            OWLClassExpression value = dataFactory.getOWLClass(getExpression("foodQuery"));
            SimpleShortFormProvider simpleShortFormProvider = new SimpleShortFormProvider(); 
            DLQueryEngine dlQueryEngine = new DLQueryEngine(reasoner, simpleShortFormProvider);
            
            String classExpression = "Meal and isBased some Food";
            Set<OWLNamedIndividual> individuals = dlQueryEngine.getQueryResults(classExpression.trim());
            
            System.out.println("Potential Query");
            System.out.println(individuals);
            System.out.println("---------------");

            System.out.println("Class value looks like this: ");
            System.out.println(value.toString());
            Set<OWLNamedIndividual> meals = reasoner.getInstances(value).entities().collect(Collectors.toSet());
            System.out.println(meals);

            // OWLDataPropertyAssertionAxiom assertion;
            OWLObjectProperty isBasedProperty = dataFactory.getOWLObjectProperty(IRI.create(BASE + "#" + "isBased"));
            OWLDataProperty co2PerGram = dataFactory.getOWLDataProperty(getExpression("CO2perGram"));
            OWLDataProperty gramsOfProtein = dataFactory.getOWLDataProperty(getExpression("GramsOfProtein"));
            OWLDataProperty hasCalories = dataFactory.getOWLDataProperty(getExpression("hasCalories"));
            OWLDataProperty isFromResturant = dataFactory.getOWLDataProperty(getExpression("isFromRestaurant"));
            // for (OWLNamedIndividual meal : meals) {
            //     System.out.println("------------------");
            //     System.out.println(getNameFromOntology(meal.toString()));
            //     System.out.println("-------------------------------------");

            //     Set<OWLNamedIndividual> foods = reasoner.getObjectPropertyValues(meal, isBasedProperty).entities()
            //             .collect(Collectors.toSet());
            //     for (OWLNamedIndividual food : foods) {
            //         System.out.println("Food: " + food.toString());
            //         System.out.println("Co2:pergram: " + reasoner.getDataPropertyValues(food, co2PerGram).stream()
            //                 .collect(Collectors.toList()).get(0).getLiteral());
            //     }
            //     try {
            //         System.out.println("Is based on: " +
            //                 reasoner.getObjectPropertyValues(meal, isBasedProperty).toString());
            //         System.out.println("Protein: " + reasoner.getDataPropertyValues(meal, gramsOfProtein).stream()
            //                 .collect(Collectors.toList()).get(0).getLiteral());
            //         System.out.println("Calories: " + reasoner.getDataPropertyValues(meal, hasCalories).stream()
            //                 .collect(Collectors.toList()).get(0).getLiteral());
            //         System.out.println("Is From Resturant: " + reasoner.getDataPropertyValues(meal, isFromResturant).stream()
            //                 .collect(Collectors.toList()).get(0).getLiteral());
            //     } catch (Exception e) {
            //         System.out.println(meal.toString() + " is missing attributes");
            //     }
            //     System.out.println("----------------------------------");
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String b = "Meal-and-isBased some Food"; 
        System.out.println("Running Application!!");
        SpringApplication.run(MyFirstOWLAPIProgram.class, args);
    }

    public static IRI getExpression(String ex) {
        return IRI.create(BASE + "#" + ex);
    }

    /**
     * Creates a new ontology manager
     * 
     * @return
     */
    public static OWLOntologyManager createManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        return manager;
    }

    public static void setString(String range, String nodeString) {
        range = nodeString;
    }

    /**
     * Load an ontology to memory from URI source
     * 
     * @param source
     * @param manager
     */
    public static OWLOntology loadOntologyFromURI(String source, OWLOntologyManager manager)
            throws OWLOntologyCreationException {
        IRI iri = IRI.create(source);
        OWLOntology ontology;
        ontology = manager.loadOntologyFromOntologyDocument(iri);
        System.out.println("Loaded ontology: " + ontology);
        return ontology;
    }

    /**
     * Load ontology from local file
     * 
     * @param path
     * @param manager
     * @return
     * @throws OWLOntologyCreationException
     */
    public static OWLOntology loadOntologyFromFile(String path, OWLOntologyManager manager)
            throws OWLOntologyCreationException {
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
     * Insert individual to a class
     * 
     * @param ontology
     * @param manager
     * @param dataFactory
     * @param subjectName
     * @param className
     * @return
     */
    public static void addClassIndividual(OWLOntology ontology, OWLOntologyManager manager, OWLDataFactory dataFactory,
            String subjectName, String className) {
        OWLIndividual subject = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + subjectName));
        OWLClass someClass = dataFactory.getOWLClass(IRI.create(BASE + "#" + className));
        OWLClassAssertionAxiom ax = dataFactory.getOWLClassAssertionAxiom(someClass, subject);
        System.out.println(ax.toString());
        manager.addAxiom(ontology, ax);
    }

    /**
     * Add object property assertion (between individuals)
     * 
     * @param ontology
     * @param manager
     * @param dataFactory
     * @param individual_1
     * @param individual_2
     * @param property
     */
    public static void addObjectPropertyAssertion(OWLOntology ontology, OWLOntologyManager manager,
            OWLDataFactory dataFactory, String individual_1, String individual_2, String property) {
        OWLIndividual i1 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_1));
        OWLIndividual i2 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_2));
        OWLObjectProperty o = dataFactory.getOWLObjectProperty(IRI.create(BASE + "#" + property));

        OWLAxiom assertion = dataFactory.getOWLObjectPropertyAssertionAxiom(o, i1, i2);
        manager.addAxiom(ontology, assertion);
    }

    /**
     * Add object property assertion (between individuals)
     * 
     * @param ontology
     * @param manager
     * @param dataFactory
     * @param individual_1
     * @param individual_2
     * @param property
     */
    public static void addDataPropertyAssertion(OWLOntology ontology, OWLOntologyManager manager,
            OWLDataFactory dataFactory, String individual_1, double individual_2, String property) {
        OWLIndividual i1 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_1));
        OWLDataProperty o = dataFactory.getOWLDataProperty(IRI.create(BASE + "#" + property));

        OWLAxiom assertion = dataFactory.getOWLDataPropertyAssertionAxiom(o, i1, individual_2);
        manager.addAxiom(ontology, assertion);
    }

    /**
     * Add object property assertion (between individuals)
     * 
     * @param ontology
     * @param manager
     * @param dataFactory
     * @param individual_1
     * @param individual_2
     * @param property
     */
    public static void addDataPropertyAssertion(OWLOntology ontology, OWLOntologyManager manager,
            OWLDataFactory dataFactory, String individual_1, String individual_2, String property) {
        OWLIndividual i1 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_1));
        OWLDataProperty o = dataFactory.getOWLDataProperty(IRI.create(BASE + "#" + property));

        OWLAxiom assertion = dataFactory.getOWLDataPropertyAssertionAxiom(o, i1, individual_2);
        manager.addAxiom(ontology, assertion);
    }

    /**
     * save ontology locally to specific path
     * 
     * @param path
     * @param manager
     * @param ontology
     * @throws IOException
     * @throws OWLOntologyStorageException
     */
    public static void saveOntology(String path, OWLOntologyManager manager, OWLOntology ontology)
            throws IOException, OWLOntologyStorageException {
        File file = new File(path);

        // default format as they are loaded e.g. xml, turtle
        manager.saveOntology(ontology, IRI.create(file.toURI()));
        System.out.println("Ontology was saved");
    }

    public static String getNameFromOntology(String ontologyName) {
        return ontologyName.substring(ontologyName.toString().indexOf("#") + 1, ontologyName.toString().indexOf(">"));
    }
}
