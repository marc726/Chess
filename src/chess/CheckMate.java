package chess;
import java.util.ArrayList;


public class CheckMate {

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

        //get opposite player king position
        String kingPosition;
        if (player.equals(Chess.Player.white)) {
            kingPosition = Chess.getKingPos(Chess.Player.black);
        } else {
            kingPosition = Chess.getKingPos(Chess.Player.white);
        }

        // Get the king piece at that position
        ReturnPiece king = Chess.getPieceAt(kingPosition);

        // Check every space around king
        char kingFile = kingPosition.charAt(0);
        int kingRank = Character.getNumericValue(kingPosition.charAt(1));

        for (int fileOffset = -1; fileOffset <= 1; fileOffset++) {
            for (int rankOffset = -1; rankOffset <= 1; rankOffset++) {
                // Exclude the square where the king is currently located
                if (fileOffset == 0 && rankOffset == 0) continue;

                char checkFile = (char) (kingFile + fileOffset);
                int checkRank = kingRank + rankOffset;

                // Ensure the position is within the board's boundaries
                if (checkFile >= 'A' && checkFile <= 'H' && checkRank >= 1 && checkRank <= 8) {
                    String moveToPosition = "" + checkFile + checkRank;

                    // Check if the king can legally move to that square
                    if (LegalCheck.isLegalMove(kingPosition + " " + moveToPosition, board)) {
                        // Check if the square is attacked by the opponent
                        if (!Chess.isSquareAttacked(moveToPosition, board, king)) {
                            return false;  // King has a safe square to move to, so it's not checkmate.
                        }
                    }
                }
            }
        }

        return true;
    }
}