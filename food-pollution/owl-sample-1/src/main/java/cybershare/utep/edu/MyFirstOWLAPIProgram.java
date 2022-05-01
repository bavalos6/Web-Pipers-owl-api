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
import java.util.*;
import java.util.Scanner;

/**
 * First OWL API exercise 
 * Web-based data ingetration
 * @author Instruction team Spring and Fall 2020 with contributions from L. Garnica
 * @version 2.0
 * Description: The purpose of these file is to provide  basic elements for using the OWLAPI
 * Resources:https://owlcs.github.io/owlapi/  -- OWL-API
 * Include your name here - ex. Modified by Ana Doe for Assignment 3
 *
 */
@SpringBootApplication
public class MyFirstOWLAPIProgram 
{
    private static final String FOOD_POLLUTION = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/ontologies/FoodPollution.owl";
    private static final String FOOD_POLLUTION_NEW = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/ontologies/FoodPollutionNew.owl";
    private static final String BASE = "http://www.semanticweb.org/Team11/ontologies/2022/3/FoodPollution";

    private static final String FASTFOOD_DATA = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/owl-sample-1/data/fastfood.csv";
    private static final String EMISSIONS_DATA = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/owl-sample-1/data/ghg_emissions_by_life_cycle_stage_ourworldindata_upload.csv";

