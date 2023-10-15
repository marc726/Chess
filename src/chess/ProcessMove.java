package chess;

import chess.Chess.Player;
import chess.ReturnPiece.PieceType;

public class ProcessMove {
    

    public static ReturnPlay.Message processMove(String move) {
		String moveFrom = move.substring(0, 2); //example: "a1 a2" -> "a1" "a2"
		String moveTo = move.substring(3, 5);
		ReturnPiece movingPiece = Chess.getPieceAt(moveFrom);
		

	
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
			return null;
		}

		    // Check if the piece is a king
			if (movingPiece.pieceType == PieceType.WK || movingPiece.pieceType == PieceType.BK) {
				if (Chess.isSquareAttacked(moveTo, Chess.board, movingPiece)) {
					return ReturnPlay.Message.ILLEGAL_MOVE;
				}
			}
        
		// whites move
		if (Chess.currentPlayer == Player.white) {
			ReturnPiece pieceAtTarget = Chess.getPieceAt(moveTo);
			if (pieceAtTarget != null && !Chess.isWhitePiece(pieceAtTarget.pieceType)) {
				// The space is occupied by a black piece.
				Chess.board.remove(pieceAtTarget);  // Remove the taken piece first
			}
			if (movingPiece != null && Chess.isWhitePiece(movingPiece.pieceType) && LegalCheck.isLegalMove(move, Chess.board)) {
				Chess.movePiece(movingPiece, moveTo); // Then move the piece
				Chess.currentPlayer = Player.black; // Switch player
				return null;  // Successfully moved so no message is needed
			} else {
				return ReturnPlay.Message.ILLEGAL_MOVE;
			}
		} else { //blacks move
			ReturnPiece pieceAtTarget = Chess.getPieceAt(moveTo);
			if (pieceAtTarget != null && Chess.isWhitePiece(pieceAtTarget.pieceType)) {
				// The space is occupied by a white piece.
				Chess.board.remove(pieceAtTarget);  // Remove the taken piece first
			}
			if (movingPiece != null && !Chess.isWhitePiece(movingPiece.pieceType) && LegalCheck.isLegalMove(move, Chess.board)) {
				Chess.movePiece(movingPiece, moveTo); // Then move the piece
				Chess.currentPlayer = Player.white; // Switch player
				return null;  // Successfully moved so no message is needed
			} else {
				return ReturnPlay.Message.ILLEGAL_MOVE;
			}
		}
	}
}

