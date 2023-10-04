package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

class ReturnPiece { //DO NOT ADD, DELETE, MODIFY
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay { //DO NOT ADD, DELETE, MODIFY
	enum Message {ILLEGAL_MOVE, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {
	
	enum Player { white, black } //DO NOT CHANGE

	public static ArrayList<ReturnPiece> board; //create board
	public static Player currentPlayer; // create the current player
	
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {

		ReturnPlay result = new ReturnPlay();
    	result.piecesOnBoard = new ArrayList<>(board); // Copy the current state at the beginning
		
		// Check if the input format is valid after checking for "resign, reset, etc."
		
		//input isn't valid
		if (InputValidation.inputCheck(move) == false){ 
			result.message = ReturnPlay.Message.ILLEGAL_MOVE;
		}else{ 
			//input is either, resign, reset, or valid piece move
			switch(move.toLowerCase().trim()) {
				case "reset":
					start();
					break;
				case "resign": 
					if(currentPlayer == Player.white) { // set winning color
						result.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
					} else {
						result.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
					}
					break; 
				case "draw":
					result.message = ReturnPlay.Message.DRAW;
					break;
				default: 
				
				//make move
				String moveFrom = move.substring(0, 2);   //example: "a1 a2" -> "a1" "a2"
				String moveTo = move.substring(3, 5);
				
				// We need a legality check here

				ReturnPiece movingPiece = getPieceAt(moveFrom);
                if (movingPiece == null) { 
                    result.message = ReturnPlay.Message.ILLEGAL_MOVE;
                }else{
					movePiece(movingPiece, moveTo);
				}
				break;
			}
		}

		return result;
	}

	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {

		board = new ArrayList<>(); 
		currentPlayer = Player.white;

		//special pieces
		addToBoard(PieceType.WR, PieceFile.a, 1); //file is letter, rank is number. Ex. pawn at a2 on board. a=file, 2=rank
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

		//pawns
		for (PieceFile file : PieceFile.values()){
			addToBoard(PieceType.WP, file, 2);
			addToBoard(PieceType.BP, file, 7);
		}

		PlayChess.printBoard(PlayChess.getCurrentBoardState());
	}
	private static void addToBoard(PieceType type, PieceFile file, int rank){
		ReturnPiece piece = new ReturnPiece();
		piece.pieceType = type;
		piece.pieceFile = file;
		piece.pieceRank = rank;
		board.add(piece);
	}

	//helper methods 

	private static ReturnPiece getPieceAt(String position) {
		for (ReturnPiece piece : board) {
			if ((piece.pieceFile.name() + piece.pieceRank).equals(position)) { //check if moveFrom == returnPiece.location (file + rank)
				return piece;
			}
		}
		return null; 
	}
	
	private static void movePiece(ReturnPiece piece, String moveTo) {
		piece.pieceFile = PieceFile.valueOf(String.valueOf(moveTo.charAt(0)));
		piece.pieceRank = Character.getNumericValue(moveTo.charAt(1));
	}
}

