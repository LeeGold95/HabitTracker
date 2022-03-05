package listapp.habittracker.users;

/*
 This class sets validation functions to validate sign-up fields input.
 Each function returns error string if input is invalid, or null if valid.
 */

public class RegValidation {


    public static String nameValid(String username){

        //check username length (also checks if field is empty)
        if(username.length()>15 || username.length()<3)
            return "must have between 3 to 15 characters";

        //check if username contains no special characters
        if(!username.matches("[a-zA-Z0-9]*"))
            return "username can't contain spaces";

        return null;
    }

    public static String passValid(String password){

        //check password length (also checks if field is empty)
        if(password.length()<5 || password.length()>25){
            return "must have between 5 to 25 characters";
        }

        //check for spaces in password. all other characters are valid.
        if(password.contains(" "))
            return "password can't contain spaces";

        return null;
    }

    public static String passMatch(String password, String rePassword){
        if(password.isEmpty())
            return "missing password";

        if(!password.equals(rePassword))
            return "confirmation doesn't match password";

        return null;
    }

    public static String mailValid(String email){
        if(email.isEmpty()) //allow empty input
            return null;

        //check if String is valid email address: ---@---.---
        if(!email.matches("(.+)@(.+)\\.(.+)$"))
            return "invalid email address";

        return null;
    }

    public static Boolean validateAll(String username, String password, String rePassword, String email){
        return (nameValid(username)==null && passValid(password)==null
                && passMatch(password, rePassword)==null && mailValid(email)==null);
    }

}
