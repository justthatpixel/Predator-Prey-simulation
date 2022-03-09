import java.util.Random;
/**
 * A class  that handles and represents the disease status of an animal
 *
 * @author David J. Barnes,Michael Kölling,Raihan Kamal and Ibrahim Miah
 * @version 2022.02.28 (1)
 */
public class Disease
{
    // instance variables - replace the example below with your own
    private double PROBABILITY_OF_BEING_INFECTED=0.05;
    public boolean infected;
    public static int infectedCases=0;

    /**
     * Creates a new disease condition/status of the animal
     * Animals that have the "disease" charachteristic are random chosen at birth,
     * 
     */
    public Disease()
    {
        // initialise instance variables
       randomInfection();
        
    }

    /**
     * Method, randomly spawn infection in a populatiom
     *  If infected condition for animal is true, it can now can spread infection which will kill it
         */
    public void randomInfection()
    {
        Random rand= new Random();
        double x= rand.nextDouble();
        if(x<=PROBABILITY_OF_BEING_INFECTED)
        {
        infected=true;
        }
        else
        {
        infected= false;
        }
     
    }
    
     /**
     * Sets disease condition to positive and increments disease/infection counter for the GUI
     * 
    
     */
    public void setInfected()
    {
       
        infected=true;
        infectedCases++;
       
     
    }
    
    /**
     *@return infected, returns the infected condtion for an organism
    
     */
   public boolean returnInfected()
    {
        return infected;
       
    }
}
    
    