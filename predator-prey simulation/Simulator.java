import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A predator-prey simulator, based on a rectangular field
 * containing rabbits,foxes,hares,mouse and lions .
 * 
 * @author David J. Barnes,Michael Kölling,Raihan Kamal and Ibrahim Miah
 * @version 2022.02.28 (1)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a tiger will be created in any given grid position.
    private static final double TIGER_CREATION_PROBABILITY = 0.05;
    // The probability that a rabbit will be created in any given grid position.
    private static final double ZEBRA_CREATION_PROBABILITY = 0.7;    
     // The probability that a mouse will be created in any given grid position.
    private static final double MOUSE_CREATION_PROBABILITY = 0.03;    
       // The probability that a lion will be created in any given grid position.
    private static final double LION_CREATION_PROBABILITY = 0.05;   
      // The probability that a Grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.04; 
    // The probability that a Hare will be created in any given grid position.
    private static final double HARE_CREATION_PROBABILITY = 0.04; 
    
    // List of animals in the field.
    private List<Organism> organisms;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    //The organism's gender
    private Gender gender;
    //Time(day/Night) of the simulation 
    private Time time;
    //weather condition of  the simulation
    private Weather weather;
    //Organism's disease condition
    private Disease disease;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        organisms = new ArrayList<>();
        field = new Field(depth, width);
        gender= new Gender();
        time = new Time();
        weather= new Weather();
        disease=new Disease();

        // Create a view of the state of each location in the field.
        
        
        
        view = new SimulatorView(depth, width);
        view.setColor(Zebra.class, Color.ORANGE);
        view.setColor(Tiger.class, Color.BLUE);
        view.setColor(Mouse.class, Color.RED);
        view.setColor(Lion.class, Color.YELLOW);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Hare.class, Color.BLACK);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
           //  delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * organism.
     * Updates time after every step
     * Animals have a random probability of being infected at any moment ( updated every step )
     */
    public void simulateOneStep()
    {
        step++;
        int y=step%10;
        if(y==0)
        {
            weather.updateWeather();
        }
        
        // Provide space for newborn animals.
        List<Organism> newOrganisms = new ArrayList<>();        
        // Let all Organisms act.
        for(Iterator<Organism> it = organisms.iterator(); it.hasNext(); ) {
            Organism organism = it.next();
            organism.act(newOrganisms);
            if(! organism.isAlive()) {
                it.remove();
            }
            int x= step;
            time.updateSteps(x);                // update Time after each step
            time.getTimeObject(time);           
            disease.randomInfection();          // Animal is given a probability of a random infection
            
    
            
        }
               
        // Add the newly born organism to the main lists.
        organisms.addAll(newOrganisms);
       
        view.showStatus(step, field);
    }
    

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        organisms.clear();
        populate();
        generateGrass();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with Oranisms: tiger,rabbit,Mouse, lion and Hares.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Tiger tiger = new Tiger(true, field, location,gender,disease);
                    organisms.add(tiger);
                }
                else if(rand.nextDouble() <= ZEBRA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Zebra zebra = new Zebra(true, field, location,gender,disease);
                    organisms.add(zebra);
                }
                else if(rand.nextDouble() <= MOUSE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Mouse mouse = new Mouse(true, field, location,gender,disease);
                    organisms.add(mouse);
                }
                else if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Lion lion = new Lion(true, field, location,gender,disease);
                    organisms.add(lion);
                    
                }
                  else if(rand.nextDouble() <= HARE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hare hare = new Hare(true, field, location,gender,disease);
                    organisms.add(hare);
                    
                }
                
                
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
     /**
     * Randomly generate grass every 10 steps.
     */
    
     public void generateGrass(){
         Random rando = Randomizer.getRandom();
        
             for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rando.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Grass grass = new Grass(true, field, location,gender,disease);
                    organisms.add(grass);
                }
                
                // else leave the location empty.
            }
        }
    
        
        }
        
     /**
     * Update the weather every 10 steps
     */
    private void updateWeather(int steps){
    
       int x= steps % 10;
    }
    
    
}




