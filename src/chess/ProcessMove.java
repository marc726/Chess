package chess;

import chess.Chess.Player;

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
			Chess.promotePawn(move, Chess.board);
			Chess.currentPlayer = (Chess.currentPlayer == Player.white) ? Player.black : Player.white;
			return null;
		}

        //whites move
		if (Chess.currentPlayer == Player.white) {
			if (movingPiece != null && Chess.isWhitePiece(movingPiece.pieceType) && LegalCheck.isLegalMove(move, Chess.board)) {
                ReturnPiece pieceAtTarget = Chess.getPieceAt(moveTo);
                if (pieceAtTarget != null && !Chess.isWhitePiece(pieceAtTarget.pieceType)) {
                    // The space is occupied by a black piece.
                    Chess.takePiece(pieceAtTarget, Chess.board);  // Assuming you want to remove the piece.
                }  
				Chess.movePiece(movingPiece, moveTo);
				Chess.currentPlayer = Player.black; // Switch player
				return null;  // Successfully moved so no msg needed
			} else {
				return ReturnPlay.Message.ILLEGAL_MOVE;
			}



		} else {  //blacks move
			if (movingPiece != null && !Chess.isWhitePiece(movingPiece.pieceType) && LegalCheck.isLegalMove(move, Chess.board)) {
				ReturnPiece pieceAtTarget = Chess.getPieceAt(moveTo);
                if (pieceAtTarget != null && Chess.isWhitePiece(pieceAtTarget.pieceType)) {
                    // The space is occupied by a black piece.
                    Chess.takePiece(pieceAtTarget, Chess.board);  // Assuming you want to remove the piece.
                }  
                Chess.movePiece(movingPiece, moveTo);
				Chess.currentPlayer = Player.white; // Switch player
				return null;  // Successfully moved so no msg needed
			} else {
				return ReturnPlay.Message.ILLEGAL_MOVE;
			}
		}
	}
}
