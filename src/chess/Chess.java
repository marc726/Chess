package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

class ReturnPiece { // DO NOT ADD, DELETE, MODIFY
	static enum PieceType {
		WP, WR, WN, WB, WQ, WK,
		BP, BR, BN, BB, BK, BQ
	};

	static enum PieceFile {
		a, b, c, d, e, f, g, h
	};

	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank; // 1..8

	public String toString() {
		return "" + pieceFile + pieceRank + ":" + pieceType;
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece) other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay { // DO NOT ADD, DELETE, MODIFY
	enum Message {
		ILLEGAL_MOVE, DRAW,
		RESIGN_BLACK_WINS, RESIGN_WHITE_WINS,
		CHECK, CHECKMATE_BLACK_WINS, CHECKMATE_WHITE_WINS,
		STALEMATE
	};

	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {

	enum Player {
		white, black
	} // DO NOT CHANGE THIS LINE

	public static ArrayList<ReturnPiece> board; // create board
	public static Player currentPlayer; // create the current player
	public static Map<ReturnPiece, Boolean> hasMoved = new HashMap<>();
	public static String lastMove = null;

	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for
	 *         details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {

		ReturnPlay result = new ReturnPlay();
		result.piecesOnBoard = new ArrayList<>(board); // Copy the current state at the beginning

		// Handle draw requests
		boolean requestingDraw = move.endsWith(" draw?");
		if (requestingDraw) {
			move = move.substring(0, move.length() - 6).trim(); // Remove " draw?" from the move
		}

		// Check if the input format is valid after checking for "resign, reset"
		if (!InputValidation.inputCheck(move)) {
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return result;
		}

			// Process move
	
			result.message = ProcessMove.processMove(move);

		// Handle draw requests at the end of the move processing
		if (requestingDraw && result.message == null) { // only set draw message if there isn't another message already
														// set
			result.message = ReturnPlay.Message.DRAW;
		}

		return result;
	}

	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {

		board = new ArrayList<>();
		currentPlayer = Player.white;

		//special pieces								file is letter, rank is number. Ex. pawn at a2 on board. a=file, 2=rank
		addToBoard(PieceType.WR, PieceFile.a, 1); 
		addToBoard(PieceType.WN, PieceFile.b, 1);
		addToBoard(PieceType.WB, PieceFile.c, 1);
		addToBoard(PieceType.WQ, PieceFile.d, 3);
		addToBoard(PieceType.WK, PieceFile.e, 1);
		addToBoard(PieceType.WB, PieceFile.f, 1);
		addToBoard(PieceType.WN, PieceFile.g, 1);
		addToBoard(PieceType.WR, PieceFile.h, 1);

		addToBoard(PieceType.BR, PieceFile.a, 8);
		addToBoard(PieceType.BN, PieceFile.b, 8);
		addToBoard(PieceType.BB, PieceFile.c, 8);
		addToBoard(PieceType.BQ, PieceFile.d, 8);
		addToBoard(PieceType.BK, PieceFile.e, 8);
		addToBoard(PieceType.BB, PieceFile.f, 8);
		addToBoard(PieceType.BN, PieceFile.g, 8);
		addToBoard(PieceType.BR, PieceFile.h, 8);


		// pawns
		for (PieceFile file : PieceFile.values()) {
			addToBoard(PieceType.WP, file, 2);
			addToBoard(PieceType.BP, file, 7);
		}

		currentPlayer = Player.white;
		PlayChess.printBoard(board);
		System.out.println();
	}

	private static void addToBoard(PieceType type, PieceFile file, int rank) {
		ReturnPiece piece = new ReturnPiece();
		piece.pieceType = type;
		piece.pieceFile = file;
		piece.pieceRank = rank;
		board.add(piece);
		hasMoved.put(piece, false);
	}

	// helper methods

	public static ReturnPiece getPieceAt(String position) {
		for (ReturnPiece piece : board) {
			if ((piece.pieceFile.name() + piece.pieceRank).equals(position)) { // check if moveFrom ==
																				// returnPiece.location (file + rank)
				return piece;
			}
		}
		return null;
	}

	public static void movePiece(ReturnPiece piece, String moveTo) {
		hasMoved.put(piece, true);
		lastMove = piece.pieceFile.name() + piece.pieceRank + " " + moveTo; // Store the last move
		piece.pieceFile = PieceFile.valueOf(String.valueOf(moveTo.charAt(0)));
		piece.pieceRank = Character.getNumericValue(moveTo.charAt(1));
	}

	public static boolean hasMoved(ReturnPiece piece) {
		return hasMoved.getOrDefault(piece, false); // Check if the piece has moved
	}

	public static boolean isWhitePiece(PieceType pieceType) {
		return pieceType.name().charAt(0) == 'W';
	}

	public static boolean isInCheck(String position) {
		// Check if the king is in check
		for (ReturnPiece piece : board) {
			if (piece.pieceType.name().charAt(1) != 'K') { // Check if the piece is not a king
				if (LegalCheck.isLegalMove(piece.pieceFile.name() + piece.pieceRank + " " + position, board)) {
					System.out.println("Threatening Piece: " + piece.pieceType + " at " + piece.pieceFile.name()
							+ piece.pieceRank); // Printing the threatening piece
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isSquareAttacked(String position, ArrayList<ReturnPiece> board) {
		for (ReturnPiece piece : board) {
			if (!isPieceSameColor(piece, position)) {
				if (LegalCheck.isLegalMove(piece.pieceFile.name() + piece.pieceRank + " " + position, board)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isPieceSameColor(ReturnPiece piece, String position) {
		ReturnPiece king = Chess.getPieceAt(position);
		return isWhitePiece(piece.pieceType) == isWhitePiece(king.pieceType);
	}

	public static void takePiece(ReturnPiece piece, ArrayList<ReturnPiece> board) {
		if (piece != null && board.contains(piece)) {
			board.remove(piece);
		}
	}

	// Pawn Promotion

	public static void promotePawn(String move, ArrayList<ReturnPiece> board) {

		String startPos = move.substring(0, 2);
		String endPos = move.substring(3, 5);

		ReturnPiece pawn = Chess.getPieceAt(startPos);

		// Remove pawn
		board.remove(pawn);

		// Determine promotion piece type
		PieceType promoType = PieceType.WQ; // Default to queen
		if (move.length() == 5) {
			switch (move.charAt(4)) {
				case 'N':
					promoType = PieceType.WN;
					break;
				case 'B':
					promoType = PieceType.WB;
					break;
				case 'R':
					promoType = PieceType.WR;
					break;
			}
		}

		// Add promotion piece
		Chess.addToBoard(promoType, pawn.pieceFile, pawn.pieceRank);

		// Make the move
		Chess.movePiece(Chess.getPieceAt(endPos), endPos);

	}
}
