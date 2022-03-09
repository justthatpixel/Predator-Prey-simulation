import java.util.List;
import java.util.Random;

/**
 * A simple model of a Grass.
 * Plants can: move, breed, change stage(return higher food value) and die.
 * 
 * @author David J. Barnes and Michael Kölling & Raihan Kamal
 * @version 2016.02.29 (2)
 */
public class Grass extends Organism
{
    // Characteristics shared by all grass (class variables).

    // The age at which a grass can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a grass can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a Grass breeding.
    private static final double BREEDING_PROBABILITY = 0.03;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    private Gender gender;
    // The grass's age.
    private  int age;
    
    private Disease disease;

    /**
     * Create a new Grass. Grass may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Grass will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The Grass's gender 
     * @param disease The Grass's disease condition 
     */
    public Grass(boolean randomAge, Field field, Location location, Gender gender,Disease disease)
    {
        super(field, location,gender,disease);
          age=0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
           
        }
        else{
          age = 0;
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newGrass A list to return newly born grass.
     */
    public void act(List<Organism> newGrass)
    {
        incrementAge();
        
        if(isAlive()) {
            Weather weather= new Weather();
           if(weather.isItRainy()) 
           {
            giveBirth(newGrass);  
    
            }
           
            
            /* Try to move into a free location.
           / Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
            */
        }
       
    }

    /**
     * Increase the stage of the Plant.
     * This could result in the Grass's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newGrass A list to return newly born Grass.
     */
    private void giveBirth(List<Organism> newGrass)
    {
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass young = new Grass(false, field, loc,gender,disease);
            newGrass.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A Plant can breed if it has reached the breeding age.
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
  /**
     * return stage of plant 
     */
    public int returnAge()
    {   
    return age;
    }
    
    
    
    
    
}
