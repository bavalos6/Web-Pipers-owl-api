/**
*Code snippets of the class shown during the OWL API review
*Provided by Y. Cabrera and shared for use in the Assignment 3 of the course
*Assume your class is ontologyReasoningClass
*If reusing this code, make sure to give attribution in the code.
*/


import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter


public ontologyReasoningClass throws OWLOntologyStorageException{

public OWLReasoner reasoner
/// Add this in the constructor of your main class 


try{
// you should probably have an instances of manager, OWLDataFactory, etc. 
// the creation of these instances should be inside the try.
} catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }

// The following line creates a reasoner instance.
// Note, the structural reasoner that is used in some examples online does not seem to be working with complex classes. 

        this.reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
        
 // This code is important to compute all the inferences of the class.
        this.reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
}

public static void main( String[] args ) throws OWLOntologyStorageException{    

// call your method(s) to load and populate your ontology
            // check that your ontology is consistent
            boolean consistent = myOntology.reasoner.isConsistent();
            System.out.println("Consistent: " + consistent);
            
            // Method for answering questions
            simpleQuestions(myOntology.ontology, myOntology.manager, myOntology.dataFactory, myOntology.reasoner, "People", "hasPet");
        }
        
        
        
        
        /*
    * answer questions of the type (subject, predicate, object) given subjectclass, predicate, and object
    * input: ontology, manager, datafactory, reasoner, subjectClass, object property value, object property
    * returns: subject/s 
    */
    public static java.util.Set<String> complexQuestions(OWLOntology ontology, OWLOntologyManager manager, 
                    OWLDataFactory dataFactory, OWLReasoner reasoner, String className, 
                    String individual_1, String property){
        OWLNamedIndividual i1 = dataFactory.getOWLNamedIndividual(IRI.create(BASE + "#" + individual_1));
        OWLClassExpression class1 = dataFactory.getOWLClass(IRI.create(BASE + "#" + className));
        OWLClassExpression class2 = dataFactory.getOWLClass(IRI.create(mlsBase + "#" + className));
        OWLObjectProperty o1 = dataFactory.getOWLObjectProperty(IRI.create(BASE + "#" + property));
        OWLObjectProperty o2 = dataFactory.getOWLObjectProperty(IRI.create(mlsBase + "#" + property));

        OWLClassExpression realClass;
        OWLObjectProperty object;
        //if-else check which IRI is the right one
        if(ontology.getObjectPropertiesInSignature().contains(o1))         
            object = o1;
        else
            object = o2;

        if(ontology.getClassesInSignature().contains(class1))         
            realClass = class1;
        else
            realClass = class2;

        String[] individuals = new String[0];
        //find instances for a given class
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(realClass, true);
        java.util.Set<OWLNamedIndividual> classValues = instances.getFlattened();

        

        //find which instances are associated to a given object property
        java.util.Set<String> s =  new HashSet<String>();
        String[] temp;
        String temp2;
        for(OWLNamedIndividual ind : classValues){
            
            if(reasoner.getObjectPropertyValues(ind, object).containsEntity(i1)){
            
                temp= (ind.toString()).split("#");
                temp2= temp[1].split(">")[0]; 
                s.add(temp2);
            }
        }
                //print instances for testing
/*         for(String ind : individuals){
            System.out.println("   individual/s " + ind);
        } */

        return s;

    }

    public static java.util.Set<String> getIndividuals(OWLOntology ontology, OWLOntologyManager manager, OWLDataFactory dataFactory, OWLReasoner reasoner, String className){
        OWLClassExpression class1 = dataFactory.getOWLClass(IRI.create(BASE + "#" + className));
        OWLClassExpression class2 = dataFactory.getOWLClass(IRI.create(mlsBase + "#" + className));

        NodeSet<OWLNamedIndividual> ax;
        if(ontology.getClassesInSignature().contains(class1))
            ax = reasoner.getInstances(class1, true);
        else
            ax = reasoner.getInstances(class2, true);

        
        java.util.Set<OWLNamedIndividual> ind2 = ax.getFlattened();
        java.util.Set<String> s =  new HashSet<String>();
        String[] temp;
        String temp2;
        for(OWLNamedIndividual ind : ind2){
            temp= (ind.toString()).split("#");
             temp2= temp[1].split(">")[0]; 
            s.add(temp2);
        }

        NodeSet<OWLClass> cl;
        java.util.Set<OWLClass> cl2;
        if(className.equals("DataType")){
            cl = reasoner.getSubClasses(class1, true);
            cl2 = cl.getFlattened();
            for(OWLClass ind : cl2){
                temp= (ind.toString()).split("#");
                 temp2= temp[1].split(">")[0]; 
                s.add(temp2);
            }
        }
        
        return s;
    }  

        } // end of your class