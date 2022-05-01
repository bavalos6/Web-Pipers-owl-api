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
import java.util.stream.Collectors;

@RestController
public class FoodPollutionController {
    private static final String FOOD_POLLUTION_NEW = "/Users/greywind/Desktop/Utep/Spring_2022/data_science/project/food-pollution-owl-api/food-pollution/ontologies/FoodPollutionNew.owl";

    OWLOntology owlOntology;
    Reasoner reasoner;
    OWLDataFactory dataFactory;;

    public FoodPollutionController() {
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

    @GetMapping("/")
    public String index() {
        return this.owlOntology.toString(); 
    }

    @GetMapping("/meals")
    public Meals getMeals(@RequestParam(value = "query", defaultValue = "Meal") String query) {
        return new Meals(this.reasoner.getInstances(
                this.dataFactory.getOWLClass(
                    Util.getExpression(query))).entities().collect(Collectors.toSet()), 
                this.owlOntology, this.dataFactory, this.reasoner);
    }

    @GetMapping("/foods")
    public Foods getFoods(@RequestParam(value = "query", defaultValue = "foodQuery") String query) {
        return new Foods(this.reasoner.getInstances(
                this.dataFactory.getOWLClass(
                    Util.getExpression(query))).entities().collect(Collectors.toSet()), 
                this.owlOntology, this.dataFactory, this.reasoner);    }
}