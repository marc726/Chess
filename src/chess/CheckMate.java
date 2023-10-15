package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class CheckMate {

    public static boolean isPlayerInCheckmate(ArrayList<ReturnPiece> board, Chess.Player currentPlayer) {
        // Check if the king is in check
        if (isKingInCheck(board, currentPlayer)) {
            // Check if there are no legal moves left for the current player that would get the king out of check
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
            return true;  // No legal moves found that can get the king out of check
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


    // Placeholder for the generateLegalMovesForPiece method. This method will need to be implemented
    // to call the appropriate method (like isLegalPawnMove, isLegalRookMove, etc.) based on the piece type.
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

    // Placeholder for the simulateMove method. This method will apply the move on a copy of the board and return the updated board.
    private static ArrayList<ReturnPiece> simulateMove(ArrayList<ReturnPiece> board, ReturnPiece piece, String move) {
        ArrayList<ReturnPiece> tempBoard = new ArrayList<>(board);
        // Implement the logic to update the tempBoard based on the move
        return tempBoard;
    }

































    public static ReturnPlay checkMate() {
        ReturnPlay check = new ReturnPlay();

        String whiteKingPos = Chess.getKingPos(Player.white);
        String blackKingPos = Chess.getKingPos(Player.black);

        if (Chess.isInCheck(blackKingPos)) {
            if (isCheckMated(Player.black)) {
                check.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
            } else {
                check.message = ReturnPlay.Message.CHECK;
            }
        } else if (Chess.isInCheck(whiteKingPos)) {
            if (isCheckMated(Player.white)) {
                check.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
            } else {
                check.message = ReturnPlay.Message.CHECK;
            }
        }
        check.piecesOnBoard = new ArrayList<>(Chess.board);
        return check;
    }

    private static boolean isCheckMated(Player player) {
        String kingPos = Chess.getKingPos(player);

        // Check if king has any valid moves
        if (hasValidMovesForKing(kingPos)) {
            return false;
        }

        // Check if any pieces can block or capture checking piece
        for (ReturnPiece piece : Chess.board) {
            if (isPieceOfCurrentPlayer(piece)) {
                ArrayList<String> moves = LegalCheck.getLegalMoves(piece);
                for (String move : moves) {
                    if (makeMove(move)) { // Make move tentatively
                        boolean stillInCheck = Chess.isInCheck(kingPos);
                        undoMove(); // Undo move
                        if (!stillInCheck) {
                            return false; // Legal move found to get out of check
                        }
                    }
                }
            }
        }
        return true; // No valid moves for king or other pieces
    }

    // Check if the player's king has any valid moves
    private static boolean hasValidMovesForKing(String kingPos) {
        ReturnPiece kingPiece = Chess.getPieceAt(kingPos); // Fetch the ReturnPiece object for the king
        ArrayList<String> moves = LegalCheck.getLegalMovesForKing(kingPiece, Chess.board); // Pass the object to the method
        for (String move : moves) {
            if (makeMove(move)) { // Make move tentatively
                undoMove(); // Undo move
                return true; // Found valid move
            }
        }
        return false; // No valid moves
    }

    // Check if player has any pieces with valid moves
    private static boolean hasValidMoves(Player player) {
        for (ReturnPiece piece : Chess.board) {
            if (isPieceOfCurrentPlayer(piece)) {
                ArrayList<String> moves = LegalCheck.getLegalMoves(piece);
                if (moves.size() > 0) {
                    return true; // Found piece with valid move
                }
            }
        }
        return false; // No pieces have valid moves
    }

    // Makes a move tentatively and returns true if successful, false otherwise
    public static boolean makeMove(String move) {
        String startPos = move.substring(0, 2);
        String endPos = move.substring(3, 5);

        ReturnPiece piece = Chess.getPieceAt(startPos);
        if (piece == null) {
            return false;
        }

        // Save current state
        ReturnPiece capturedPiece = Chess.getPieceAt(endPos);
        ReturnPiece.PieceType pieceType = piece.pieceType;
        PieceFile startFile = piece.pieceFile;
        int startRank = piece.pieceRank;

        // Make the move
        piece.pieceFile = PieceFile.valueOf(endPos.substring(0, 1));
        piece.pieceRank = Integer.parseInt(endPos.substring(1));
        if (capturedPiece != null) {
            Chess.board.remove(capturedPiece);
        }

        // Check if move was legal
        if (!LegalCheck.isLegalMove(move, Chess.board)) {
            // Undo the move if illegal
            undoMove(piece, startPos, capturedPiece);
            return false;
        }

        return true;
    }

    // Undoes a move by restoring the piece to its original position
    public static void undoMove(ReturnPiece piece, String startPos, ReturnPiece capturedPiece) {
        piece.pieceFile = PieceFile.valueOf(startPos.substring(0, 1));
        piece.pieceRank = Integer.parseInt(startPos.substring(1));
        if (capturedPiece != null) {
            Chess.board.add(capturedPiece);
        }
    }

    // Checks if a piece belongs to the player whose turn it currently is
    public static boolean isPieceOfCurrentPlayer(ReturnPiece piece) {
        if (Chess.currentPlayer == Player.white) {
            return Chess.isWhitePiece(piece.pieceType);
        } else {
            return !Chess.isWhitePiece(piece.pieceType);
        }
    }




    public static ArrayList<String> getLegalMoves(ReturnPiece piece, ArrayList<ReturnPiece> board) {
        ArrayList<String> legalMoves = new ArrayList<>();
    
        String position = piece.pieceFile.name() + piece.pieceRank;
    
        switch (piece.pieceType) {
            case WP:
            case BP:
                // Add all legal pawn moves
                legalMoves.addAll(getLegalPawnMoves(piece, position, board));
                break;
            case WR:
            case BR:
                // Add all legal rook moves
                legalMoves.addAll(getLegalRookMoves(piece, position, board));
                break;
            case WN:
            case BN:
                // Add all legal knight moves
                legalMoves.addAll(getLegalKnightMoves(piece, position, board));
                break;
            case WB:
            case BB:
                // Add all legal bishop moves
                legalMoves.addAll(getLegalBishopMoves(piece, position, board));
                break;
            case WQ:
            case BQ:
                // Add all legal queen moves
                legalMoves.addAll(getLegalQueenMoves(piece, position, board));
                break;
            case WK:
            case BK:
                // Add all legal king moves
                legalMoves.addAll(getLegalMovesForKing(piece, board));
                break;
        }
    
        return legalMoves;
    }

}
