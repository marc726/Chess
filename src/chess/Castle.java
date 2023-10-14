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
    
    public static boolean matchesCastlePattern(String move) {
        return move.equals("e1 g1") || move.equals("e1 c1") || move.equals("e8 g8") || move.equals("e8 c8");
    }
    
    public static boolean canCastle(String move, ArrayList<ReturnPiece> board) {
        String kingPosition = move.substring(0, 2);
        String endPosition = move.substring(3, 5);
        
        ReturnPiece king = Chess.getPieceAt(kingPosition);
        
        if (Chess.hasMoved(king)) {
            return false;
        }
        
        // Check if the squares between the king and the rook are empty
        if (endPosition.equals("g1") || endPosition.equals("g8")) {
            if (Chess.getPieceAt("f" + king.pieceRank) != null || Chess.getPieceAt("g" + king.pieceRank) != null) {
                return false;
            }
            if (!Chess.hasMoved(Chess.getPieceAt("h" + king.pieceRank))) {
                return true;
            }
        }
        
        if (endPosition.equals("c1") || endPosition.equals("c8")) {
            if (Chess.getPieceAt("b" + king.pieceRank) != null || Chess.getPieceAt("c" + king.pieceRank) != null || Chess.getPieceAt("d" + king.pieceRank) != null) {
                return false;
            }
            if (!Chess.hasMoved(Chess.getPieceAt("a" + king.pieceRank))) {
                return true;
            }
        }
        
        // Check if the king is not currently in check and the squares it moves across are not attacked
        if (Chess.isInCheck(kingPosition)) {
            return false;
        }
        
        String intermediateSquare;
        if (endPosition.equals("g1") || endPosition.equals("g8")) {
            intermediateSquare = "f" + king.pieceRank;
        } else {
            intermediateSquare = "d" + king.pieceRank;
        }

        if (Chess.isSquareAttacked(intermediateSquare, board)) {
            return false;
        }
        
        return true;
    }
    
    public static void makeCastlingMove(String move) {
        String kingPosition = move.substring(0, 2); //example: "a1 a2" -> "a1" "a2"
        String endPosition = move.substring(3, 5);
        
        ReturnPiece king = Chess.getPieceAt(kingPosition);
        ReturnPiece rook;
        
        if (endPosition.equals("g1") || endPosition.equals("g8")) {
            rook = Chess.getPieceAt("h" + king.pieceRank);
            Chess.movePiece(rook, "f" + king.pieceRank);
        } else {
            rook = Chess.getPieceAt("a" + king.pieceRank);
            Chess.movePiece(rook, "d" + king.pieceRank);
        }
        
        Chess.movePiece(king, endPosition);
    }
}