package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.KING;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame
{
    TeamColor team;
    ChessBoard board;

    public ChessGame()
    {
        this.team = WHITE;
        this.board = new ChessBoard();
        board.resetBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "team=" + team +
                ", board=" + board +
                '}';
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn()
    {
        return team;
    }


    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team)
    {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor
    {
        WHITE, BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) throws InvalidMoveException
    {
        ChessPiece pi = new ChessPiece(board.getPiece(startPosition).pieceColor, board.getPiece(startPosition).getPieceType());
        ArrayList<ChessMove> unfiltered_moves = (ArrayList<ChessMove>) pi.pieceMoves(board, startPosition);
        ArrayList<ChessMove> valid_moves = (ArrayList<ChessMove>) pi.pieceMoves(board, startPosition);

        for (ChessMove move : unfiltered_moves)
        {
            makeMove(move);

            if (!isInCheck(board.getPiece(move.endPosition).pieceColor) && !isInCheckmate(board.getPiece(move.endPosition).pieceColor) && !isInStalemate(board.getPiece(move.endPosition).pieceColor))
            {
                valid_moves.add(move);
            }
        }
        return valid_moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        if (board != null && move.promotionPiece == null) //non-pawn
        {
            board.addPiece(move.endPosition, board.getPiece(move.startPosition));
            board.addPiece(move.startPosition, null);
        }

        else //pawn promotion
        {
            if (board != null)
            {
                ChessPiece pi = new ChessPiece(board.getPiece(move.startPosition).getTeamColor(), move.promotionPiece);
                board.addPiece(move.endPosition, pi);
                board.addPiece(move.startPosition, null);
            }
        }

        if (team == WHITE)
        {
            setTeamTurn(BLACK);
        }

        else
        {
            setTeamTurn(WHITE);
        }
    }

    public void undoMove(ChessMove move)
    {
        board.addPiece(move.startPosition, board.getPiece(move.startPosition));
        board.addPiece(move.endPosition, board.getPiece(move.endPosition));
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) throws InvalidMoveException
    {
        for (int i = 1; i < 9; i++)
        {
            for (int j = 1; j < 9; j++)
            {
                ChessPosition po = new ChessPosition(i, j);
                if (board != null && board.getPiece(po) != null && board.getPiece(po).getTeamColor() != teamColor)
                {
                    ArrayList<ChessMove> valid_moves = new ArrayList<>();
                    valid_moves = (ArrayList<ChessMove>) validMoves(po);

                    for (ChessMove move : valid_moves)
                    {
                        if (board.getPiece(move.endPosition).getPieceType() == KING && board.getPiece(move.endPosition).getTeamColor() == teamColor);
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) throws InvalidMoveException
    {
        if (!isInCheck(teamColor))
        {
            return false;
        }

        else //if in check, King can either block with other piece or move
        {
            for (int i = 1; i < 9; i++)
            {
                for (int j = 1; j < 9; j++)
                {
                    ChessPosition po = new ChessPosition(i, j);
                    if (board.getPiece(po).getPieceType() == KING && board.getPiece(po).getTeamColor() == teamColor) // move
                    {
                        ChessPosition king_position = new ChessPosition(i, j);
                        if (!validMoves(king_position).isEmpty()) // king is able to move
                        {
                            return false;
                        }
                        break;
                    }

                    else if (board.getPiece(po).getPieceType() != KING && board.getPiece(po).getTeamColor() == teamColor) // block
                    {
                        ArrayList<ChessMove> valid_moves = new ArrayList<>();
                        valid_moves = (ArrayList<ChessMove>) validMoves(po);

                        for (ChessMove move : valid_moves)
                        {
                            makeMove(move);
                            if (!isInCheck(teamColor))
                            {
                                undoMove(move);
                                return false;
                            }
                            else
                            {
                                undoMove(move);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) throws InvalidMoveException
    {
        if (isInCheck(teamColor) || isInCheckmate(teamColor))
        {
            return false;
        }

        for (int i = 1; i < 9; i++)
        {
            for (int j = 1; j < 9; j++)
            {
                ChessPosition po = new ChessPosition(i, j);
                if (board.getPiece(po) != null && board.getPiece(po).getTeamColor() == teamColor)
                {
                    if (!validMoves(po).isEmpty())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board)
    {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard()
    {
       return board;
    }
}
