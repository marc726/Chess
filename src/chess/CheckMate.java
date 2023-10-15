package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class CheckMate {

    public static boolean isPlayerInCheckmate(ArrayList<ReturnPiece> board, Chess.Player currentPlayer) {
        // Check if the king is in check
        if (isKingInCheck(board, currentPlayer)) {
            // Check if there are no legal moves left for the current player that would get
            // the king out of check
            for (ReturnPiece piece : board) {
                if ((currentPlayer == Chess.Player.white && piece.pieceType.name().startsWith("W")) ||
                        (currentPlayer == Chess.Player.black && piece.pieceType.name().startsWith("B"))) {
                    ArrayList<String> legalMoves = generateLegalMovesForPiece(piece, board);
                    for (String move : legalMoves) {
                        // Create a temporary board to simulate the move
                        ArrayList<ReturnPiece> tempBoard = simulateMove(board, piece, move);
                        // If the king is not in check after this move, then it's not checkmate
                        if (isKingInCheck(tempBoard, currentPlayer)) {
                            return false;
                        }
                    }
                }
            }
            return true; // No legal moves found that can get the king out of check
        }
        return false;
    }

    public static boolean isKingInCheck(ArrayList<ReturnPiece> board, Chess.Player currentPlayer) {
        // Identify the position of the king
        String kingPosition = null;
        for (ReturnPiece piece : board) {
            if ((currentPlayer == Chess.Player.white && piece.pieceType == ReturnPiece.PieceType.WK) ||
                    (currentPlayer == Chess.Player.black && piece.pieceType == ReturnPiece.PieceType.BK)) {
                kingPosition = piece.pieceFile.name() + piece.pieceRank;
                break;
            }
        }

        // Check all potential attacking moves from the opposing player
        for (ReturnPiece piece : board) {
            if ((currentPlayer == Chess.Player.white && piece.pieceType.name().startsWith("B")) ||
                    (currentPlayer == Chess.Player.black && piece.pieceType.name().startsWith("W"))) {
                // Generate all legal moves for this piece
                // We would call the appropriate legal move method for each piece type here
                ArrayList<String> legalMoves = generateLegalMovesForPiece(piece, board);
                if (legalMoves.contains(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Placeholder for the generateLegalMovesForPiece method. This method will need
    // to be implemented
    // to call the appropriate method (like isLegalPawnMove, isLegalRookMove, etc.)
    // based on the piece type.
    private static ArrayList<String> generateLegalMovesForPiece(ReturnPiece piece, ArrayList<ReturnPiece> board) {
        ArrayList<String> legalMoves = new ArrayList<>();

        switch (piece.pieceType) {
            case WP:
            case BP:
                legalMoves.addAll(generateLegalPawnMoves(piece, board));
                break;
            case WR:
            case BR:
                legalMoves.addAll(generateLegalRookMoves(piece, board));
                break;
            case WN:
            case BN:
                legalMoves.addAll(generateLegalKnightMoves(piece, board));
                break;
            case WB:
            case BB:
                legalMoves.addAll(generateLegalBishopMoves(piece, board));
                break;
            case WQ:
            case BQ:
                legalMoves.addAll(generateLegalQueenMoves(piece, board));
                break;
            case WK:
            case BK:
                legalMoves.addAll(generateLegalKingMoves(piece, board));
                break;
        }

        return legalMoves;
    }

    // Placeholder for the simulateMove method. This method will apply the move on a
    // copy of the board and return the updated board.
    private static ArrayList<ReturnPiece> simulateMove(ArrayList<ReturnPiece> board, ReturnPiece piece, String move) {
        ArrayList<ReturnPiece> tempBoard = new ArrayList<>(board);
        // Implement the logic to update the tempBoard based on the move
        return tempBoard;
    }

    public static ArrayList<String> generateLegalPawnMoves(ReturnPiece pawn, ArrayList<ReturnPiece> board) {
        ArrayList<String> moves = new ArrayList<>();

        int direction = (pawn.pieceType == PieceType.WP) ? 1 : -1;
        int startRank = pawn.pieceRank;
        char file = pawn.pieceFile.name().charAt(0);

        // Check one square forward
        int endRank = startRank + direction;
        if (isSquareEmpty(file, endRank, board)) {
            moves.add(String.valueOf(file) + startRank + String.valueOf(file) + endRank);
        }

        // Check two squares forward from starting position
        if (!Chess.hasMoved(pawn) && isSquareEmpty(file, endRank + direction, board)) {
            moves.add(String.valueOf(file) + startRank + String.valueOf(file) + (endRank + direction)); 
          }

        // Check diagonal squares for captures
        for (int i = -1; i <= 1; i += 2) {
            char endFile = (char) (file + i);
            if (isSquareOccupiedByEnemy(endFile, endRank, board, pawn)) {
                moves.add(String.valueOf(file) + startRank + String.valueOf(endFile) + endRank);
            }
        }

        // Add en passant if applicable
        char targetFile = (direction == 1) ? (char) (file + 1) : (char) (file - 1); // Adjust based on your game logic
        String targetPosition = targetFile + String.valueOf(endRank + direction);
        if (EnPassant.canEnPassant(pawn, targetPosition)) {
            char epFile = targetFile;  // Adjust this if needed
            moves.add(String.valueOf(file) + startRank + epFile + (startRank + direction));
        }

        return moves;
    }

    public static ArrayList<String> generateLegalRookMoves(ReturnPiece rook, ArrayList<ReturnPiece> board) {
        ArrayList<String> moves = new ArrayList<>();
        int rank = rook.pieceRank;
        char file = rook.pieceFile.name().charAt(0);

        // Check horizontal moves
        for (char f = 'A'; f <= 'H'; f++) {
            if (f != file && !isSquareBlocked(f, rank, board, rook)) {
                moves.add(String.valueOf(file) + rank + String.valueOf(f) + rank);
            }
        }

        // Check vertical moves
        for (int r = 1; r <= 8; r++) {
            if (r != rank && !isSquareBlocked(file, r, board, rook)) {
                moves.add(String.valueOf(file) + rank + String.valueOf(file) + r);
            }
        }

        return moves;
    }

    public static ArrayList<String> generateLegalKnightMoves(ReturnPiece knight, ArrayList<ReturnPiece> board) {
        ArrayList<String> moves = new ArrayList<>();
        int rank = knight.pieceRank;
        char file = knight.pieceFile.name().charAt(0);

        // Check L-shaped moves
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) + Math.abs(j) == 3) { // Ensures L-shape movement
                    checkKnightMove(file, rank, moves, (char) (file + i), rank + j, board);
                }
            }
        }

        return moves;
    }

    public static ArrayList<String> generateLegalBishopMoves(ReturnPiece bishop, ArrayList<ReturnPiece> board) {
        ArrayList<String> moves = new ArrayList<>();
        int rank = bishop.pieceRank;
        char file = bishop.pieceFile.name().charAt(0);

        // Check diagonal moves
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                for (int k = 1; k <= 8; k++) {
                    char endFile = (char) (file + i * k);
                    int endRank = rank + j * k;
                    if (isSquareEmpty(endFile, endRank, board)) {
                        moves.add(String.valueOf(file) + rank + endFile + endRank);
                    } else if (isSquareOccupiedByEnemy(endFile, endRank, board, bishop)) {
                        moves.add(String.valueOf(file) + rank + endFile + endRank);
                        break; // Stop in this diagonal direction if blocked
                    } else {
                        break; // Stop if blocked by friendly piece
                    }
                }
            }
        }

        return moves;
    }

    public static ArrayList<String> generateLegalQueenMoves(ReturnPiece queen, ArrayList<ReturnPiece> board) {
        ArrayList<String> moves = new ArrayList<>();
        moves.addAll(generateLegalRookMoves(queen, board));
        moves.addAll(generateLegalBishopMoves(queen, board));
        return moves;
    }

    public static ArrayList<String> generateLegalKingMoves(ReturnPiece king, ArrayList<ReturnPiece> board) {
        ArrayList<String> moves = new ArrayList<>();
        int rank = king.pieceRank;
        char file = king.pieceFile.name().charAt(0);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                int endRank = rank + i;
                char endFile = (char) (file + j);
                if (isSquareEmpty(endFile, endRank, board)) {
                    moves.add(String.valueOf(file) + rank + endFile + endRank);
                }
            }
        }

        // Check for castling
        if (!Chess.hasMoved.get(king)) {
            // Kingside castling
            if (!isSquareBlocked(file, rank, board, king)
                    && canCastleKingside(board)) {
                moves.add(file + rank + "g" + rank);
            }
            // Queenside castling
            if (!isSquareBlocked(file, rank, board, king)
                    && canCastleQueenside(board)) {
                moves.add(file + rank + "c" + rank);
            }
        }

        return moves;
    }

    public static void checkKnightMove(char startFile, int startRank, ArrayList<String> moves, char endFile, int endRank, ArrayList<ReturnPiece> board) {
        String startPos = "" + startFile + startRank;
        // Check if target square is on the board
        if (endFile < 'A' || endFile > 'H' || startRank < 1 || startRank > 8) {
            return;
        }

        // Check if target square is empty
        if (Chess.getPieceAt(endFile + "") == null) {
            moves.add(String.valueOf(startFile) + startRank + endFile + startRank);
        }

        // Check if target square contains enemy piece
        else {
            ReturnPiece endPiece = Chess.getPieceAt(endFile + String.valueOf(endRank));
            ReturnPiece knight = Chess.getPieceAt(startFile + String.valueOf(startRank));
            if (!Chess.isPieceSameColor(endPiece, knight, board)) {
            moves.add(startPos + endFile + startRank);
            }
        }
    }

    // Helper methods
    public static boolean isSquareEmpty(char file, int rank, ArrayList<ReturnPiece> board) {
        // Check if square is occupied by a piece
        return Chess.getPieceAt(file + String.valueOf(rank)) == null;
    }

    public static boolean isSquareOccupiedByEnemy(char file, int rank, ArrayList<ReturnPiece> board,
            ReturnPiece piece) {
        ReturnPiece other = Chess.getPieceAt(file + String.valueOf(rank));
        return other != null && !Chess.isPieceSameColor(piece, other, board);
    }

    public static boolean isSquareBlocked(char file, int rank, ArrayList<ReturnPiece> board, ReturnPiece piece) {
        // Check if any pieces of same color are between the piece's file/rank and the
        // target square
        if (piece == null) {
            return false;
        }
        return !LegalCheck.isPathClear(piece.pieceFile.name() + piece.pieceRank, file + String.valueOf(rank), board);
    }

    public static boolean canCastleKingside(ArrayList<ReturnPiece> board) {
        // Check if king can castle kingside
        return !isSquareBlocked('f', 1, board, null)
                && !isSquareBlocked('g', 1, board, null);
    }

    public static boolean canCastleQueenside(ArrayList<ReturnPiece> board) {
        // Check if king can castle queenside
        return !isSquareBlocked('b', 1, board, null)
                && !isSquareBlocked('c', 1, board, null)
                && !isSquareBlocked('d', 1, board, null);
    }
}
