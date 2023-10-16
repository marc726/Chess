package chess;
import java.util.ArrayList;


public class CheckMate {

    private static final char MIN_FILE = 'a';
    private static final char MAX_FILE = 'h';
    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 8;
    /**
     * Determines if the given player's king is in checkmate.
     *
     * @param player The player to check.
     * @param board  The current board state.
     * @return true if the king is in checkmate, false otherwise.
     */

    
    public static boolean isInCheckMate(Chess.Player player, ArrayList<ReturnPiece> board) {

        if (!Check.isInCheck(player, board)) {
            return false;  // The king must be in check for a checkmate.
        }
        System.out.println("got past isInCheck");
        //get opposite player king position
        String kingPosition;
        if (player.equals(Chess.Player.white)) {
            kingPosition = Chess.getBlackKingPos();
        } else {
            kingPosition = Chess.getWhiteKingPos();
        }

        // Get the king piece at that position
        ReturnPiece king = Chess.getPieceAt(kingPosition);
        System.out.println("King at " + kingPosition);

        // Check every space around king
        char kingFile = kingPosition.charAt(0);
        int kingRank = Character.getNumericValue(kingPosition.charAt(1));

        System.out.println("Checking king at " + kingPosition);

        for (int fileOffset = -1; fileOffset <= 1; fileOffset++) {
            System.out.println("Checking offset " + fileOffset);
            for (int rankOffset = -1; rankOffset <= 1; rankOffset++) {
                System.out.println("Checking offset " + fileOffset + " " + rankOffset);
                // Exclude the square where the king is currently located
                if (fileOffset == 0 && rankOffset == 0) continue;

                char checkFile = (char) (kingFile + fileOffset);


                int checkRank = kingRank + rankOffset;

                // Ensure the position is within the board's boundaries
                if (checkFile >= MIN_FILE && checkFile <= MAX_FILE && checkRank >= MIN_RANK && checkRank <= MAX_RANK) {
                    System.out.println("Checking " + checkFile + checkRank);
                    String moveToPosition = "" + checkFile + checkRank;

                    // Check if the king can legally move to that square
                    if (LegalCheck.isLegalMove(kingPosition + " " + moveToPosition, board)) {
                        System.out.println("King can move to " + moveToPosition);
                        // Check if the square is attacked by the opponent
                        if (!Chess.isSquareAttacked(moveToPosition, board, king)) {
                            System.out.println("King can move to " + moveToPosition + " and might not be attacked");
                            // Save the piece at the target location
		                    String originalKingPosition = kingPosition;  // Store the original position before any moves
                            Chess.movePiece(king, moveToPosition);
                            if (Chess.isSquareAttacked(moveToPosition, board, king)) {
                                //move king back
                                System.out.println("King can move to " + moveToPosition + " but is attacked");
                                Chess.movePiece(king, originalKingPosition);  // Move the king back to its original position
                                System.out.println("King moved back to " + kingPosition);
                            }else{
                                System.out.println("King can move to " + moveToPosition + " and isn't attacked");
                                Chess.movePiece(king, originalKingPosition);  // Move the king back to its original position
                                System.out.println("King moved back to " + kingPosition);
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}