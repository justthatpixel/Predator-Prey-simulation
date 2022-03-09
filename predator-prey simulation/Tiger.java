import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Tiger.
 * Tiger : age, move, eat rabbits(only in the day), breed(only at night & opposite gender), pass disease
 * and die.
 * 
 * @author David J. Barnes,Michael Kölling,Raihan Kamal and Ibrahim Miah
 * @version 2022.02.28 (1)
 */
public class Tiger extends Organism
{
    // Characteristics shared by all tigers (class variables).
    // Probability of 
    private static final double GENDER_PROBABILITY = 0.5;
    // The age at which a tiger can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a tiger can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 30;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The tiger's age.
    private int age;
    // The tiger's food level, which is increased by eating rabbits.
    private int foodLevel;
    // The tiger's gender
    private Gender gender;
    // The neibouring cell tiger's gender  
    private String AnimalGender="";
    // The tiger's disease condition
    private Disease disease;
    
 


    /**
     * Create a tiger. A tiger can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the tiger will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The tiger's gender
     * @param disease The tiger's disease condition
     */
    public Tiger(boolean randomAge, Field field, Location location,Gender gender, Disease disease)
    {
        super(field, location,gender,disease);
        this.gender= new Gender();
        AnimalGender=gender.ShowGender();
        this.disease=disease;
        
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the tiger does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newTigers A list to return newly born tigers.
     */
    public void act(List<Organism> newTiger)
    {
        incrementAge();
        incrementHunger();
        chanceOfDyingFromDisease();
        if(isAlive()) {
            giveBirth(newTiger);      
            
            // Move towards a source of food if found.
            Location newLocation = Move();
           
            
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
     * Increase the age. This could result in the tiger's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
           setDead();
        }
    }
    
    /**
     * Make this tiger more hungry. This could result in the tiger's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * During the day the tiger moves towards rabbits to eat and during the night the tiger moves towards
     * other tigers to breed with.
     * Looks at adjacent tigers & Lions and gives them a high probability of being infected 
     * @return Where food was found, or null if it wasn't.
     */
    private Location Move()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        
        //Retrieve the time from "Time" class
        Time time= new Time();
        boolean x= time.DayTime;
        
     
       //If time is day,check neibouring cell for what animal occupies it
    if(x){
        while(it.hasNext()) {

            //If the occupying cell is a rabbit, eat it
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Zebra) {
                Zebra zebra = (Zebra) animal;
                if(zebra.isAlive()) { 
                    zebra.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
            
            //If the current tiger object is infected, the neibouring cell if it is a tiger is given a random chance 
            //of catching the disease
             else if(animal instanceof Tiger) {
                Tiger tiger = (Tiger) animal;
                if(tiger.isAlive()&& disease.returnInfected()) { 
                
                      tiger.setInfect();
                    
                    return where;
                }
            }
            //If the current tiger object is infected, the neibouring cell if it a lion is given a random chance 
            //of catching the disease
            
            else if(animal instanceof Lion) {
                Lion lion = (Lion) animal;
                if(lion.isAlive()&& disease.returnInfected()) { 
                
                      lion.setInfect();
                    
                    return where;
                }
            }
            
        }
    }
    // If the time is night it will look for other tigers to breed with
    else{
    
     while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Tiger) {
                Tiger tiger = (Tiger) animal;
                if(tiger.isAlive()) {
                    
                    return where;
                }
            }
        }
    
    }
        
        return null;
    }
    
    /**
     * Check whether or not this tiger is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newTigers A list to return newly born Tigers.
     */
    private void giveBirth(List<Organism> newTiger)
    {
        // New tigeres are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
       
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Tiger young = new Tiger(false, field, loc,gender,disease);
            newTiger.add(young);
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
     * A tiger can breed if it has: reached the breeding age, neibouring cell is the opposite gender .
     */
    private boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location where = it.next();
        Boolean compatableGenders=false;
            Object animal = field.getObjectAt(where);
            if((animal instanceof Tiger)) {
                Tiger tiger = (Tiger) animal;
                if(! tiger.showGender().equals(AnimalGender)) { 
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
     * Return the gender of the tiger
     */
    private String showGender()
    {
        return AnimalGender;
  
    }
    
      /**
     *This sets the tiger's disease status to infected
     */
     public void setInfect()
    {
       disease.setInfected();
  
    }
    
     /**
     * The infected tiger's are given a 50% chance of dying (at every step)
     */
     private void chanceOfDyingFromDisease()
    {
        
        //if animal is infected enter this if statement
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





