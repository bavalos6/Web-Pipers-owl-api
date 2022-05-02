package cybershare.utep.edu;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

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
