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

    public static int display_Rights_Of_User() {// After Login - User Will See -.- This menu
                                                // For Logout Return 'any num' != 3
                                                // For Close Return 3

        Scanner sc = new Scanner(System.in);
        print_Message(5);                       // Print Congrats You are : "Logged In" ...//
        
        boolean isUserLoggedIn = true;                      // Just for Starting Value to Skip While Condition FIRST Time ...//
        int userChoice = 1;

        while( isUserLoggedIn == true )
        {

            
            String msg="\n\n|====================================================|\n"+
                           "|============= !!!  Welcome Admin  !!! ==============|\n"+
                           "|====================================================|\n"+
                           "|============== (1) Write in File     ===============|\n"+
                           "|============== (2) Read File         ===============|\n"+
                           "|============== (3) Delete File       ===============|\n"+
                           "|============== (4) Logout            ===============|\n"+
                           "|============== (5) Close Application ===============|\n"+
                           "\n\n\nSelect Option : ";
            print_GraceFully( msg );
            try {
                userChoice = Integer.parseInt(sc.next());
            } catch (Exception e) {
                print_Message( -3 );    // Print Input is Invalid ... 
                break;
            }
            if( userChoice < 1 || userChoice > 5 )
            {
                print_Message(-2);      // Invalid Choice Selected ...
                break;
            }
            
            try {
                switch ( userChoice ) {
                    case 1:                 // Write in File
                        {
                            boolean writingDone = false;
                            String userNotes = "";
                            msg = "\n\n\n|================= Write your Notes =================|\n\n-> ";                            
                            while( writingDone == false )                       // Taking Notes From User
                            {                                
                                print_GraceFully( msg );

                                sc = new Scanner( System.in );
                                userNotes += sc.nextLine();

                                msg = "\n\n\n|================ Writing Complete ? ================|\n\n"+
                                             "Yes-> Press 'Y' OR No-> 'N' : ";
                                print_GraceFully( msg );

                                sc = new Scanner( System.in );
                                String chs = sc.next();
                                if( chs.equalsIgnoreCase("y") || chs.equalsIgnoreCase("'y'"))
                                {
                                    writingDone = true;
                                }
                                msg = "\n\n\n|=================== Continue... ===================|\n\n-> ";
                                userNotes += "  ";
                            }

                            msg = "\n\n\n|=========== Do You Want to Save This ? =============|\n\n"+
                                    userNotes + "\n\n" +
                                    "|====================================================|\n\n" +
                                    "yes-> Y  OR  No-> N : ";
                            print_GraceFully( msg );
                            String strSave = sc.next();
                            boolean isNotesSaved = false;
                            if( strSave.equalsIgnoreCase("y"))
                            {
                                isNotesSaved = save_Users_Notes( userNotes );   // Saving User Notes ...
                                if( isNotesSaved )
                                {
                                    msg = "\n\n\n|============== Saved Successfully ==================|\n\n";
                                    System.out.println( msg );
                                }else{
                                    throw new Exception("-1");
                                }
                            }
                        }                    
                        break;
                    case 2:                 // Read File
                        {

                            String encryptedUserNotes = get_User_Notes();
                            if( encryptedUserNotes.equals("File Not Found"))
                            {
                                throw new Exception("-5");      // print "You Have Not Saved Any Notes" ...//
                            }
                            else if( encryptedUserNotes.equals("Error") )
                            {
                                throw new Exception("-1");      // Something Went Wrong ...//
                            }
                            
                            msg = "\n\n\n|================ !!! Read Notes !!! ================|\n\n";
                            print_GraceFully( msg );

                            Base64.Decoder dec = Base64.getDecoder();
                            String decryptedUserNotes = new String( dec.decode( encryptedUserNotes ));
                            System.out.println( decryptedUserNotes );
                        }
                        break;
                    case 3:                 // Delete File
                        {
                            try {
                                Scanner input = new Scanner(System.in);
                                String userChs = "";
                                msg = "\n\n\n|============= !!! Deleting File !!! ================|\n\n"+
                                        "This Can't Be Undo, Are You Sure ( Yes-> Y || No-> N ) : ";
                                print_GraceFully( msg );
                                userChs = input.next();

                                if( userChs.equalsIgnoreCase("y") || userChs.equalsIgnoreCase("yes") )
                                {
                                    
                                    String homeDir = System.getProperty("user.home");
                                    String fileSep = System.getProperty("file.separator");
                                    String finalPath = homeDir + fileSep + "My_Dream_Diary__Shubham"+ fileSep;          

                                    File fp = new File ( finalPath + "shubh_MyDreamDiary_"+ uname +"_File.txt" );
                                    if( fp.exists() )
                                    {                                        
                                        fp.delete();
                                        print_GraceFully("\n|=================== Notes Deleted ==================|\n");                                        
                                    }
                                    else
                                    {
                                        throw new Exception("-5");
                                    }
                                }
                            } catch (Exception e) {
                                print_Message( Integer.parseInt( e.getMessage()) );
                            }
                        }
                        break;

                    case 4:                 // Logout
                        {                            
                            msg = "\n|================ !!! Logout !!! ===================|\n\n"+
                                    "Are You Sure ( Yes-> Y || No-> N ) : ";

                            print_GraceFully( msg );
                            String choosen = sc.next();
                            if( choosen.equalsIgnoreCase("y") || choosen.equalsIgnoreCase("yes") )
                            {
                                isUserLoggedIn = false;                            
                            }
                        }
                        break;
                    case 5:                 // close Application
                        {
                            msg = "\n\n\n!============= Are You Sure To Close App ============|\n"+
                                  "Enter Choice : (Yes-> Y  Or  NO-> n) : ";
    
                            
                            print_GraceFully( msg );
                            String choosen = sc.next();
                            if( choosen.equalsIgnoreCase("y") || choosen.equalsIgnoreCase("yes") )
                            {
                                return -3;  // it Represents Exiting From App...
                            }
                        }
                        break;            
                    default:
                        break;
                }
            } catch (Exception e) {
                print_Message( Integer.parseInt( e.getMessage() ) );
            }
        }
       
        return 4;   // For Logout Return 'any num' != 3
    
    }               // ------- end of  'display_Rights_Of_User()' ------

    public static String get_User_Notes() {
        
        String Folder = "";
        Folder = get_Master_Directory();    // "System_Home_dir / Master_Folder /"

        if( Folder.length() == 0) {
            return "Error";
        }                                        
        String from_Where_to_Fetch = get_Home_Dir_File_Path( Folder + "shubh_MyDreamDiary_"+ uname +"_File.txt" );

        if( from_Where_to_Fetch.equals( "NotGotFullPath" ))
        {    return "Error";    }

        FileReader fp = null;        
        try {
            String encryptedUserNotes = "";
            fp = new FileReader( from_Where_to_Fetch );     
                            // If No User Notes Created Yet : throws Exception 'FileNotFoundException'

            BufferedReader buffrdr = new BufferedReader( fp );
            String row = null;
            while(( row = buffrdr.readLine() ) != null )
            {
                encryptedUserNotes += row ;
            }
            return encryptedUserNotes;
        } catch (Exception e) 
        {
            return "File Not Found";
        }
    }
    public static boolean save_Users_Notes( String userNotes ) {
        
        String encryptedUserNotes = "";

        Base64.Encoder enc = Base64.getEncoder();
        encryptedUserNotes = enc.encodeToString( userNotes.getBytes() );
                
        return ( store_Enc_User_Notes( encryptedUserNotes ) );
    }

    public static boolean store_Enc_User_Notes( String encryptedUserNotes ) {
        
        String Folder = "";
        Folder = get_Master_Directory();    // "System_Home_dir / Master_Folder /"

        if( Folder.length() == 0) {
            return false;
        }
                                        
        String fullPath = get_Home_Dir_File_Path( Folder + "shubh_MyDreamDiary_"+ uname +"_File.txt" );

        if(fullPath.equals( "NotGotFullPath" ))
        {    
            return false;
        }

        FileWriter fp ;
        try {

            fp = new FileWriter( fullPath,true );
            BufferedWriter buffwtr = new BufferedWriter( fp );
            buffwtr.write( encryptedUserNotes );
            buffwtr.newLine();
            buffwtr.close();

        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public static int print_Menu( String whichMenu ) {    
        
        String menu = "\n\n\n";
        int choice = 0;
        Scanner sc = new Scanner(System.in);
        try {
            if( whichMenu.equals( "BeforeLogin" ))   // checks if User has Logged in or Not  : "Menu" For Non Logged In Users
            {                                   // Not Loginned..
                
                 menu +="|===================|||  Menu  |||===================|\n"+
                        "|                   1) Login                         |\n"+
                        "|                   2) Create A/c                    |\n"+
                        "|                   3) Exit Now                      |\n"+
                        "|                   4) About us & Security           |\n"+
                        "|====================================================|\n\n";
            }
            else // "AfterLogin"                // User has Logged in : "Menu" For Logged In User
            {                                   // Logged In   ..
                menu =  "|===================|||  Menu  |||===================|\n"+
                        "|                   1) Write Something               |\n"+
                        "|                   2) Read My Notes                 |\n"+
                        "|                   3) Exit Now                      |\n"+
                        "|                   4) Logout                        |\n"+
                        "|====================================================|\n\n";
            }
            
            //  -------- printing Msg Gracefully ---------- //
            print_GraceFully( menu );

            System.out.print("\nEnter Your Choice : ");
            choice = sc.nextInt();
            if( choice >= 1 && choice <= 4 )
                return choice;
            else
                throw new Exception(" Bad Input ");
        } catch (Exception e) {            
            choice = -1;
        }
        finally
        {
            return choice;
        }
    } // print_Menu() ended

    public static void print_GraceFully(String msg) {
        
        char []charMsg = msg.toCharArray();
        try {
            for (int i = 0; i < msg.length(); i++) {
                System.out.print( charMsg[i] );
                Thread.sleep(8);
            }
        } catch (Exception e) {
            // print OOps Something went Wrong //
        }
    }

    
    public static void printLogo() {
        String msg = "\n\n\n";
        msg +="\n\n\n|=========================(^)=========================|\n"+
                    "|                   !!! Welcome !!!                   |\n"+
                    "|               On 'My Dream Diary App'               |\n"+
                    "|               This App will Maintain                |\n"+
                    "|                 (1) To Do List                      |\n"+
                    "|                 (2) Secret Notes                    |\n"+
                    "|  Developer :                 ... Shubham Srivastava |\n"+
                    "|=====================================================|\n\n\n\n";
                    
        char[] arrMsg = msg.toCharArray();
        try {                
            for (int i = 0; i < msg.length(); i++)
            {
                Thread.sleep(16);
                System.out.print( arrMsg[i] );
            }
        } catch (Exception e) {
            
        }
    } //printLogo() ended 
    public static boolean is_User_Already_Exist(String uname)
    { 
        // Telling : From which File fetching 'll start 
        String homeDir = System.getProperty("user.home");        
        String fileSep = System.getProperty("file.separator");
        String Folder = "My_Dream_Diary__Shubham";
        String from_Where_to_Fetch = homeDir + fileSep + Folder + fileSep + "Do_Not_Delete_File.txt";
        
        FileReader fp = null;
        boolean uname_Found = false;
        try {            

            fp = new FileReader( from_Where_to_Fetch );     // If No User Created : throws Exception 'FileNotFoundException'
            BufferedReader buffrdr = new BufferedReader( fp );
            String row = null;
            while(( uname_Found == false ) && (( row = buffrdr.readLine()) != null ))     // Each User's "Uname,Pass" stored In : 'row'
            {                
                String []encryptedUnameThanPass = row.split(",");
                uname_Found = check_User_Name_In( encryptedUnameThanPass[0] , uname);
            }        
        } catch (Exception e) {
                                    // System.out.println("\n---- No User Created Yet ----\n");
        }
        finally{
            return uname_Found;     // return false if Passed Username NOT Found in All Stored Usernames
        }
    }
    public static boolean is_Valid_Uname_Pass() {

        String homeDir = System.getProperty("user.home");
        String fileSep = System.getProperty("file.separator");
        String Folder = "My_Dream_Diary__Shubham";
        String FetchFromThisFile = homeDir + fileSep + Folder + fileSep + "Do_Not_Delete_File.txt";
        
        FileReader fp = null;
        boolean isUnamePassFound = false;

        try { 

            fp = new FileReader( FetchFromThisFile );     // If No User Created : throws Exception 'FileNotFoundException'
            BufferedReader buffrdr = new BufferedReader( fp );
            String row = null;
            while(( isUnamePassFound == false ) && (( row = buffrdr.readLine()) != null ))     // Each User's "Uname,Pass" stored In : 'row'
            {                
                String []encryptedUnameThanPass = row.split(",");
                // uname_Found = check_User_Name_In( encryptedUnameThanPass[0] , uname);
                isUnamePassFound = check_Uname_Pass_Matched_With( encryptedUnameThanPass[0] , encryptedUnameThanPass[1]);
            }
            return isUnamePassFound;
        } catch (Exception e) {
            return isUnamePassFound;
        }        
    }

    public static boolean check_User_Name_In( String encryUname , String uname ) { 
        Base64.Decoder dec = Base64.getDecoder();
        String storedUname = new String(dec.decode( encryUname ));
        if( storedUname.equals( uname ))
            return true;
        else
            return false;
    }

    // This Method will check ,Does Encrypted Uname and Encrypted Pass Match with Uname & Password stored in Files
    public static boolean check_Uname_Pass_Matched_With( String encryptedUname, String encryptedPass) {
        Base64.Decoder dec = Base64.getDecoder();
        String storedUname = new String(dec.decode( encryptedUname ));
        String storedPass = new String( dec.decode( encryptedPass ));
        
        if( storedUname.equals( MyDreamDiary.uname ) && (storedPass.equals( MyDreamDiary.pass )))
        {            
            return true;    // Uname and Pass Matched...
        }
        return false;
    }

    // This Method will Take the Raw Username and Raw Password from the User...
    public static boolean takeUnamePass() {
        try {

            Scanner sc = new Scanner(System.in);
            Console cons = System.console();
            char []charArrPass = null;

            System.out.print("\nEnter Your Username : ");
            MyDreamDiary.uname = sc.nextLine();
            System.out.print("\nEnter Your Password : ");
            charArrPass = cons.readPassword();

            MyDreamDiary.pass = "";             // Refreshing it Otherwise It will Start Concatinating

            for (int i=0 ; i<charArrPass.length ; i++) {
                MyDreamDiary.pass += charArrPass[i];
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // For Security Seasons ...Before Storing Credentials in Files, Encrypting them...with this method
    public static String encrypt_Uname_Pass() {        
        try {

            Base64.Encoder enc = Base64.getEncoder();
            Base64.Decoder dec = Base64.getDecoder();
            
            String encUname = "", encPass = "";
            encUname = enc.encodeToString( MyDreamDiary.uname.getBytes() );
            encPass = enc.encodeToString( MyDreamDiary.pass.getBytes() );
                        
            String encUnameCommaPass = encUname + "," + encPass;
            // char []arrUnamePass = null;
            // arrUnamePass = encUnameCommaPass.toCharArray();     // encUname and encPass stored in a Char Array

            return encUnameCommaPass;
        } catch (Exception e) {
            return "NotEncrypted";
        }
    }

    // This method will provide the Cross-Platform System's Home Directory path
    public static String get_Home_Dir_File_Path( String fileName) 
    {
        try {            
            String homeDir = System.getProperty("user.home");
            String pathSep = System.getProperty("file.separator");
            String fullPath = homeDir + pathSep + fileName;
            return fullPath;
        } catch (Exception e) {
            return "NotGotFullPath";
        }
    }

    // This Method will store Encrypted Username & Password in file
    public static boolean store_Enc_Uname_Pass( String fullPath )
    {
        FileWriter fp;
        try 
        {
            fp = new FileWriter( fullPath,true );   // append Encrypted 'Username' and 'Password'
            BufferedWriter buffwtr = new BufferedWriter( fp );
            buffwtr.write( MyDreamDiary.EncUnamePass );
            buffwtr.newLine();
            buffwtr.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // This method will provide the Master Directory for Storing the Files
    public static String get_Master_Directory()
    {
        String Folder="";
        try {
            String homeDir = System.getProperty("user.home");
            String fileSep = System.getProperty("file.separator");
            String whereToCreateDir = homeDir + fileSep + "My_Dream_Diary__Shubham";
            File fp = new File( whereToCreateDir );
            // File fp = new File( "D:\\jprojs" );            
            if( fp.mkdir() )
            {                    
                Folder = "My_Dream_Diary__Shubham" + fileSep;
            }
            else
            {                    
                Folder = "My_Dream_Diary__Shubham" + fileSep;
            }
        } catch (Exception e) {
            // Folder = "";
        }
        finally{
            return Folder;
        }
    }// Directory Created 
    // This Method will Print the Message according to the given Flag
    public static void print_Message( int flag ) {
        String msg = "";
        if( flag == -1 )                     // OOps!!! Something went Wrong...
        {
              msg="\n|====================================================|\n"+
                    "|                     OOPs !!!                       |\n"+
                    "|            Something Went Wrong,Retry!             |\n"+
                    "|====================================================|\n";           
        }
        else if( flag == -2 )
        {
              msg="\n|====================================================|\n"+
                    "|                     OOPs !!!                       |\n"+
                    "|             Invalid Choice Selected !              |\n"+
                    "|====================================================|\n";
        }
        else if( flag == -3 )
        {
              msg="\n|====================================================|\n"+
                    "|                    Warning !!!                     |\n"+
                    "|                  Invalid Input !                   |\n"+
                    "|====================================================|\n";
        }
        else if( flag == -4 )
        {
              msg="\n|====================================================|\n"+
                    "|                    Warning !!!                     |\n"+
                    "|              Invalid File Selected !               |\n"+
                    "|====================================================|\n";
        }

        else if( flag == -5 )
        {
              msg="\n|====================================================|\n"+
                    "|                      OOPs !!!                      |\n"+
                    "|           You Didn't Save any Notes Yet !          |\n"+
                    "|====================================================|\n";
        }

        else if( flag == 1)          // Congratulations...
        {
              msg="\n|=====================================================|\n"+
                    "|                Congratulations !!!                  |\n"+
                    "|              Successfully Registered                |\n"+
                    "|=====================================================|\n";            
        }        
        else if( flag == 2 )    // OOPs!!! Invalid Uname...
        {            
              msg="\n|====================================================|\n"+
                    "|                     OOPs !!!                       |\n"+
                    "|            Invalid Username Selected !             |\n"+
                    "|====================================================|\n";            
        }
        else if( flag == 4 )    // OOPs!!! Invalid Uname...
        {            
              msg="\n|====================================================|\n"+
                    "|                     OOPs !!!                       |\n"+
                    "|          Invalid Username OR Password !            |\n"+
                    "|====================================================|\n";            
        }
        else if( flag == 5 )
        {
            msg="\n\n|=====================================================|\n"+
                    "|                Congratulations !!!                  |\n"+
                    "|            ! You are Now Logged In !                |\n"+
                    "|=====================================================|\n\n";
        }
        else
        {
            System.out.println("\n ------- OOPs -----\n");
        }

        //  -------Printing the Message Gracefully ---------- //

        print_GraceFully( msg );
        
    } //print_Message() ended 
}// ____________ My_Dream_Diary_App Class Ended _________
