package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() { return pieceColor; }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger3
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
//        Get current piece
        ChessPiece piece = board.getPiece(myPosition);

//        Calculate moves
        return calculateMoves(board, piece, myPosition);

    }

    private ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPiece piece, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPiece.PieceType type = piece.getPieceType();

        if (type == PieceType.KING) {
            moves.addAll(nonPawnStraightMoves(board, position, piece));
            moves.addAll(diagonalMoves(board, position, piece));
        } else if (type == PieceType.QUEEN) {
            moves.addAll(nonPawnStraightMoves(board, position, piece));
            moves.addAll(diagonalMoves(board, position, piece));
        } else if (type == PieceType.ROOK) {
            moves.addAll(nonPawnStraightMoves(board, position, piece));
        } else if (type == PieceType.BISHOP) {
            moves.addAll(diagonalMoves(board, position, piece));
        } else if (type == PieceType.KNIGHT) {
            moves.addAll(knightMoves(board, position, piece));
        }  else if (type == PieceType.PAWN) {
            moves.addAll(pawnMoves(board, position, piece));
        } else {
            throw new IllegalArgumentException("Not a valid chess piece: " + type);
        }

        return moves;
    }

    private static ArrayList<ChessMove> nonPawnStraightMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        ArrayList<ChessMove> legalMoves = new ArrayList<>();

        // Right
        for (int j = col + 1; j <= 8; j++) {
            ChessPosition potentialMove = new ChessPosition(row, j);
            if (friendlyFire(board, potentialMove, pieceColor)) {
                break;
            } else {
                ChessMove legalMove = new ChessMove(position, potentialMove, null);
                legalMoves.add(legalMove);
            }
            if (enemyPiece(board, potentialMove, pieceColor)) {
                break;  // Stop if an enemy piece is captured
            }
            if (type == ChessPiece.PieceType.KING) break; // King moves only 1 square
        }

        // Left
        for (int j = col - 1; j >= 1; j--) {
            ChessPosition potentialMove = new ChessPosition(row, j);
            if (friendlyFire(board, potentialMove, pieceColor)) {
                break;
            } else {
                ChessMove legalMove = new ChessMove(position, potentialMove, null);
                legalMoves.add(legalMove);
            }
            if (enemyPiece(board, potentialMove, pieceColor)) {
                break;  // Stop if an enemy piece is captured
            }
            if (type == ChessPiece.PieceType.KING) break; // King moves only 1 square
        }

        // Up
        for (int i = row + 1; i <= 8; i++) {
            ChessPosition potentialMove = new ChessPosition(i, col);
            if (friendlyFire(board, potentialMove, pieceColor)) {
                break;
            } else {
                ChessMove legalMove = new ChessMove(position, potentialMove, null);
                legalMoves.add(legalMove);
            }
            if (enemyPiece(board, potentialMove, pieceColor)) {
                break;  // Stop if an enemy piece is captured
            }
            if (type == ChessPiece.PieceType.KING) break; // King moves only 1 square
        }

        // Down
        for (int i = row - 1; i >= 1; i--) {
            ChessPosition potentialMove = new ChessPosition(i, col);
            if (friendlyFire(board, potentialMove, pieceColor)) {
                break;
            } else {
                ChessMove legalMove = new ChessMove(position, potentialMove, null);
                legalMoves.add(legalMove);
            }
            if (enemyPiece(board, potentialMove, pieceColor)) {
                break;  // Stop if an enemy piece is captured
            }
            if (type == ChessPiece.PieceType.KING) break; // King moves only 1 square
        }

        return legalMoves;
    }


    private static ArrayList<ChessMove> diagonalMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        ArrayList<ChessMove> legalMoves = new ArrayList<>();

        // Right-up
        for (int i = row + 1, j = col + 1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition potentialMove = new ChessPosition(i, j);
            if (friendlyFire(board, potentialMove, pieceColor)) {
                break;
            } else {
                ChessMove legalMove = new ChessMove(position, potentialMove, null);
                legalMoves.add(legalMove);
            }
            if (enemyPiece(board, potentialMove, pieceColor)) {
                break;  // Stop after capturing an enemy piece
            }
            if (type == ChessPiece.PieceType.KING) break;  // King moves only 1 square
        }

        // Right-down
        for (int i = row - 1, j = col + 1; i >= 1 && j <= 8; i--, j++) {
            ChessPosition potentialMove = new ChessPosition(i, j);
            if (friendlyFire(board, potentialMove, pieceColor)) {
                break;
            } else {
                ChessMove legalMove = new ChessMove(position, potentialMove, null);
                legalMoves.add(legalMove);
            }
            if (enemyPiece(board, potentialMove, pieceColor)) {
                break;  // Stop after capturing an enemy piece
            }
            if (type == ChessPiece.PieceType.KING) break;  // King moves only 1 square
        }

        // Left-up
        for (int i = row + 1, j = col - 1; i <= 8 && j >= 1; i++, j--) {
            ChessPosition potentialMove = new ChessPosition(i, j);
            if (friendlyFire(board, potentialMove, pieceColor)) {
                break;
            } else {
                ChessMove legalMove = new ChessMove(position, potentialMove, null);
                legalMoves.add(legalMove);
            }
            if (enemyPiece(board, potentialMove, pieceColor)) {
                break;  // Stop after capturing an enemy piece
            }
            if (type == ChessPiece.PieceType.KING) break;  // King moves only 1 square
        }

        // Left-down
        for (int i = row - 1, j = col - 1; i >= 1 && j >= 1; i--, j--) {
            ChessPosition potentialMove = new ChessPosition(i, j);
            if (friendlyFire(board, potentialMove, pieceColor)) {
                break;
            } else {
                ChessMove legalMove = new ChessMove(position, potentialMove, null);
                legalMoves.add(legalMove);
            }
            if (enemyPiece(board, potentialMove, pieceColor)) {
                break;  // Stop after capturing an enemy piece
            }
            if (type == ChessPiece.PieceType.KING) break;  // King moves only 1 square
        }

        return legalMoves;
    }



    private static ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();

        ArrayList<ChessMove> legalMoves = new ArrayList<>();

        int endRow;
        int endCol;
        ChessPosition potentialPosition;

