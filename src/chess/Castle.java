package chess;

import java.util.ArrayList;

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




}
