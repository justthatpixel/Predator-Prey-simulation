import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * A simple model of a Hare.
 * Hares age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling & Raihan Kamal
 * @version 2016.02.29 (2)
 */
public class Hare extends Organism
{
    // Characteristics shared by all Hare (class variables).

    // The age at which a Hare can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a Hare can live.
    private static final int MAX_AGE =  40;
    // The likelihood of a Hare breeding.
    private static final double BREEDING_PROBABILITY = 0.02;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    //Amount of steps a hare can make from eating grass
    private static final int GRASS_FOOD_VALUE = 90;
    
    // Individual characteristics (instance fields).
    
    // The hare's food level, which is increased by eating grass.
    private int foodLevel;
    // The hare's age.
    private int age;
    // The hare's gender
    private Gender gender;
    // The hare's disease condition
    private Disease disease;

    /**
     * Create a new hare. A hare may be created with age
     * zero (a new born) or with a random age.
     * @param randomAge If true, the hare will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The Hare's gender (can it breed or not?)
     * @param disease The Hare's disease condition (is it ill or not?)
     */
    public Hare(boolean randomAge, Field field, Location location,Gender gender,Disease disease)
    {
        super(field, location,gender, disease);
        age = 0;
      if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(GRASS_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = GRASS_FOOD_VALUE;
        }
    }
    
    /**
     * Make this Hare more hungry. This could result in the Hare's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    /**
     * This is what the Hare does most of the time: it looks for
     * Grass. In the process, it might breed, die of hunger,
     * or die of old age.
     */
     private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            if(organism instanceof Grass) {
                Grass grass = (Grass) organism;
                if(grass.isAlive() ) { 
                    grass.setDead();
                    foodLevel = GRASS_FOOD_VALUE;
                    return where;
                }
              
            }
        }
        return null;
    }
    
    /**
     * This is what the hare does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newHares A list to return newly born hares.
     */
    public void act(List<Organism> newHares)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newHares);            
            // Try to move into a free location.
            
            if(foodLevel>= GRASS_FOOD_VALUE/2)
            {
            Location newLocation = findFood();
            }
            else
            {
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
                 }
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the hare's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this hare is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHares A list to return newly born hares.
     */
    private void giveBirth(List<Organism> newHares)
    {
        // New hares are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Hare young = new Hare(false, field, loc,gender,disease);
            newHares.add(young);
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
     * A hare can breed if it has reached the breeding age.
     * @return true if the hare can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
