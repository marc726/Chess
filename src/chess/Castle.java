package chess;

import java.util.ArrayList;


/**
 * Chess Castling:
 * 
 * Castling is a special chess move involving the king and one of the rooks. It is the only move that allows two pieces (the king and a rook) to move at the same time. Castling is subject to specific rules and serves a dual purpose of king safety and rook activation.
 * 
 * Restrictions:
 * - Castling can only occur under the following conditions:
 *   - Neither the king nor the rook involved in the castling has moved previously in the game.
 *   - The squares between the king and the chosen rook must be unoccupied.
 *   - The king cannot be in check before or after the castling move.
 *   - The squares that the king moves across or occupies during castling cannot be under attack.
 * - Castling can be either kingside (short castling) or queenside (long castling).
 *   - Kingside castling involves the king moving two squares towards the rook on its right (g1 for White, g8 for Black), and the rook moving to the square crossed by the king (f1 for White, f8 for Black).
 *   - Queenside castling involves the king moving two squares towards the rook on its left (c1 for White, c8 for Black), and the rook moving to the square crossed by the king (d1 for White, d8 for Black).
 * 
 * When It Occurs:
 * - Castling can occur during a player's move if all the restrictions mentioned earlier are met.
 * - It is a single move that is executed as one action.
 * - Castling is a valuable strategic move to improve the safety of the king and activate a rook by bringing it to a central or semi-central file.
 * - Castling is a rare move and often occurs in the early to mid-game.
 * - Once a king or rook moves or castling conditions are violated, the right to castle in that particular game is lost for those pieces.
 * 
 * Castling adds depth to chess strategy by allowing players to develop their kingside and queenside positions efficiently while keeping the king safe. It is a unique chess move that requires a clear understanding of the rules and the game's tactical and positional aspects.
 */


public class Castle {
    
    public static boolean CastlePattern(String move, ArrayList<ReturnPiece> board) {
        //Check if the move is a castle or matches its pattern
        if (move.matches("^[e][1-8] [c|g][1-8]$")) {
            return true;
        }
        return false;
    }

    public static boolean isLegalCastling(String move, ArrayList<ReturnPiece> board) {
        if (!CastlePattern(move, board)) {
            return false;
        }

        //Check Legality

        //Rook and king positions
        String kingStart = move.substring(0, 2);
        String kingEnd = move.substring(3, 5);
        String rookStart;
        String rookEnd;

        if (kingEnd.charAt(0) == 'g') {
            rookStart = "h" + kingStart.charAt(1);
            rookEnd = "f" + kingEnd.charAt(1);
        } else {
            rookStart = "a" + kingStart.charAt(1);
            rookEnd = "d" + kingEnd.charAt(1);
        }

        //check if king and rook are on board
        ReturnPiece king = getPieceAt(kingStart, board);
        ReturnPiece rook = getPieceAt(rookStart, board);

        if (king == null || rook == null) {
            return false;
        }

        //checkif king and rook have moved
        if (king.hasMoved || rook.hasMoved) {
            return false;
        } 

        //check if squares in between are empty 

        //check if king is in check

        return true;
    }

    public static void makeCastlingMove(String move, ArrayList<ReturnPiece> board) {
        if(!isLegalCastling(move, board)) {
          return; // Illegal, do nothing
        }
    
        // Get king and rook
        String kingStart = move.substring(0,2); 
        String rookStart = move.substring(3,5); // Based on kingEnd
        
    
        // Update positions
        movePiece(getPieceAt(kingStart, board), kingEnd, board);
        movePiece(getPieceAt(rookStart, board), rookEnd, board);
    
      }

       // Helpers

    private static ReturnPiece getPieceAt(String pos, ArrayList<ReturnPiece> board) {
        // Implementation  
    }

    private static void movePiece(ReturnPiece piece, String newPos, ArrayList<ReturnPiece> board) {
        // Implementation
    }

    //checking something


}
