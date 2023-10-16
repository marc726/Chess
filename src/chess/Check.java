package chess;

import java.util.ArrayList;

public class Check {

    /**
     * Determines if the given player's king is in check.
     *
     * @param player The player to check.
     * @param board  The current board state.
     * @return true if the king is in check, false otherwise.
     */
    public static boolean isInCheck(Chess.Player currentPlayer, ArrayList<ReturnPiece> board) {
        // Identify the opposing king's position.

        String opposingKingPosition;
        if (currentPlayer.equals(Chess.Player.white)) {
            opposingKingPosition = Chess.getBlackKingPos();
            System.out.println("Opposing king at " + opposingKingPosition);
        }else{
            opposingKingPosition = Chess.getWhiteKingPos();
            System.out.println("Opposing king at " + opposingKingPosition);
        }
        
        

        // Check if any of the opponent's pieces can attack the king.
        for (ReturnPiece piece : board) {
            // If the piece is of the same color as the player's, ignore it.
            if ((currentPlayer == Chess.Player.white && Chess.isWhitePiece(piece.pieceType))
                    || (currentPlayer == Chess.Player.black && !Chess.isWhitePiece(piece.pieceType))) {
                continue;
            }

            // Use the isSquareAttacked method to check if the piece can attack the king's position.
            if (Chess.isSquareAttacked(opposingKingPosition, board, piece)) {
                return true;  // The king is in check.
            }
        }

        return false;  // The king is not in check.
    }
}