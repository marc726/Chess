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
          return isLegalPawnMove(movingPiece, startPos, endPos, board);
        case WR:
        case BR:
          return isLegalRookMove(movingPiece, startPos, endPos, board);
        case WN:
        case BN:
          return isLegalKnightMove(movingPiece, startPos, endPos, board);
        case WB:
        case BB:
          return isLegalBishopMove(movingPiece, startPos, endPos, board);
        case WQ:
        case BQ:
          return isLegalQueenMove(movingPiece, startPos, endPos, board);
        case WK:
        case BK:
          return isLegalKingMove(movingPiece, startPos, endPos, board);
      }
    }
    // No piece at start pos or illegal move
    return false;
  }

  // Implement legality check for each piece type
  // Examples:

  private static boolean isLegalPawnMove(ReturnPiece pawn, String start, String end, ArrayList<ReturnPiece> board) {
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
      if (!isSquareOccupied(end, board)) {
        return true;
      }
    }

    // Initial pawn move: two squares forward from the starting position
    if (startFile == endFile && endRank == startRank + 2 * direction) {
      // Check if both the destination square and the square in between are empty
      String intermediateSquare = String.valueOf(startFile) + (startRank + direction);
      if (!isSquareOccupied(intermediateSquare, board) && !isSquareOccupied(end, Chess.board)) {
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

  private static boolean isLegalRookMove(ReturnPiece rook, String start, String end, ArrayList<ReturnPiece> board) {
    // Rook move rules
    // Parse files
    char startFile = start.charAt(0);
    char endFile = end.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    int endRank = Character.getNumericValue(end.charAt(1));

    // check if move is horizontal or vertical
    if (startFile == endFile || startRank == endRank) {
      // if theres a piece in the way
      if (isPathClear(start, end, board)) {
        // check of destination square is empty
        if (!isSquareOccupiedBySameColor(end, board, rook.pieceType)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isLegalKnightMove(ReturnPiece knight, String start, String end, ArrayList<ReturnPiece> board) {
    // parse again
    char startFile = start.charAt(0);
    char endFile = end.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    int endRank = Character.getNumericValue(end.charAt(1));

    // if move is L shape
    int fileDiff = Math.abs(startFile - endFile);
    int rankDiff = Math.abs(startRank - endRank);
    if ((fileDiff == 1 && rankDiff == 2) || (fileDiff == 2 && rankDiff == 1)) {
      // check if destination square is empty
      if (!isSquareOccupiedBySameColor(end, board, knight.pieceType)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isLegalBishopMove(ReturnPiece bishop, String start, String end, ArrayList<ReturnPiece> board) {
    // parse
    char startFile = start.charAt(0);
    char endFile = end.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    int endRank = Character.getNumericValue(end.charAt(1));

    // check if move is diagonal
    if (Math.abs(startFile - endFile) == Math.abs(startRank - endRank)) {
      // if piece in the way
      if (isPathClear(start, end, board)) {
        // check if destination square is empty
        if (!isSquareOccupiedBySameColor(end, board, bishop.pieceType)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isLegalQueenMove(ReturnPiece queen, String start, String end, ArrayList<ReturnPiece> board) {
    // queen can move like rook or bishop
    if (isLegalRookMove(queen, start, end, board) || isLegalBishopMove(queen, start, end, board)) {
      return true;
    }

    return false;
  }

  private static boolean isLegalKingMove(ReturnPiece king, String start, String end, ArrayList<ReturnPiece> board) {
    // parse
    char startFile = start.charAt(0);
    char endFile = end.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    int endRank = Character.getNumericValue(end.charAt(1));

    // one square in any direction
    int fileDiff = Math.abs(startFile - endFile);
    int rankDiff = Math.abs(startRank - endRank);
    if (fileDiff <= 1 && rankDiff <= 1) {
      // dest is empty
      if (!isSquareOccupiedBySameColor(end, board, king.pieceType)) {
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

  private static boolean isSquareOccupiedBySameColor(String square, ArrayList<ReturnPiece> board,
      ReturnPiece.PieceType pieceType) {
    for (ReturnPiece piece : board) {
      if (square.equals(piece.pieceFile.name() + piece.pieceRank) && piece.pieceType == pieceType) {
        return true;
      }
    }
    return false;
  }

  private static boolean isSquareOccupiedByOpponent(String position, ArrayList<ReturnPiece> board,
      ReturnPiece.PieceType currentPieceType) {
    for (ReturnPiece piece : board) {
      if ((piece.pieceFile.name() + piece.pieceRank).equals(position)) {
        // Check color of the occupying piece
        if (isWhitePiece(currentPieceType) && !isWhitePiece(piece.pieceType)) { // White Piece -> Black
          return true;
        } else if (!isWhitePiece(currentPieceType) && isWhitePiece(piece.pieceType)) { // Black Piece -> White
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isWhitePiece(ReturnPiece.PieceType pieceType) {
    // Assuming that piece types for white pieces have a 'W' prefix (e.g., WP for
    // white pawn)
    return pieceType.name().charAt(0) == 'W';
  }

  // assume that we've checked if the piece can make that move already. Knights
  // are excluded from this
  private static boolean isPathClear(String start, String end, ArrayList<ReturnPiece> board) {
    // Implement logic to check if the path between two positions is clear of pieces
    // Return true if the path is clear, otherwise return false

    char startFile = start.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    char endFile = end.charAt(0);
    int endRank = Character.getNumericValue(end.charAt(1));

    // horizontal
    if (startRank == endRank) {

      char leastFile = (char) Math.min(startFile, endFile); // go from this
      char mostFile = (char) Math.max(startFile, endFile); // to this

      for (char file = (char) (leastFile + 1); file < mostFile; file++) { // leastFile+1 bc we dont check the square the
                                                                          // piece is on
        if (isSquareOccupied(Character.toString(file) + startRank, board)) { // rank doesn't change since horizontal
          return false;
        }
      }

      // vertical
    } else if (startFile == endFile) {

      int leastRank = Math.min(startRank, endRank);
      int mostRank = Math.max(startRank, endRank);

      for (int rank = leastRank + 1; rank < mostRank; rank++) {
        if (isSquareOccupied(Character.toString(startFile) + rank, board)) { // file doesn't change since verical
          return false;
        }
      }
    }

    // diagonal
    return true;
  }

}