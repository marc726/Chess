package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceType;

public class ProcessMove {
    

    public static ReturnPlay.Message processMove(String move) {
		if (!InputValidation.inputCheck(move)) {
			return ReturnPlay.Message.ILLEGAL_MOVE;
		}


	
		String moveFrom = move.substring(0, 2);
		String moveTo = move.substring(3, 5);
		ReturnPiece movingPiece = Chess.getPieceAt(moveFrom);
	
		// Check if the player is moving their own piece
		if (movingPiece == null || Chess.isWhitePiece(movingPiece.pieceType) != (Chess.currentPlayer == Player.white)) {
			return ReturnPlay.Message.ILLEGAL_MOVE;
		}



		// Check for castling moves:
		if (Castle.matchesCastlePattern(move)) {
			if (Castle.canCastle(move, Chess.board)) {
				Castle.makeCastlingMove(move);
				// Switch player after successful castling:
				Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;
				return null;  // Successfully castled so no message is needed
			} else {
				return ReturnPlay.Message.ILLEGAL_MOVE;
			}
		}




		// Check for pawn promotion:
		if (PawnPromo.checkPawnPromotion(move, Chess.board)) {
			PawnPromo.promotePawn(move, Chess.board);
			Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;
			return null;  // Pawn successfully promoted so no message is needed
		}
	




		// Save the piece at the target location
		ReturnPiece pieceAtTarget = Chess.getPieceAt(moveTo);

		//make move
		Chess.movePiece(movingPiece, moveTo);

		
		// Check if the king is threatened after the move
		if (Chess.isSquareAttacked(Chess.getKingPos(Chess.currentPlayer), Chess.board, movingPiece)) {
			// If the king is in check, revert the move
			Chess.movePiece(movingPiece, moveFrom); // Move the piece back
			if (pieceAtTarget != null) {
				Chess.board.add(pieceAtTarget); // Put the taken piece back
			}
			return ReturnPlay.Message.ILLEGAL_MOVE;
		}
			




		//check if opponent king is in check after move
		if (Check.isInCheck(Chess.currentPlayer , Chess.board)) {
			// If the opponent's king is in check, check if it is checkmate 
				return ReturnPlay.Message.CHECK;
		}
		






		// Switch player after a successful move
		Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;
		return null;  // Successfully moved so no message is needed
	}
}

