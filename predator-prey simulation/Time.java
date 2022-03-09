
/**
 *  A class  that handles and represents the Time 
 *  Time can either be day or night
 *
 * @author David J. Barnes,Michael Kölling,Raihan Kamal and Ibrahim Miah
 * @version 2022.02.28 (1)
 */
public class Time
{
    // instance variables - replace the example below with your own
    private int NumberOfSteps;
    public static boolean DayTime;
    public static String day="day";
    public static String night="night";

    private int x;
    private Time time;
    /**
     * Creats a new time object
     */
    public Time()
    {
        // initialise instance variables

    }

    /**
     * Update the time after every step
     * @param  steps, steps used to calculate length of day
     */
    public void updateSteps(int steps)
    {

        this.NumberOfSteps=steps;
        int x= steps % 10;
        if (x<5)
        {
            this.DayTime=true;
        }
        else
        {
            this.DayTime=false;
        }

    }
    /**
     * @return day, returns the string "day" if it is daytime 
     * @return night, returns the string "night" if it is nighttime
     * 
     */
    public String isItDaytimeStringVer()
    {

        if( time.DayTime== true)
        {

            return day;
        }
        else {

            return night;

        }

    }
    
    /**
     * Method to check if it is daytime
     * @return  true - if it is day
     * @return false - if it is night
     */
    public boolean isItDaytime()
    {

        if( time.DayTime== true)
        {
            return true;
        }
        else {

            return false;

        }

    }
        
    /**
     * Method To get time object
     */
    public void getTimeObject(Time time)
    {

        this.time= time;
        // System.out.println("The time of day is="+DayTime);

    }

}
