import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a lion.
 * lion age, move, eat zebras, and die.
 * 
 * @author David J. Barnes and Michael Kölling & Raihan Kamal & Ibrahim Miah 
 * @version 2022.02.28 (2)
 */
public class Lion extends Organism
{
    // Characteristics shared by all lions (class variables).

    // The age at which a lion can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a lion can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a lion breeding.
    private static final double BREEDING_PROBABILITY = 0.75;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single zebra. In effect, this is the
    // number of steps a lion can go before it has to eat again.
    private static final int ZEBRA_FOOD_VALUE = 4;
    //number of steps lion can move from mouse
    private static final int MOUSE_FOOD_VALUE = 2;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    // The lion's age.
    private int age;
    // The lion's food level, which is increased by eating zebras.
    private int foodLevel;
    // The lion's gender
    private Gender gender;
    // The lion's health conditon
    private Disease disease;

    private String AnimalGender="";
    /**
     * Create a lion. A lion can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the lion will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The lion's gender (can it breed or not?)
     * @param disease The lion's disease condition 
     */
    public Lion(boolean randomAge, Field field, Location location,Gender gender,Disease disease)
    {
        super(field, location, gender,disease);
        this.gender= new Gender();
        AnimalGender=gender.ShowGender();
        this.disease=disease;

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(ZEBRA_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = ZEBRA_FOOD_VALUE;
        }
    }

    /**
     * This is what the lion does most of the time: it hunts for
     * zebraS. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newLion A list to return newly born lions.
     */
    public void act(List<Organism> newLion)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newLion);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Increase the age. This could result in the lion's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this lion more hungry. This could result in the lion's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for zebras & mice adjacent to the current location.
     * Only the first live zebras/mice is eaten.
     * Adjacent Tiger & lions are given a high probability of being infected 
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Zebra) {
                Zebra zebra = (Zebra) animal;
                if(zebra.isAlive()) { 
                    zebra.setDead();
                    foodLevel = ZEBRA_FOOD_VALUE;
                    return where;
                }

            }
            else if(animal instanceof Mouse) {
                Mouse mouse = (Mouse) animal;
                if(mouse.isAlive()) { 
                    mouse.setDead();
                    foodLevel = MOUSE_FOOD_VALUE;
                    return where;
                }
                else if(animal instanceof Lion) {
                    Lion lion = (Lion) animal;
                    if(lion.isAlive()&& disease.returnInfected()) { 

                        lion.setInfect();

                        return where;
                    }
                }
                else if(animal instanceof Tiger) {
                    Tiger tiger = (Tiger) animal;
                    if(tiger.isAlive()&& disease.returnInfected()) { 

                        tiger.setInfect();

                        return where;
                    }
                }

            }
        }
        return null;
    }

    /**
     * Check whether or not this lion is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newLions A list to return newly born lions.
     */
    private void giveBirth(List<Organism> newLions)
    {
        // New lions are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(false, field, loc,gender,disease);
            newLions.add(young);
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
     * A lion can breed if it has reached the breeding age and adjacent cell is the opposite gender.
     */
    private boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location where = it.next();
        Boolean compatableGenders=false;
        Object animal = field.getObjectAt(where);
        if((animal instanceof Lion)) {
            Lion lion = (Lion) animal;
            if(! lion.showGender().equals(AnimalGender)) { 
                compatableGenders=true;
                if(age >= BREEDING_AGE ){
                    return  true;

                }
                else
                {
                    return false;
                }
            }
            else 
            {
                return false;
            }
        }
        // age >= BREEDING_AGE   
        return  age >= BREEDING_AGE; 
    }

    /**
     * Return the gender of the Lion
     */
    private String showGender()
    {
        return AnimalGender;

    }
     /**
     *This sets the lion's disease status to infected
     */
    public void setInfect()
    {
        disease.setInfected();

    }

      /**
     * The infected lion's are given a 50% chance of dying (at every step)
     */
    private void chanceOfDyingFromDisease()
    {

        //if animal is infected enter this if statemetn
        if(disease.returnInfected())
        {

            // if animal is infected it has a 50 % chance of dying on each step
            int probabilityOfDying=rand.nextInt(2);
            if(probabilityOfDying==0)
            {
                setDead();

            }

        }

    }
}
