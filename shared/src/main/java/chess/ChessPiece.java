package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece
{

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type)
    {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString()
    {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType
    {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ArrayList<ChessMove> moves = new ArrayList<>();
        PieceType type1 = board.getPiece(myPosition).getPieceType();

        if (type1 == ROOK)
        {
            RookMoves r1 = new RookMoves();
            r1.move(board, myPosition, moves);
            return moves;
        }

        if (type1 == KNIGHT)
        {
            KnightMoves kn1 = new KnightMoves();
            kn1.move(board, myPosition, moves);
            return moves;
        }

        if (type1 == BISHOP)
        {
            BishopMoves b1 = new BishopMoves();
            b1.move(board, myPosition, moves);
            return moves;
        }

        if (type1 == QUEEN)
        {
            QueenMoves q1 = new QueenMoves();
            q1.move(board, myPosition, moves);
            return moves;
        }

        if (type1 == KING)
        {
            KingMoves k1 = new KingMoves();
            k1.move(board, myPosition, moves);
            return moves;
        }

        PawnMoves p1 = new PawnMoves();
        p1.move(board, myPosition, moves);
        return moves;
    }
}
