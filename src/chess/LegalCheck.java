package chess;

import java.util.ArrayList;

public class LegalCheck {

  public static boolean isLegalMove(String move, ArrayList<ReturnPiece> board) {

    // Parse move
    String startPos = move.substring(0, 2);
    String endPos = move.substring(3, 5);

    // Get piece that is moving
    ReturnPiece movingPiece = null;
    for (ReturnPiece piece : board) {
      if (piece.pieceFile.name() + piece.pieceRank == startPos) {
        movingPiece = piece;
        break;
      }
    }

    // Check if move is legal for that piece
    if (movingPiece != null) {
      switch (movingPiece.pieceType) {
        case WP:
        case BP:
          return isLegalPawnMove(movingPiece, startPos, endPos);
        case WR:
        case BR:
          return isLegalRookMove(movingPiece, startPos, endPos);
        // Add cases for other piece types
      }
    }

    // No piece at start pos or illegal move
    return false;
  }

  // Implement legality check for each piece type
  // Examples:

  private static boolean isLegalPawnMove(ReturnPiece pawn, String start, String end) {
    // Direction of the movie depends on the piece color
    int direction = (pawn.pieceType == ReturnPiece.PieceType.WP) ? 1 : -1;

    // Parse the file (column) and rank (row) of the start and end positions
    char startFile = start.charAt(0);
    char endFile = end.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    int endRank = Character.getNumericValue(end.charAt(1));

    // One Square Forward - Reg Pawn
    if (startFile == endFile && endRank == startRank + direction) {
      // check oif destination square is empty
      if (!isSquareOccupied(end, Chess.board)) {
        return true;
      }
    }

    // Initial pawn move: two squares forward from the starting position
    if (startFile == endFile && endRank == startRank + 2 * direction) {
      // Check if both the destination square and the square in between are empty
      String intermediateSquare = String.valueOf(startFile) + (startRank + direction);
      if (!isSquareOccupied(intermediateSquare, Chess.board) && !isSquareOccupied(end, Chess.board)) {
        return true;
      }
    }
    // Pawn capturing moves: diagonal
    if (Math.abs(startFile - endFile) == 1 && endRank == startRank + direction) {
      // Check if the destination square contains an opponent's piece
      if (isSquareOccupiedByOpponent(end, Chess.board, pawn.pieceType)) {
        return true;
      }
    }
    return false;
  }

  // Helper methods

  private static boolean isSquareOccupied(String position, ArrayList<ReturnPiece> board) {
    for (ReturnPiece piece : board) {
      if (piece.pieceFile.name() + piece.pieceRank == position) {
        return true;
      }
    }
    return false;
  }

  private static boolean isPathClear(String start, String end, ArrayList<ReturnPiece> board) {
    // Implement logic to check if the path between two positions is clear of pieces
    // Return true if the path is clear, otherwise return false
  }

  private static boolean isLegalRookMove(ReturnPiece rook, String start, String end) {
    // Rook move rules
  }

  // Other piece move check methods

}