    public static void main( String[] args )
    {
        System.out.println( "Running OWL demo" );

        OWLOntologyManager manager = createManager();
        try {
            OWLOntology ontology = loadOntologyFromFile(FOOD_POLLUTION, manager);
            OWLDataFactory dataFactory = manager.getOWLDataFactory();

            Scanner scanner = new Scanner(new File(EMISSIONS_DATA));
            scanner.nextLine();
            while(scanner.hasNextLine()){
                String[] line = scanner.nextLine().split(",");
                String food = line[0];
                double CO2 = 
                    Double.parseDouble(line[1]) + 
                    Double.parseDouble(line[2]) + 
                    Double.parseDouble(line[3]) + 
                    Double.parseDouble(line[4]) + 
                    Double.parseDouble(line[5]) + 
                    Double.parseDouble(line[6]) + 
                    Double.parseDouble(line[7]);
                    
                food = food.replaceAll("[^a-zA-Z0-9]","");

                if(food.contains("Beef") || food.contains("Poultry") || food.contains("Pig")){
                    addClassIndividual(ontology, manager, dataFactory, food, "meat");
                    addDataPropertyAssertion(ontology, manager, dataFactory, food, CO2, "CO2perGram");
                }

                if(food.contains("Tofu")){
                    addClassIndividual(ontology, manager, dataFactory, food, "vegan");
                    addDataPropertyAssertion(ontology, manager, dataFactory, food, CO2, "CO2perGram");
                }
            }

            scanner.close();

            scanner = new Scanner(new File(FASTFOOD_DATA));
            scanner.nextLine();
            while(scanner.hasNextLine()){
                try{
                    String[] line       = scanner.nextLine().split(",");
                    String meal         = line[1];
                    String restaurant   = line[0];
                    Double calories     = Double.parseDouble(line[2]);

                    double protein = Double.parseDouble(line[12]);
                    meal = meal.replaceAll("[^a-zA-Z0-9]","");
                    addClassIndividual(ontology, manager, dataFactory, meal, "Meal");

                    if(meal.toLowerCase().contains("chicken")){
                        addObjectPropertyAssertion(ontology, manager, dataFactory, meal, "PoultryMeat", "isMeatBased");
                        addDataPropertyAssertion(ontology, manager, dataFactory, meal, protein, "GramsOfProtein");
                    }
                    else if(meal.toLowerCase().contains("pork")){
                        addObjectPropertyAssertion(ontology, manager, dataFactory, meal, "PigMeat", "isMeatBased");
                        addDataPropertyAssertion(ontology, manager, dataFactory, meal, protein, "GramsOfProtein");
                    }
                    else if(meal.toLowerCase().contains("veggie")){
                        addObjectPropertyAssertion(ontology, manager, dataFactory, meal, "Tofu", "isPlantBased");
                        addDataPropertyAssertion(ontology, manager, dataFactory, meal, protein, "GramsOfProtein");
                    }
                    else{
                        addObjectPropertyAssertion(ontology, manager, dataFactory, meal, "Beef", "isMeatBased");
                        addDataPropertyAssertion(ontology, manager, dataFactory, meal, protein, "GramsOfProtein");
                    }

                    addDataPropertyAssertion(ontology, manager, dataFactory, meal, restaurant, "isFromRestaurant");
                    addDataPropertyAssertion(ontology, manager, dataFactory, meal, calories, "hasCalories");
                } catch(NumberFormatException e){

                }
                
            }

            scanner.close();

            saveOntology(FOOD_POLLUTION_NEW, manager, ontology);

                        
            //WLOntology ontology = loadOntologyFromFile(FOOD_POLLUTION_NEW, manager);
            Set<OWLNamedIndividual> meals = DLQueryEngine.getInstances("Meal", ontology);
            OWLReasoner reasoner = DLQueryEngine.createReasoner(ontology);
            
            //OWLDataPropertyAssertionAxiom assertion; 
            for (OWLNamedIndividual meal : meals){
                System.out.println("------------------");
                //System.out.println(meal.toString().substring(meal.toString().indexOf("#")+1, meal.toString().indexOf(">")));
                System.out.println(getNameFromOntology(meal.toString()));
                System.out.println("-------------------------------------");
                //System.out.println(ontology.getDataPropertyAssertionAxioms(meal));
                System.out.println("Grams of protein, From Resturant, Calories:::");
                ontology.getDataPropertyAssertionAxioms(meal).forEach(x -> System.out.println(x.getProperty().toString() + " : " + x.getObject().getLiteral()));
                System.out.println("----------------------------------");
                System.out.println("This is typedoifdpaif:");
                //ontology.getObjectPropertyAssertionAxioms(meal).forEach(x -> System.out.println(getNameFromOntology(x.getProperty().toString())));

                // Get the Food type
                String range = "value"; 
                ontology.getObjectPropertiesInSignature().stream().forEach(property -> System.out.println(reasoner.getObjectPropertyValues(meal, property).getNodes()));
                ArrayList<String> nodes = new ArrayList<>();
                ontology.getObjectPropertiesInSignature()
                    .stream()
                    .forEach(property -> reasoner.getObjectPropertyValues(meal, property)
                        .getNodes()
                            .stream()
                            .forEach(node -> nodes.add(node.getRepresentativeElement().toString())));
                



                ontology.getObjectPropertiesInSignature()
                            .stream()
                            .forEach(property -> reasoner.getObjectPropertyValues(meal, property)
                                .getNodes()
                                    .stream()
                                    .forEach(node -> 
                                        ontology
                                            .getDataPropertyAssertionAxioms(
                                                node.getRepresentativeElement())
                                                    .stream()
                                                    .forEach(p -> System.out.println(p.getObject().getLiteral()))));
                
                System.out.println(getNameFromOntology(nodes.get(0)));
                System.out.println("----------------------------");
                //ontology.getObjectPropertiesInSignature()
                // ontology.getObjectPropertyAssertionAxioms(meal)
                //                     .forEach(x -> ontology.getObjectPropertyRangeAxioms(x.getProperty()).forEach(y -> System.out.println(y)));       
                // ontology.getObjectPropertyAssertionAxioms(meal).stream().forEach(x -> 
                //         ontology.getObjectPropertyRangeAxioms(x.getProperty()).stream().forEach(y -> 
                //         y.getRange().individualsInSignature().forEach(d -> System.out.println("Here: " + d))));
                //ontology.getObjectPropertyDomainAxioms();
                //ontology.getDataPropertyAssertionAxioms(meal).stream().forEach(x -> System.out.println(x.getObject().getLiteral()));
                //System.out.println(meal.getDataPr);                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(MyFirstOWLAPIProgram.class, args); 
    }

    /**
     * Creates a new ontology manager
     * @return
     */
    public static OWLOntologyManager createManager(){
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        return manager;
    }

    public static void setString(String range, String nodeString) {
        range = nodeString; 
    }

    /**
     * Load an ontology to memory from URI source
     * @param source
     * @param manager
     */
    public static OWLOntology loadOntologyFromURI(String source, OWLOntologyManager manager) throws OWLOntologyCreationException {
        IRI iri = IRI.create(source);
        OWLOntology ontology;
        ontology = manager.loadOntologyFromOntologyDocument(iri);
        System.out.println("Loaded ontology: " + ontology);
        return ontology;
    }

    /**
     * Load ontology from local file
     * @param path
     * @param manager
     * @return
     * @throws OWLOntologyCreationException
     */
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
     * Insert individual to a class
     * @param ontology
     * @param manager
     * @param dataFactory
     * @param subjectName
     * @param className
     * @return
     */
    public static void addClassIndividual(OWLOntology ontology, OWLOntologyManager manager, OWLDataFactory dataFactory, String subjectName, String className){
        OWLIndividual subject = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + subjectName));
        OWLClass someClass = dataFactory.getOWLClass(IRI.create(BASE + "#" + className));
        OWLClassAssertionAxiom ax = dataFactory.getOWLClassAssertionAxiom(someClass, subject);
        System.out.println(ax.toString());
        manager.addAxiom(ontology, ax);
    }

