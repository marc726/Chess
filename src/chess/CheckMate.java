package chess;

import java.util.ArrayList;

import chess.Chess.Player;
import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class CheckMate {
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
        ArrayList<String> moves = LegalCheck.getLegalMovesForKing(kingPos);
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
}
