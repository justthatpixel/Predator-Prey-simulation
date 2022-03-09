import java.util.Random;

/**
 *  A class  that handles and represents the Gender of the organism
 *  This class: assignes a gender
 *
 * @author David J. Barnes,Michael Kölling,Raihan Kamal and Ibrahim Miah
 * @version 2022.02.28 (1)
 */
public class Gender
{
    private String M= "male";
    private String F= "female";
    // Stores string value of the gender
    private String organismGender="";
    

    /**
     * Constructor for objects of class Gender
     * Create new gender object and assigns gender to Organism
     */
    public Gender()
    {
        // initialise instance variables
        
        Random randomNum = new Random();
         int result = randomNum.nextInt(2);
         if (result == 0) {
             organismGender=M;
         
             
          
        } else {
            organismGender=F;
         
            
        }
    }

    /**
     * @return organismGender- returns the gender of the organism as a String
     */
    public String ShowGender()
    {
       
        return organismGender;
       
    }
    
    
}
