package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

/**
 * Chess Pawn Promotion:
 * 
 * In chess, pawn promotion is a special rule that allows a pawn to be promoted
 * to another piece when it reaches the opponent's back rank (8th rank for White
 * and 1st rank for Black). This rule exists to prevent a pawn from being stuck
 * as a pawn for the entire game and to add strategic depth to the game.
 * 
 * Restrictions:
 * - Pawn promotion occurs only when a pawn advances all the way to the
 * opponent's back rank (8th rank for White and 1st rank for Black).
 * - The promoted piece must be a piece that was captured earlier in the game.
 * The most common promotions are to a queen, but it's also possible to promote
 * to a rook, knight, or bishop.
 * - The promotion is not restricted to pieces of the same color, meaning a
 * player can have multiple queens or other promoted pieces of different colors.
 * - Pawn promotion is not mandatory. If a player chooses, they can keep a pawn
 * on the back rank. In practice, promoting to a queen is almost always the best
 * choice due to the queen's versatility.
 * 
 * When It Occurs:
 * - Pawn promotion can occur during a player's move when a pawn reaches the
 * opponent's back rank.
 * - After the pawn reaches the back rank, the player can choose which piece to
 * promote the pawn to, and the pawn is replaced by the chosen piece on the same
 * square.
 * - The new piece can be used immediately, and it follows the rules of the
 * promoted piece.
 * 
 * Pawn promotion adds an interesting tactical and strategic element to chess,
 * as players must decide whether to promote a pawn, what piece to promote it
 * to, and when to execute the promotion for maximum advantage.
 */

public class PawnPromo {

    public static boolean checkPawnPromotion(String move, ArrayList<ReturnPiece> board) {
        String startPos = move.substring(0, 2);
        ReturnPiece pawn = Chess.getPieceAt(startPos);
    
        // If pawn is null or not an actual pawn, return false
        if (pawn == null || !isPawn(pawn)) {
            return false;
        }
    
        // Check if pawn ends on promotion rank based on its color
        int promoRank = getPromotionRank(pawn);
        if (promoRank != Character.getNumericValue(move.charAt(4))) { // directly check the rank of endPos
            return false;
        }
    
        return true;
    }

    private static boolean isPawn(ReturnPiece piece) {
        if (piece == null) return false;
        return piece.pieceType == PieceType.WP || piece.pieceType == PieceType.BP;
    }

    private static int getPromotionRank(ReturnPiece pawn) {
        if (pawn.pieceType == PieceType.WP) {
            return 8;
        } else {
            return 1;
        }
    }


    public static void promotePawn(String move, ArrayList<ReturnPiece> board) {
		String startPos = move.substring(0, 2);
		ReturnPiece pawn = Chess.getPieceAt(startPos);
	
		// Remove pawn from board
		board.remove(pawn);
	
		// Determine promotion piece type based on pawn's color and the provided move
		PieceType promoType;
	
		if (move.matches("^[a-h][1-8] [a-h][1-8]( [NBRQ])?$") || move.matches("^[a-h][1-8] [a-h][1-8]( [NBRQ])?$|^draw$|^resign$|^reset$")) {
			char promotionChar = 'Q';  // Get the promotion character from the move string
			switch (promotionChar) {
				case 'N':
					promoType = (pawn.pieceType == PieceType.WP) ? PieceType.WN : PieceType.BN;
					break;
				case 'B':
					promoType = (pawn.pieceType == PieceType.WP) ? PieceType.WB : PieceType.BB;
					break;
				case 'R':
					promoType = (pawn.pieceType == PieceType.WP) ? PieceType.WR : PieceType.BR;
					break;
				default:
					promoType = (pawn.pieceType == PieceType.WP) ? PieceType.WQ : PieceType.BQ; // Default to queen
					break;
			}
		} else {
			promoType = (pawn.pieceType == PieceType.WP) ? PieceType.WQ : PieceType.BQ; // Default to queen if no promotion character specified
		}

		if (move.matches("^[a-h][1-8] [a-h][1-8] draw$")) {
			// Handle draw request after a move
		} else if (move.matches("^[a-h][1-8] [a-h][1-8] resign$")) {
			// Handle resign request after a move
		} else if (move.matches("^[a-h][1-8] [a-h][1-8] reset$")) {
			// Handle reset request after a move
	
		}
		// Add promotion piece to board at the end position
		Chess.addToBoard(promoType, PieceFile.valueOf(move.substring(3, 4)), Character.getNumericValue(move.charAt(4)));

		
	}
}