//      Up 2 Right 1
        endRow = row + 2;
        endCol = col + 1;
        potentialPosition = new ChessPosition(endRow, endCol);
        if(withinBoundaries(potentialPosition) && !friendlyFire(board, potentialPosition, pieceColor)) {
            legalMoves.add(new ChessMove(position, potentialPosition, null));
        }

//      Up 2 Left 1
        endRow = row + 2;
        endCol = col - 1;
        potentialPosition = new ChessPosition(endRow, endCol);
        if(withinBoundaries(potentialPosition) && !friendlyFire(board, potentialPosition, pieceColor)) {
            legalMoves.add(new ChessMove(position, potentialPosition, null));
        }

//      Up 1 Right 2
        endRow = row + 1;
        endCol = col + 2;
        potentialPosition = new ChessPosition(endRow, endCol);
        if(withinBoundaries(potentialPosition) && !friendlyFire(board, potentialPosition, pieceColor)) {
            legalMoves.add(new ChessMove(position, potentialPosition, null));
        }

//      Up 1 Left 2
        endRow = row + 1;
        endCol = col - 2;
        potentialPosition = new ChessPosition(endRow, endCol);
        if(withinBoundaries(potentialPosition) && !friendlyFire(board, potentialPosition, pieceColor)) {
            legalMoves.add(new ChessMove(position, potentialPosition, null));
        }

//      Down 1 Right 2
        endRow = row - 1;
        endCol = col + 2;
        potentialPosition = new ChessPosition(endRow, endCol);
        if(withinBoundaries(potentialPosition) && !friendlyFire(board, potentialPosition, pieceColor)) {
            legalMoves.add(new ChessMove(position, potentialPosition, null));
        }

//      Down 1 Left 2
        endRow = row - 1;
        endCol = col - 2;
        potentialPosition = new ChessPosition(endRow, endCol);
        if(withinBoundaries(potentialPosition) && !friendlyFire(board, potentialPosition, pieceColor)) {
            legalMoves.add(new ChessMove(position, potentialPosition, null));
        }

