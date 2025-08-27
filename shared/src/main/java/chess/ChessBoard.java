package chess;

import java.util.Objects;

import static chess.ChessGame.TeamColor.WHITE;

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
        return Objects.equals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + squares +
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

        ChessPosition p1 = new ChessPosition(1,1);
        ChessPiece pi1 = new ChessPiece(WHITE, ChessPiece.PieceType.ROOK);
        board.addPiece(p1, pi1);

        ChessPosition p2 = new ChessPosition(1,2);
        ChessPiece pi2 = new ChessPiece(WHITE, ChessPiece.PieceType.KNIGHT);
        board.addPiece(p2, pi2);

        ChessPosition p3 = new ChessPosition(1,3);
        ChessPiece pi3 = new ChessPiece(WHITE, ChessPiece.PieceType.BISHOP);
        board.addPiece(p3, pi3);

        ChessPosition p4 = new ChessPosition(1,4);
        ChessPiece pi4 = new ChessPiece(WHITE, ChessPiece.PieceType.QUEEN);
        board.addPiece(p4, pi4);

        ChessPosition p5 = new ChessPosition(1,5);
        ChessPiece pi5 = new ChessPiece(WHITE, ChessPiece.PieceType.KING);
        board.addPiece(p5, pi5);

        ChessPosition p6 = new ChessPosition(1,6);
        ChessPiece pi6 = new ChessPiece(WHITE, ChessPiece.PieceType.BISHOP);
        board.addPiece(p6, pi6);

        ChessPosition p7 = new ChessPosition(1,7);
        ChessPiece pi7 = new ChessPiece(WHITE, ChessPiece.PieceType.KNIGHT);
        board.addPiece(p7, pi7);

        ChessPosition p8 = new ChessPosition(1,8);
        ChessPiece pi8 = new ChessPiece(WHITE, ChessPiece.PieceType.ROOK);
        board.addPiece(p8, pi8);

        throw new RuntimeException("Not implemented");
    }
}
