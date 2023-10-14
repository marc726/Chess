package chess;

import chess.ReturnPiece.PieceType;


    /**
     * Checks if pawn can capture via en passant.
     *
     * @param currentPawn pawn attempting the en passant move.
     * @param targetPosition  target position for the pawn move.
     * @return ret true if en passant is possible, else false.
     */

public class EnPassant {
    
    public static boolean canEnPassant(ReturnPiece pawn, String end) {
    // Check if last move was a two-square pawn advance
    if (Chess.lastMove == null) return false;

    String lastMoveStart = Chess.lastMove.substring(0, 2);
    String lastMoveEnd = Chess.lastMove.substring(3, 5);

    ReturnPiece lastMovedPiece = Chess.getPieceAt(lastMoveEnd);
    if (lastMovedPiece == null || !isPawn(lastMovedPiece)) return false;

    // Ensure pawn being captured is opposite color
    if (lastMovedPiece.pieceType == pawn.pieceType) return false;

    if (Math.abs(lastMoveStart.charAt(1) - lastMoveEnd.charAt(1)) != 2) return false;

    int endRank = Character.getNumericValue(end.charAt(1));
    if ((pawn.pieceType == PieceType.WP && endRank != 6) || (pawn.pieceType == PieceType.BP && endRank != 3)) return false;

    char pawnFile = pawn.pieceFile.name().charAt(0);
    char lastMovedPieceFile = lastMovedPiece.pieceFile.name().charAt(0);
    if (Math.abs(pawnFile - lastMovedPieceFile) != 1) return false;

    return true;
    }

    private static boolean isPawn(ReturnPiece piece) {
        return piece.pieceType == PieceType.WP || piece.pieceType == PieceType.BP;
    }


}
