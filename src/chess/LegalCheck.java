package chess;
import java.util.ArrayList;
import chess.ReturnPiece.PieceType;


public class LegalCheck {

  public static boolean isLegalMove(String move, ArrayList<ReturnPiece> board) {

    // Parse move
    String startPos = move.substring(0, 2);
    String endPos = move.substring(3, 5);
    // Get piece that is moving
    ReturnPiece movingPiece = null;
    for (ReturnPiece piece : board) {
      if ((piece.pieceFile.name() + piece.pieceRank).equals(startPos)) {
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

    if (!Chess.isMoveValidBasedOnColor(pawn, end)) {
      return false; // Invalid move based on piece color
    }
    // Direction of the move depends on the piece color
    int direction = (pawn.pieceType == PieceType.WP) ? 1 : -1;


    // Parse the file (column) and rank (row) of the start and end positions
    char startFile = start.charAt(0);
    char endFile = end.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    int endRank = Character.getNumericValue(end.charAt(1));

    if (Math.abs(startFile - endFile) == 1 && endRank == startRank + direction) {
      // Check for normal capture
      if (isSquareOccupiedByOpponent(end, board, pawn.pieceType)) {
          // Also, remove the captured piece
          ReturnPiece capturedPiece = Chess.getPieceAt(end);
          Chess.board.remove(capturedPiece);
          return true;
      }
      // Check for en passant
      if (EnPassant.canEnPassant(pawn, end)) {
        // Also, remove the pawn being captured en passant
        ReturnPiece capturedPawn = Chess.getPieceAt(String.valueOf(endFile) + startRank);
        Chess.board.remove(capturedPawn);
        return true;
    }
  }

    // Initial pawn move: two squares forward from the starting position
    if (!Chess.hasMoved.getOrDefault(pawn, false) && endRank == startRank + 2 * direction) {
      // Check if both the destination square and the square in between are empty
      String intermediateSquare = String.valueOf(startFile) + (startRank + direction);
      if (!isSquareOccupied(intermediateSquare, board) && !isSquareOccupied(end, board)) {
        return true;
      }
    }
    // Subsequent pawn moves: only one square forward
    else if (startFile == endFile && endRank == startRank + direction) {
      // Check if destination square is empty
      if (!isSquareOccupied(end, board)) {
        return true;
      }
    }

    // Pawn capturing moves: diagonal
    if (Math.abs(startFile - endFile) == 1 && endRank == startRank + direction) {
      // Check if the destination square contains an opponent's piece
      if (isSquareOccupiedByOpponent(end, board, pawn.pieceType)) {
        return true;
      }
    }

    return false;
  }

  private static boolean isLegalRookMove(ReturnPiece rook, String start, String end, ArrayList<ReturnPiece> board) {
    // Rook move rules
    // Parse files
    if (!Chess.isMoveValidBasedOnColor(rook, end)) {
      return false; // Invalid move based on piece color
    }
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
    if (!Chess.isMoveValidBasedOnColor(knight, end)) {
      return false; // Invalid move based on piece color
    }

    char startFile = start.charAt(0);
    char endFile = end.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    int endRank = Character.getNumericValue(end.charAt(1));

    // if move is L shape (2x1 or 1x2)
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
    
    if (!Chess.isMoveValidBasedOnColor(bishop, end)) {
      return false; // Invalid move based on piece color
    }

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
    if (!Chess.isMoveValidBasedOnColor(queen, end)) {
      return false; // Invalid move based on piece color
    }

    if (isLegalRookMove(queen, start, end, board) || isLegalBishopMove(queen, start, end, board)) {
      return true;
    }

    return false;
  }

  private static boolean isLegalKingMove(ReturnPiece king, String start, String end, ArrayList<ReturnPiece> board) {
    
    if (!Chess.isMoveValidBasedOnColor(king, end)) {
      return false; // Invalid move based on piece color
    }

    char startFile = start.charAt(0);
    char endFile = end.charAt(0);
    int startRank = Character.getNumericValue(start.charAt(1));
    int endRank = Character.getNumericValue(end.charAt(1));

    // one square in any direction
    int fileDiff = Math.abs(startFile - endFile);
    int rankDiff = Math.abs(startRank - endRank);

    //typical movement logic
    if (fileDiff <= 1 && rankDiff <= 1) {
      // dest is empty
      if (!isSquareOccupiedBySameColor(end, board, king.pieceType)) {
        if (isMoveSafeForKing(start, end, board)) {
          return true;
      }
      }
    }


    //castle movement logic
    if (rankDiff == 0 && (fileDiff == 2 || fileDiff == 3)) {
      // Here, check for the castling conditions.
      // For example: no pieces between king and rook, king is not in check, etc.
      if (Castle.canCastle(start + " " + end, board)) {
          return true;
      }
    }


    return false;
  }
  // Helper methods

  public static boolean isSquareOccupied(String position, ArrayList<ReturnPiece> board) {
    for (ReturnPiece piece : board) {
      if ((piece.pieceFile.name() + piece.pieceRank).equals(position)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isSquareOccupiedBySameColor(String square, ArrayList<ReturnPiece> board,
      ReturnPiece.PieceType movingPieceType) {
    for (ReturnPiece piece : board) {
      if (square.equals(piece.pieceFile.name() + piece.pieceRank)) {
        if (isWhitePiece(movingPieceType) == isWhitePiece(piece.pieceType)) {
          return true;
        }
      }
    }
    return false;
}

  public static boolean isSquareOccupiedByOpponent(String position, ArrayList<ReturnPiece> board,
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
  public static boolean isPathClear(String start, String end, ArrayList<ReturnPiece> board) {
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
=======
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
    else if (Math.abs(startFile - endFile) == Math.abs(startRank - endRank)) {
      // determine the direction of the diagonal
      int fileDirection = (startFile < endFile) ? 1 : -1;  // left to right or right to left
      int rankDirection = (startRank < endRank) ? 1 : -1;  // top to bottom or bottom to top

      // iterate over the diagonal squares
      char currentFile = (char) (startFile + fileDirection);
      int currentRank = startRank + rankDirection;
      while (currentFile != endFile && currentRank != endRank) {
          if (isSquareOccupied(Character.toString(currentFile) + currentRank, board)) {
              return false;  // path is blocked
          }
          currentFile += fileDirection;
          currentRank += rankDirection;
      }
  }

  return true;
  }


  public static ArrayList<String> getLegalMovesForKing(ReturnPiece king, ArrayList<ReturnPiece> board) {
    ArrayList<String> legalMoves = new ArrayList<String>();
    char file = king.pieceFile.name().charAt(0);
    int rank = king.pieceRank;

    // Check all squares around the king
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            // Skip the square the king is on
            if (i == 0 && j == 0) {
                continue;
            }

            // Calculate the file and rank of the square to check
            char checkFile = (char) (file + i);
            int checkRank = rank + j;

            // Check if the square is on the board
            if (checkFile < 'A' || checkFile > 'H' || checkRank < 1 || checkRank > 8) {
                continue;
            }

            // Get the piece at the square
            ReturnPiece pieceAtSquare = Chess.getPieceAt("" + checkFile + checkRank);

            // Check if the square is empty or contains an enemy piece
            if (pieceAtSquare == null || !Chess.isPieceSameColor(king, pieceAtSquare, board)) {
                legalMoves.add("" + file + rank + checkFile + checkRank);
            }
        }
    }

    return legalMoves;
  }
  public static void removePieceFromBoard(String position, ArrayList<ReturnPiece> board) {
    ReturnPiece pieceToRemove = null;
    for (ReturnPiece piece : board) {
        if (position.equals(piece.pieceFile.name() + piece.pieceRank)) {
            pieceToRemove = piece;
            break;
        }
    }
    if (pieceToRemove != null) {
        board.remove(pieceToRemove);
  }
  }


  public static boolean isMoveSafeForKing(String kingPosition, String targetPosition, ArrayList<ReturnPiece> board) {
    // 1. Virtually move the king to the target position:
    ReturnPiece king = Chess.getPieceAt(kingPosition);
    ReturnPiece targetPiece = Chess.getPieceAt(targetPosition);
    
    // If there's a piece on the target square, temporarily remove it
    if (targetPiece != null) {
        board.remove(targetPiece);
    }
    
    Chess.movePiece(king, targetPosition);
    
    // 2. Check if the king's new position is under attack by any opposing pieces:
    boolean isSafe = !Chess.isSquareAttacked(targetPosition, board);
    
    // 3. Revert the virtual move:
    Chess.movePiece(king, kingPosition);
    if (targetPiece != null) {
        board.add(targetPiece);
    }
    
    return isSafe;
  }
}