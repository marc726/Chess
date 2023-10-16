/*
 * Authors:
 *  		* Marcc Rizzolo (NETID)
 * 		    * Bhavya Patel (bsp75)
 */
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
		            //move priority illegal move -> draw -> reset/resign -> checkmate/check 

		ReturnPlay result = new ReturnPlay();
		move = move.toLowerCase().trim();
		
		// Check if the input format is valid after checking for "resign, reset"
		if (!InputValidation.inputCheck(move)) {
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
		}

		// Process move
		if(!move.equals("resign") && !move.equals("reset")){
			result.message = ProcessMove.processMove(move);
		}

		// Handle draw after a legal move is made
		if ((result.message != ReturnPlay.Message.ILLEGAL_MOVE) && (move.endsWith(" draw?" ))) {
			result.message = ReturnPlay.Message.DRAW;
		}

		// check for reset and resign requests
		if (move.equals("resign")) {
			if (currentPlayer == Player.white) {
				result.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
			} else {
				result.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
			}
		} else if (move.equals("reset")) {
			start();
		}

		result.piecesOnBoard = new ArrayList<>(board); 
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
		addToBoard(PieceType.WQ, PieceFile.d, 1);
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

	public static void addToBoard(PieceType type, PieceFile file, int rank) {
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
			String piecePosition = piece.pieceFile.name() + piece.pieceRank;
			if (piecePosition.equals(position)) {
				return piece;
			}
		}
		return null;
	}

	//already checked if piece is null
	public static void movePiece(ReturnPiece piece, String moveTo) {
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
					return true;
				}
			}
		}
		return false;
	}


	public static boolean isSquareAttacked(String moveToPosition, ArrayList<ReturnPiece> board, ReturnPiece movingPiece) {
		for (ReturnPiece piece : board) {
			if (piece.equals(movingPiece)) continue; // Exclude the moving piece from the check
			if (!isPieceSameColor(piece, movingPiece)) {
				if (LegalCheck.isLegalMove(piece.pieceFile.name() + piece.pieceRank + " " + moveToPosition, board)) {
					return true;
				}
			}
		}
		return false;
		}		
	
		public static boolean isSquareAttacked(String targetPosition, ArrayList<ReturnPiece> board) {
			for (ReturnPiece piece : board) {
				// If it's the same color as the king we're checking, skip this piece.
				if (Chess.isWhitePiece(piece.pieceType) == Chess.isWhitePiece(Chess.getPieceAt(targetPosition).pieceType)) {
					continue;
				}
				
				// Check if the opposing piece can attack the target position.
				if (LegalCheck.isLegalMove(piece.pieceFile.name() + piece.pieceRank + " " + targetPosition, board)) {
					System.out.println("Piece " + piece.pieceType + " at " + piece.pieceFile + piece.pieceRank + " can attack " + targetPosition);
					return true;
				}
			}
			return false;
		}

	private static boolean isPieceSameColor(ReturnPiece threatPiece, ReturnPiece movingPiece) {
		
		if (movingPiece == null || threatPiece == null) {
			return false;
		}

		return isWhitePiece(movingPiece.pieceType) == isWhitePiece(threatPiece.pieceType);
	}

	public static String getKingPos(Player opposingPlayer) {
		for (ReturnPiece piece : board) {
			if (piece.pieceType.name().charAt(1) == 'K') { // Checking if the piece is a King
				if (isWhitePiece(piece.pieceType) && opposingPlayer == Player.black) { // If it's a white king and we want the black king
					continue;
				} else if (!isWhitePiece(piece.pieceType) && opposingPlayer == Player.white) { // If it's a black king and we want the white king
					continue;
				}
				return piece.pieceFile.name() + piece.pieceRank;  // Return the found king's position
			}
		}
		return null;
	}

	public static boolean isPieceSameColor(ReturnPiece piece1, ReturnPiece piece2, ArrayList<ReturnPiece> board) {
		if (piece1 == null || piece2 == null) {
			return false;
		}
	
		for (ReturnPiece piece : board) {
			if (piece.pieceFile == piece1.pieceFile && piece.pieceRank == piece1.pieceRank) {
				if (piece.pieceType.name().charAt(0) == piece2.pieceType.name().charAt(0)) {
					return true;
				} else {
					return false;
				}
			}
		}
	
		return false;
	}


	public static boolean isMoveValidBasedOnColor(ReturnPiece movingPiece, String position) {
		ReturnPiece pieceAtPosition = Chess.getPieceAt(position);
		
		// Case 1: The destination square is empty
		if (pieceAtPosition == null) {
			return true;
		}
		
		// Case 2 & 3: Compare the colors of the moving piece and the piece at the destination
		boolean isSameColor = isWhitePiece(movingPiece.pieceType) == isWhitePiece(pieceAtPosition.pieceType);
		return !isSameColor; // If they are of the same color, return false (invalid move). Otherwise, return true.
	}

	public static String findPiecePosition(ReturnPiece targetPiece, ArrayList<ReturnPiece> board) {
		for (ReturnPiece piece : board) {
			if (piece.equals(targetPiece)) {
				return piece.pieceFile.name() + piece.pieceRank;
			}
		}
		return null;
	}


	public static String getWhiteKingPos() {
		for (ReturnPiece piece : board) {
			if (piece.pieceType.name().charAt(1) == 'K' && isWhitePiece(piece.pieceType)) {
				return piece.pieceFile.name() + piece.pieceRank;
			}
		}
		return null;
	}
	
	public static String getBlackKingPos() {
		for (ReturnPiece piece : board) {
			if (piece.pieceType.name().charAt(1) == 'K' && !isWhitePiece(piece.pieceType)) {
				return piece.pieceFile.name() + piece.pieceRank;
			}
		}
		return null;
	}
}