    /**
     * Add object property assertion (between individuals)
     * @param ontology
     * @param manager
     * @param dataFactory
     * @param individual_1
     * @param individual_2
     * @param property
     */
    public static void addObjectPropertyAssertion(OWLOntology ontology, OWLOntologyManager manager, OWLDataFactory dataFactory, String individual_1, String individual_2, String property){
        OWLIndividual i1 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_1));
        OWLIndividual i2 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_2));
        OWLObjectProperty o = dataFactory.getOWLObjectProperty(IRI.create(BASE + "#" + property));

        OWLAxiom assertion = dataFactory.getOWLObjectPropertyAssertionAxiom(o, i1, i2);
        manager.addAxiom(ontology, assertion);
    }

        /**
     * Add object property assertion (between individuals)
     * @param ontology
     * @param manager
     * @param dataFactory
     * @param individual_1
     * @param individual_2
     * @param property
     */
    public static void addDataPropertyAssertion(OWLOntology ontology, OWLOntologyManager manager, OWLDataFactory dataFactory, String individual_1, double individual_2, String property){
        OWLIndividual i1 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_1));
        OWLDataProperty o = dataFactory.getOWLDataProperty(IRI.create(BASE + "#" + property));

        OWLAxiom assertion = dataFactory.getOWLDataPropertyAssertionAxiom(o, i1, individual_2);
        manager.addAxiom(ontology, assertion);
    }

    /**
    * Add object property assertion (between individuals)
    * @param ontology
    * @param manager
    * @param dataFactory
    * @param individual_1
    * @param individual_2
    * @param property
    */
   public static void addDataPropertyAssertion(OWLOntology ontology, OWLOntologyManager manager, OWLDataFactory dataFactory, String individual_1, String individual_2, String property){
       OWLIndividual i1 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_1));
       OWLDataProperty o = dataFactory.getOWLDataProperty(IRI.create(BASE + "#" + property));

       OWLAxiom assertion = dataFactory.getOWLDataPropertyAssertionAxiom(o, i1, individual_2);
       manager.addAxiom(ontology, assertion);
   }


    /**
     * save ontology locally to specific path
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
        return ontologyName.substring(ontologyName.toString().indexOf("#")+1, ontologyName.toString().indexOf(">")); 
    }
}
