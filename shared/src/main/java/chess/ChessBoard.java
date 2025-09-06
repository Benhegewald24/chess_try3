package chess;

import java.util.Objects;

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
        ChessPiece pi1 = new ChessPiece(WHITE, ROOK);
        board.addPiece(p1, pi1);

        ChessPosition p2 = new ChessPosition(1,2);
        ChessPiece pi2 = new ChessPiece(WHITE, KNIGHT);
        board.addPiece(p2, pi2);

        ChessPosition p3 = new ChessPosition(1,3);
        ChessPiece pi3 = new ChessPiece(WHITE, BISHOP);
        board.addPiece(p3, pi3);

        ChessPosition p4 = new ChessPosition(1,4);
        ChessPiece pi4 = new ChessPiece(WHITE, QUEEN);
        board.addPiece(p4, pi4);

        ChessPosition p5 = new ChessPosition(1,5);
        ChessPiece pi5 = new ChessPiece(WHITE, KING);
        board.addPiece(p5, pi5);

        ChessPosition p6 = new ChessPosition(1,6);
        ChessPiece pi6 = new ChessPiece(WHITE, BISHOP);
        board.addPiece(p6, pi6);

        ChessPosition p7 = new ChessPosition(1,7);
        ChessPiece pi7 = new ChessPiece(WHITE, KNIGHT);
        board.addPiece(p7, pi7);

        ChessPosition p8 = new ChessPosition(1,8);
        ChessPiece pi8 = new ChessPiece(WHITE, ROOK);
        board.addPiece(p8, pi8);

        ChessPosition p9 = new ChessPosition(2,1);
        ChessPiece pi9 = new ChessPiece(WHITE, PAWN);
        board.addPiece(p9, pi9);

        ChessPosition p10 = new ChessPosition(2,2);
        ChessPiece pi10 = new ChessPiece(WHITE, PAWN);
        board.addPiece(p10, pi10);

        ChessPosition p11 = new ChessPosition(2,3);
        ChessPiece pi11 = new ChessPiece(WHITE, PAWN);
        board.addPiece(p11, pi11);

        ChessPosition p12 = new ChessPosition(2,4);
        ChessPiece pi12 = new ChessPiece(WHITE, PAWN);
        board.addPiece(p12, pi12);

        ChessPosition p13 = new ChessPosition(2,5);
        ChessPiece pi13 = new ChessPiece(WHITE, PAWN);
        board.addPiece(p13, pi13);

        ChessPosition p14 = new ChessPosition(2,6);
        ChessPiece pi14 = new ChessPiece(WHITE, PAWN);
        board.addPiece(p14, pi14);

        ChessPosition p15 = new ChessPosition(2,7);
        ChessPiece pi15 = new ChessPiece(WHITE, PAWN);
        board.addPiece(p15, pi15);

        ChessPosition p16 = new ChessPosition(2,8);
        ChessPiece pi16 = new ChessPiece(WHITE, PAWN);
        board.addPiece(p16, pi16);

        ChessPosition p17 = new ChessPosition(7,1);
        ChessPiece pi17 = new ChessPiece(BLACK, PAWN);
        board.addPiece(p17, pi17);

        ChessPosition p18 = new ChessPosition(7,2);
        ChessPiece pi18 = new ChessPiece(BLACK, PAWN);
        board.addPiece(p18, pi18);

        ChessPosition p19 = new ChessPosition(7,3);
        ChessPiece pi19 = new ChessPiece(BLACK, PAWN);
        board.addPiece(p19, pi19);

        ChessPosition p20 = new ChessPosition(7,4);
        ChessPiece pi20 = new ChessPiece(BLACK, PAWN);
        board.addPiece(p20, pi20);

        ChessPosition p21 = new ChessPosition(7,5);
        ChessPiece pi21 = new ChessPiece(BLACK, PAWN);
        board.addPiece(p21, pi21);

        ChessPosition p22 = new ChessPosition(7,6);
        ChessPiece pi22 = new ChessPiece(BLACK, PAWN);
        board.addPiece(p22, pi22);

        ChessPosition p23 = new ChessPosition(7,7);
        ChessPiece pi23 = new ChessPiece(BLACK, PAWN);
        board.addPiece(p23, pi23);

        ChessPosition p24 = new ChessPosition(7,8);
        ChessPiece pi24 = new ChessPiece(BLACK, PAWN);
        board.addPiece(p24, pi24);

        ChessPosition p25 = new ChessPosition(8,1);
        ChessPiece pi25 = new ChessPiece(BLACK, ROOK);
        board.addPiece(p25, pi25);

        ChessPosition p26 = new ChessPosition(8,2);
        ChessPiece pi26 = new ChessPiece(BLACK, KNIGHT);
        board.addPiece(p26, pi26);

        ChessPosition p27 = new ChessPosition(8,3);
        ChessPiece pi27 = new ChessPiece(BLACK, BISHOP);
        board.addPiece(p27, pi27);

        ChessPosition p28 = new ChessPosition(8,4);
        ChessPiece pi28 = new ChessPiece(BLACK, QUEEN);
        board.addPiece(p28, pi28);

        ChessPosition p29 = new ChessPosition(8,5);
        ChessPiece pi29 = new ChessPiece(BLACK, KING);
        board.addPiece(p29, pi29);

        ChessPosition p30 = new ChessPosition(8,6);
        ChessPiece pi30 = new ChessPiece(BLACK, BISHOP);
        board.addPiece(p30, pi30);

        ChessPosition p31 = new ChessPosition(8,7);
        ChessPiece pi31 = new ChessPiece(BLACK, KNIGHT);
        board.addPiece(p31, pi31);

        ChessPosition p32 = new ChessPosition(8,8);
        ChessPiece pi32 = new ChessPiece(BLACK, ROOK);
        board.addPiece(p32, pi32);

        board.squares = this.squares;
        //this.squares = board.squares;
    }
}
