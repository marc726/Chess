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

    public static boolean canCastle(String move, ArrayList<ReturnPiece> board) {
        // Check the pattern
        if (!matchesCastlePattern(move)) {
            return false;
        }

        // Check legality
        String kingStart = move.substring(0, 2);
        String kingEnd = move.substring(3, 5);

        // Positions of the rooks
        String rookStart = kingEnd.equals("g1") || kingEnd.equals("g8") ? "h" + kingStart.charAt(1) : "a" + kingStart.charAt(1);
        String rookEnd = kingEnd.equals("g1") || kingEnd.equals("g8") ? "f" + kingEnd.charAt(1) : "d" + kingEnd.charAt(1);

        ReturnPiece king = Chess.getPieceAt(kingStart);
        ReturnPiece rook = Chess.getPieceAt(rookStart);

        if (king == null || rook == null || Chess.hasMoved(king) || Chess.hasMoved(rook)) {
            return false;
        }

        // Check no piece is in between the king and the rook
        char startFile = kingStart.charAt(0);
        char endFile = rookStart.charAt(0);
        char rank = kingStart.charAt(1);
        for (char file = (char) (Math.min(startFile, endFile) + 1); file < Math.max(startFile, endFile); file++) {
            if (LegalCheck.isSquareOccupied(String.valueOf(file) + rank, board)) {
                return false;
            }
        }
            return true;
        }
        
        // TO-DO: Check that the king is not in check before castling


        // TO-DO: Check that the squares the king crosses are not attacked


    public static boolean matchesCastlePattern(String move) {
        return move.matches("^[e][1-8] [c|g][1-8]$");
    }

    public static void makeCastlingMove(String move) {
        switch(move) {
            case "e1 g1":
                Chess.movePiece(Chess.getPieceAt("e1"), "g1");
                Chess.movePiece(Chess.getPieceAt("h1"), "f1");
                break;
            case "e8 g8":
                Chess.movePiece(Chess.getPieceAt("e8"), "g8");
                Chess.movePiece(Chess.getPieceAt("h8"), "f8");
                break;
            case "e1 c1":
                Chess.movePiece(Chess.getPieceAt("e1"), "c1");
                Chess.movePiece(Chess.getPieceAt("a1"), "d1");
                break;
            case "e8 c8":
                Chess.movePiece(Chess.getPieceAt("e8"), "c8");
                Chess.movePiece(Chess.getPieceAt("a8"), "d8");
                break;
        }
    }
}