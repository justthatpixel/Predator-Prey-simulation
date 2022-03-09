import java.util.List;
import java.util.Random;

/**
 * A simple model of a Mouse.
 * Mouse age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling & Raihan Kamal
 * @version 2016.02.29 (2)
 */
public class Mouse extends Organism
{
    // Characteristics shared by all MICE (class variables).

    // The age at which a Mouse can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a mouse can live.
    private static final int MAX_AGE = 30;
    // The likelihood of a mouse breeding.
    private static final double BREEDING_PROBABILITY = 0.5;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The Mouse's age.
    private int age;
    
    private Gender gender;
    
    private Disease disease;

    /**
     * Create a new Mouse. A Mouse may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the hawk will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The Mouse's gender (can it breed or not?)
     * @param disease The Mouse's disease condition (is it ill or not?)
     */
    public Mouse(boolean randomAge, Field field, Location location,Gender gender,Disease disease)
    {
        super(field,location,gender,disease);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the hawk does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newMouse A list to return newly born Mouse.
     */
    public void act(List<Organism> newMouse)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newMouse);            
            // Try to move into a free location.
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

    /**
     * Increase the age.
     * This could result in the mouse's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this mouse is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newMouse A list to return newly born mice.
     */
    private void giveBirth(List<Organism> newMouse)
    {
        // New Mice are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Mouse young = new Mouse(false, field, loc,gender,disease);
            newMouse.add(young);
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
     * A Mice can breed if it has reached the breeding age.
     * @return true if the Mice can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
