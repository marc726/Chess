package chess;

public class InputValidation {
    
    public static boolean inputCheck(String move){

        if (move == null){
            return false;
        }else{
            switch(move.toLowerCase().trim()){
                case "reset":
                case "resign":
                case "draw":
                    return true;
            default:
                return isValidInputFormat(move);
            }
        }
    }

    private static boolean isValidInputFormat(String input) {
		return input.matches("^[a-h][1-8] [a-h][1-8]$");
	}
}
