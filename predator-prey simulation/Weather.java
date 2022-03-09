import java.util.Random;
/**
 * A class that handles and represents the Weather
 * Weather condtion can either be:Foggy,Sunny or rainy
 *
 * @author David J. Barnes,Michael Kölling,Raihan Kamal and Ibrahim Miah
 * @version 2022.02.28 (1)
 */
public class Weather
{
    // instance variables - replace the example below with your own
    private int NumberOfSteps;
    public static boolean Sunny;
    public static boolean Foggy;
    public static boolean Rainy;
    public static String Sun="sunny";
    public static String Fog="foggy";
    public static String Rain="rainy";

    /**
     * Constructor for objects of class Weather
     */
    public Weather()
    {
        // initialise instance variables

    }

    /**
     * The weather is chosen randomly using a random number generator
     */
    public void updateWeather()
    {
        Random rand = new Random();
        int result = rand.nextInt(3);
        if(result==0)
        {
            Sunny=true;
            Foggy=false;
            Rainy=false;

        }
        else if(result==1)
        {
            Sunny=false;
            Foggy=true;
            Rainy=false;

        }        
        else
        {
            Sunny=false;
            Foggy=false;
            Rainy=true;

        }
    }

    /**
     * @return Sun- if the weather is sunny
     * @return Rain- if the weather is rainy
     * @return Fog- if the weather is foggy
     */
    public String whatWeatherIsIt()
    {

        if(Sunny==true)
        {
            return  Sun;

        }
        else if(Rainy==true)
        {
            return Rain;
        }        
        else
        {
            return Fog;

        }

    }
    /**
     * @return true-  if the weather is sunny
     */
    public boolean isItSunny()
    {
        if(Sunny==true)
        {
            return true;
        }
        else
            return false;

    }
    /**
     * @return true-  if the weather is Foggy
     */
    public boolean isItFoggy()
    {
        if(Foggy==true)
        {
            return true;
        }
        else
            return false;

    }
    /**
     * @return true-  if the weather is Rainy
     */
    public boolean isItRainy()
    {
        if(Rainy==true)
        {
            return true;
        }
        else
            return false;

    }

}
