import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * A simple model of a zebra.
 * zebra's age, move, breed, and die.
 * 
 * @author David J. Barnes,Michael Kölling,Raihan Kamal and Ibrahim Miah
 * @version 2022.02.28 (1)
 */
public class Zebra extends Organism
{
    // Characteristics shared by all zebras (class variables).

    // The age at which a zebra can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a zebra can live.
    private static final int MAX_AGE =  150;
    // The likelihood of a zebra breeding.
    private static final double BREEDING_PROBABILITY = 0.02;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    //Amount of steps a zebra can make from eating grass
    private static final int GRASS_FOOD_VALUE = 90;
    
    // Individual characteristics (instance fields).
    
    // The zebra's food level, which is increased by eating grass.
    private int foodLevel;
    // The zebra's age.
    private int age;
    // The zebra's gender
    private Gender gender;
    // The zebra's disease condition
    private Disease disease;
    
    private Grass grass;

    /**
     * Create a new zebra. A zebra may be created with age
     * zero (a new born) or with a random age.
     * @param randomAge If true, the zebra will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The zebra's gender 
     * @param disease The zebra's disease condition 
     */
    public Zebra(boolean randomAge, Field field, Location location,Gender gender,Disease disease)
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
     * Make this zebra more hungry. This could result in the zebra's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    /**
     * This is what the zebra does most of the time: it looks for Grass to eat. 
     * Grass has 2 stages: stage 1 is not fully grown, stage 2 is fully grown and therefore returns more food value.
     * In the process, it might breed, die of hunger,
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
                
                // Stage 2 Fully grown grass
                if(grass.isAlive() && grass.returnAge()>=6) { 
                    grass.setDead();
                    foodLevel = GRASS_FOOD_VALUE;
                    return where;
                }
                
                //Stage 1 Not fully grown Grass
                else if(grass.isAlive() && grass.returnAge()<=5)
                {
                     grass.setDead();
                    foodLevel = GRASS_FOOD_VALUE/2;
                    return where;
                }
            
              
            }
        }
        return null;
    }
    
    /**
     * This is what the zebra does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newZebras A list to return newly born zebras.
     */
    public void act(List<Organism> newZebras)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newZebras);            
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
     * This could result in the zebra's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this zebra is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newZebras A list to return newly born zebras.
     */
    private void giveBirth(List<Organism> newRabbits)
    {
        // New zebras are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Zebra young = new Zebra(false, field, loc,gender,disease);
            newRabbits.add(young);
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
     * A zebra can breed if it has reached the breeding age.
     * @return true if the zebra can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
