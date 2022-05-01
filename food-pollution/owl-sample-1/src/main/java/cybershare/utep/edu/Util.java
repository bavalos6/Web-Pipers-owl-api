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
