package chess;



import chess.Chess.Player;


public class ProcessMove {
    

    public static ReturnPlay.Message processMove(String move) {

		String moveFrom = move.substring(0, 2);
		String moveTo = move.substring(3, 5);
		ReturnPiece movingPiece = Chess.getPieceAt(moveFrom);

		if (!LegalCheck.isLegalMove(move, Chess.board)) {
			return ReturnPlay.Message.ILLEGAL_MOVE;
		}
	
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

			// Check if opponent king is in check/checkmate after pawn promotion
			if (CheckMate.isInCheckMate(Chess.currentPlayer, Chess.board)) {
				return (Chess.currentPlayer == Player.white) ? ReturnPlay.Message.CHECKMATE_WHITE_WINS : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
			} else if (Check.isInCheck(Chess.currentPlayer, Chess.board)) {
				return ReturnPlay.Message.CHECK;
			}

			// Switch player after successful pawn promotion
			Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;
			return null;  // Pawn successfully promoted so no message is needed
		}
	


		// Save the piece at the target location
		ReturnPiece pieceAtTarget = Chess.getPieceAt(moveTo);

		// Remove the piece at the target location if it exists (capture)
		if (pieceAtTarget != null) {
			Chess.board.remove(pieceAtTarget);
		}

		//make move
		Chess.movePiece(movingPiece, moveTo);

		Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;
		
		// Check if the king is threatened after the move
		if (Check.isInCheck(Chess.currentPlayer, Chess.board)) {
			// If the king is in check, revert the move
			Chess.movePiece(movingPiece, moveFrom); // Move the piece back
			if (pieceAtTarget != null) {
				Chess.board.add(pieceAtTarget); // Put the taken piece back
			}
			Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;
			return ReturnPlay.Message.ILLEGAL_MOVE;
		}
			

		Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;

		//check if opponent king is in check/checkmate after move
		if (CheckMate.isInCheckMate(Chess.currentPlayer, Chess.board)) {
		return (Chess.currentPlayer == Player.white) ? ReturnPlay.Message.CHECKMATE_WHITE_WINS : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
		}
		if (Check.isInCheck(Chess.currentPlayer, Chess.board)) {
			Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;
			return ReturnPlay.Message.CHECK;
		}

		Chess.hasMoved.put(movingPiece, true);



		// Switch player after a successful move
		Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;

		return null;  // Successfully moved so no message is needed
	}
}