//      Down 2 Right 1
        endRow = row - 2;
        endCol = col + 1;
        potentialPosition = new ChessPosition(endRow, endCol);
        if(withinBoundaries(potentialPosition) && !friendlyFire(board, potentialPosition, pieceColor)) {
            legalMoves.add(new ChessMove(position, potentialPosition, null));
        }

//      Down 2 Left 1
        endRow = row - 2;
        endCol = col - 1;
        potentialPosition = new ChessPosition(endRow, endCol);
        if(withinBoundaries(potentialPosition) && !friendlyFire(board, potentialPosition, pieceColor)) {
            legalMoves.add(new ChessMove(position, potentialPosition, null));
        }

        return legalMoves;
    }

    private static ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition position, ChessPiece piece) {
        ArrayList<ChessMove> legalMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();

        // Determine direction based on piece color
        int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        // Straight motion (1 square move)
        ChessPosition potentialPosition = new ChessPosition(row + direction, col);
        if (withinBoundaries(potentialPosition) && board.getPiece(potentialPosition) == null) {
            if (isLastRow(potentialPosition)) {
                legalMoves.add(new ChessMove(position, potentialPosition, ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(position, potentialPosition, ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(position, potentialPosition, ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(position, potentialPosition, ChessPiece.PieceType.KNIGHT));
            } else {
                legalMoves.add(new ChessMove(position, potentialPosition, null));
            }
        }

        // Initial two-square move
        if ((pieceColor == ChessGame.TeamColor.WHITE && row == 2) || (pieceColor == ChessGame.TeamColor.BLACK && row == 7)) {
            ChessPosition twoSquarePosition = new ChessPosition(row + 2 * direction, col);
            // Check if both the straight and the two-square positions are unoccupied
            if (withinBoundaries(twoSquarePosition) && board.getPiece(potentialPosition) == null && board.getPiece(twoSquarePosition) == null) {
                legalMoves.add(new ChessMove(position, twoSquarePosition, null));
            }
        }

        // Capture diagonally left
        ChessPosition captureLeftPosition = new ChessPosition(row + direction, col - 1);
        if (withinBoundaries(captureLeftPosition) && enemyPiece(board, captureLeftPosition, pieceColor)) {
            if (isLastRow(captureLeftPosition)) {
                legalMoves.add(new ChessMove(position, captureLeftPosition, ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(position, captureLeftPosition, ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(position, captureLeftPosition, ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(position, captureLeftPosition, ChessPiece.PieceType.KNIGHT));
            } else {
                legalMoves.add(new ChessMove(position, captureLeftPosition, null));
            }
        }

        // Capture diagonally right
        ChessPosition captureRightPosition = new ChessPosition(row + direction, col + 1);
        if (withinBoundaries(captureRightPosition) && enemyPiece(board, captureRightPosition, pieceColor)) {
            if (isLastRow(captureRightPosition)) {
                legalMoves.add(new ChessMove(position, captureRightPosition, ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(position, captureRightPosition, ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(position, captureRightPosition, ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(position, captureRightPosition, ChessPiece.PieceType.KNIGHT));
            } else {
                legalMoves.add(new ChessMove(position, captureRightPosition, null));
            }
        }

        return legalMoves;
    }


    private static boolean friendlyFire(ChessBoard board, ChessPosition potentialMove, ChessGame.TeamColor color) {
        ChessPiece piece = board.getPiece(potentialMove);
        return piece != null && piece.getTeamColor() == color;
    }


    private static boolean enemyPiece(ChessBoard board, ChessPosition potentialMove, ChessGame.TeamColor pieceColor) {
        ChessPiece pieceAtPosition = board.getPiece(potentialMove);
        return pieceAtPosition != null && pieceAtPosition.getTeamColor() != pieceColor;
    }

    private static boolean withinBoundaries(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();

        if (row <= 8 && col <= 8 && row >= 1 && col >= 1){ return true; }
        else { return false; }
    };

    private static boolean isLastRow(ChessPosition position) {
        // Assuming that the last row for white is 8 and for black is 1
        return position.getRow() == 1 || position.getRow() == 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece(" +
                "teamColor=" + pieceColor +
                ", type=" + type +
                ')';
    }
}
