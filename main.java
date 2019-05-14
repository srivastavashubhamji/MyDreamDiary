import java.util.Scanner;
import java.util.Base64;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.Console;

public class MyDreamDiary
{
    private static String UserId;
    private static String uname;
    private static String pass;
    private static String EncUnamePass;    

    public static void main(String[] takeArgs) 
    {
        Scanner sc = new Scanner(System.in);        
        int userChoice = 0;

        printLogo();
            
        while( userChoice != 3 )                    // 3 ==> Exit 
        {
                            
            try 
            {
                userChoice = print_Menu("BeforeLogin");
                if( userChoice == -1 )
                {
                    throw new Exception("-2");      // Invalid UserChoice
                }
                if( (MyDreamDiary.UserId == null) )
                {
                    switch ( userChoice ) {

                     // __________   Case 1 : Login Started    _____________

                        case 1:
                        {
                            if( takeUnamePass() == false )
                            {   throw new Exception("-1");    }

                            /*  This  'is_Valid_Uname_Pass()'  will Check Valid Uname & Pass and Return 'boolean' Result */

                            if( ( is_Valid_Uname_Pass() == false ))         // Uname or Pass Incorrect ...
                            {    throw new Exception("4");    } 
                                                        
                            int UserWants = display_Rights_Of_User();

                            // 3 means Exit Now , Else If he Returns Anything else That  means 'Logout'

                            if( UserWants == 3 )    
                            {
                                userChoice = 3;
                                break;
                            }  
                        }                          
                        break;                            
                     
                            // __________ Case 2 : create A/c (Not Logged In) Started __________

                        case 2:
                        {
                            try
                            {
                                                // Dir Create Block...
                                String Folder="";
                                Folder = get_Master_Directory();    // "System_Home_dir / Master_Folder /"
                                if( Folder.length() == 0) {
                                    throw new Exception("-1");
                                }
                                takeUnamePass();
                                                // Before Registering New User , Checking in All Users List if-
                                                //  -This Username is  Already Exist  or  Not
                                if(( is_User_Already_Exist( uname )) == true )
                                {
                                    print_Message( 2 );    // Flag og 'Invalid Username Selected'.
                                }
                                else
                                {
                                    MyDreamDiary.EncUnamePass = encrypt_Uname_Pass();
                                    if( MyDreamDiary.EncUnamePass.equals("NotEncrypted") )
                                    {
                                        throw new Exception("-1");
                                    }
                    
                                    // Create / Open a file 'Do_Not_Delete_UP.txt' in 'System_Home_dir' and Save Encrypted Uname & Pass...                                                                                    
                                    String fullPath = get_Home_Dir_File_Path( Folder + "Do_Not_Delete_File.txt" );
                    
                                    if(fullPath.equals("NotGotFullPath"))
                                    {    
                                        throw new Exception("-1");
                                    }
                    
                                    boolean isEnc_Uname_Pass_Stored = store_Enc_Uname_Pass( fullPath );
                    
                                    if( isEnc_Uname_Pass_Stored == false )
                                    {    
                                        throw new Exception("-1");
                                    }
                                    else
                                    {
                                        print_Message( 1 ); // Successfully Registered
                                    }
                                }
                            } catch (Exception e) {
                                print_Message( -1 );    // Something Went Wrong, Retry.
                            }
                        }        // __________ Case 2 : create A/c (Not Logged In) ended _________
                        break;

                                // __________ Case 3 : Exit Now started _________
                        case 3:         // 3) Exit Now
                            {
                                String msg = "\n|================ !!! Exiting !!! ===================|\n\n"+
                                               "Are You Sure ( Yes-> Y || No-> N ) : ";
                                
                                try {
                                    print_GraceFully( msg );
                                }
                                finally{
                                    String chose = sc.next();
                                    if( (chose.equalsIgnoreCase("y") || chose.equalsIgnoreCase("yes")))
                                    {
                                        break;
                                    }
                                }
                            }
                            break;
                       
                        case 4:         // 4) About us & Security 
                            
                            String aboutUs = " \n\n\n"+
                                        "|=====================================================|\n"+
                                        "|        |||  My Dream Diary : Security  |||          |\n"+
                                        "|=====================================================|\n"+
                                        "| Dear User , First of All Thanks for using this App. |\n"+
                                        "| This Application is Build to keep this in mind that |\n"+
                                        "| Any One who has Saved his Secret Notes & ToDo List  |\n"+
                                        "| has not need to think About that Whatif SomeOne has |\n"+
                                        "| Read his Notes Behind him/her.                      |\n"+
                                        "|      Thats Why our Team has worked More on Security |\n"+
                                        "| by using  *64-bit Encryption Policy*                |\n"+
                                        "| So no One Can Read his Secret Notes without knowing |\n"+
                                        "| the Password as the Notes are Fully Encrypted :)    |\n"+
                                        "|=====================================================|\n"+
                                        "|               |||  Star Features |||                |\n"+
                                        "|=====================================================|\n"+
                                        "| 1) Login - Logout Feature                           |\n"+
                                        "| 2) Create new Account Feature                       |\n"+
                                        "| 3) Secured ( 64-bit Encryption )                    |\n"+
                                        "| 4) Cross-Platform Supportable                       |\n"+                                        
                                        "| 5) Robust                                           |\n"+
                                        "|=====================================================|\n"+
                                        "|               |||    Feedback    |||                |\n"+
                                        "|=====================================================|\n"+
                                        "| Contact us :                            8601725056  |\n"+
                                        "| shubhsrivastava271999@gmail.com         Shubham Sri.|\n"+
                                        "|=====================================================|\n";
                                        print_GraceFully( aboutUs );
                            break;
                        default:
                            
                            break;                        
                    }
                }                
                else                            // If  'Invalid'  Choice Returned
                    System.out.println("");
            
            } catch (Exception e) {
                int flag = Integer.parseInt( e.getMessage() );
                print_Message( flag );          // OOps Something went Wrong //
            }
        }
    }   // main end    
}
