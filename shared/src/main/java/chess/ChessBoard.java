package chess;

import java.util.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard
{
    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece)
    {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void removePiece(ChessPosition position)
    {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position)
    {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard()
    {
        ChessBoard board = new ChessBoard();

        ArrayList<ChessPiece.PieceType> my_type = new ArrayList<>();
        my_type.add(ROOK);
        my_type.add(KNIGHT);
        my_type.add(BISHOP);
        my_type.add(QUEEN);
        my_type.add(KING);
        my_type.add(BISHOP);
        my_type.add(KNIGHT);
        my_type.add(ROOK);

        for (int i = 1; i < 9; i++)
        {
            ChessPosition pos = new ChessPosition(1, i);
            ChessPiece pie = new ChessPiece(WHITE, my_type.get(i - 1));
            board.addPiece(pos, pie);
        }

        for (int i = 1; i < 9; i++)
        {
            ChessPosition pos = new ChessPosition(8, i);
            ChessPiece pie = new ChessPiece(BLACK, my_type.get(i - 1));
            board.addPiece(pos, pie);
        }

        for (int i = 1; i < 9; i++)
        {
            ChessPosition pos = new ChessPosition(7, i);
            ChessPiece pie = new ChessPiece(BLACK, PAWN);
            board.addPiece(pos, pie);
        }

        for (int i = 1; i < 9; i++)
        {
            ChessPosition pos = new ChessPosition(2, i);
            ChessPiece pie = new ChessPiece(WHITE, PAWN);
            board.addPiece(pos, pie);
        }

        this.squares = board.squares;
    }
}
