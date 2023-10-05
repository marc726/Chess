package chess;

public class InputValidation {
    
    public static boolean inputCheck(String move){

        if (move == null){
            return false;
        }

        String moveFrom = move.substring(0, 2);   //example: "a1 a2" -> "a1" "a2"
        String moveTo = move.substring(3, 5);

        if (moveFrom == moveTo){
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