/*Did you ever hear the tragedy of Darth Plagueis The Wise? I thought not. It’s not a story the Jedi would tell you. It’s a Sith legend. 
Darth Plagueis was a Dark Lord of the Sith, so powerful and so wise he could use the Force to influence the midichlorians to create life… 
He had such a knowledge of the dark side that he could even keep the ones he cared about from dying. 
The dark side of the Force is a pathway to many abilities some consider to be unnatural. He became so powerful… 
the only thing he was afraid of was losing his power, which eventually, of course, he did. Unfortunately, he taught his apprentice everything he knew, 
then his apprentice killed him in his sleep. Ironic. He could save others from death, but not himself. 

⠀⠀⠘⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡜⠀⠀⠀
⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡔⠁⠀⠀⠀
⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠴⠊⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⠀⢀⣀⣀⣀⣀⣀⡀⠤⠄⠒⠈⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠘⣀⠄⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀
⣿⣿⣿⣿⣿⣿⣿⣿⡿⠿⠛⠛⠛⠋⠉⠈⠉⠉⠉⠉⠛⠻⢿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⡿⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⢿⣿⣿⣿⣿
⣿⣿⣿⣿⡏⣀⠀⠀⠀⠀⠀⠀⠀⣀⣤⣤⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠙⢿⣿⣿
⣿⣿⣿⢏⣴⣿⣷⠀⠀⠀⠀⠀⢾⣿⣿⣿⣿⣿⣿⡆⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿
⣿⣿⣟⣾⣿⡟⠁⠀⠀⠀⠀⠀⢀⣾⣿⣿⣿⣿⣿⣷⢢⠀⠀⠀⠀⠀⠀⠀⢸⣿
⣿⣿⣿⣿⣟⠀⡴⠄⠀⠀⠀⠀⠀⠀⠙⠻⣿⣿⣿⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⣿
⣿⣿⣿⠟⠻⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠶⢴⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⣿
⣿⣁⡀⠀⠀⢰⢠⣦⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⣿⣿⣿⣿⣿⡄⠀⣴⣶⣿⡄⣿
⣿⡋⠀⠀⠀⠎⢸⣿⡆⠀⠀⠀⠀⠀⠀⣴⣿⣿⣿⣿⣿⣿⣿⠗⢘⣿⣟⠛⠿⣼
⣿⣿⠋⢀⡌⢰⣿⡿⢿⡀⠀⠀⠀⠀⠀⠙⠿⣿⣿⣿⣿⣿⡇⠀⢸⣿⣿⣧⢀⣼
⣿⣿⣷⢻⠄⠘⠛⠋⠛⠃⠀⠀⠀⠀⠀⢿⣧⠈⠉⠙⠛⠋⠀⠀⠀⣿⣿⣿⣿⣿
⣿⣿⣧⠀⠈⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠀⠀⠀⠀⢀⢃⠀⠀⢸⣿⣿⣿⣿
⣿⣿⡿⠀⠴⢗⣠⣤⣴⡶⠶⠖⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡸⠀⣿⣿⣿⣿
⣿⣿⣿⡀⢠⣾⣿⠏⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠛⠉⠀⣿⣿⣿⣿
⣿⣿⣿⣧⠈⢹⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⣿⣿⣿
⣿⣿⣿⣿⡄⠈⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣴⣾⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣦⣄⣀⣀⣀⣀⠀⠀⠀⠀⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡄⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠙⣿⣿⡟⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇⠀⠁⠀⠀⠹⣿⠃⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⡿⠛⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⢐⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⠿⠛⠉⠉⠁⠀⢻⣿⡇⠀⠀⠀⠀⠀⠀⢀⠈⣿⣿⡿⠉⠛⠛⠛⠉⠉
⣿⡿⠋⠁⠀⠀⢀⣀⣠⡴⣸⣿⣇⡄⠀⠀⠀⠀⢀⡿⠄⠙⠛⠀⣀⣠⣤⣤⠄⠀


*/


