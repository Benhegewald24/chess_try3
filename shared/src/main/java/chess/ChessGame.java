package chess;
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
    ChessBoard board;
    TeamColor team;

    public ChessGame()
    {
        board = new ChessBoard();
        board.resetBoard();
        team = WHITE;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) {return false;}
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        ChessPiece startingPiece = board.getPiece(startPosition);
        ArrayList<ChessMove> valid = new ArrayList<>();

        if (startingPiece == null)
        {
            return valid;
        }

        ArrayList<ChessMove> unfilteredMoves = (ArrayList<ChessMove>) startingPiece.pieceMoves(board, startPosition);

        for (ChessMove move : unfilteredMoves)
        {
            ChessPiece deadPiece = board.getPiece(move.getEndPosition());
            board.removePiece(startPosition);

            if (move.getPromotionPiece() == null)
            {
                board.addPiece(move.getEndPosition(), startingPiece);
            }

            else //pawn promotion
            {
                ChessPiece pie2 = new ChessPiece(startingPiece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), pie2);
            }

            if (!isInCheck(startingPiece.getTeamColor()))
            {
                valid.add(move);
            }

            board.removePiece(move.getEndPosition());

            if (deadPiece != null)
            {
                board.addPiece(move.getEndPosition(), deadPiece);
                board.addPiece(startPosition, startingPiece);
            }

            else
            {
                board.addPiece(startPosition, startingPiece);
            }
        }
        return valid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException
    {

        if (board == null)
        {
            return;
        }

        ChessPiece startingPiece = board.getPiece(move.getStartPosition());
        ChessPosition startPosition = move.getStartPosition();

        if (startingPiece != null && startingPiece.getTeamColor()
                != getTeamTurn() || !validMoves(move.getStartPosition()).contains(move))
            // If given a move for the wrong team (not their turn), throw an InvalidMoveException.
        {
            throw new InvalidMoveException();
        }

        if (!validMoves(startPosition).isEmpty())
        {
            if (move.getPromotionPiece() != null && startingPiece != null)
            {
                ChessPiece pie2 = new ChessPiece(startingPiece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), pie2);
            }

            else
            {
                board.addPiece(move.getEndPosition(), startingPiece);
            }

            board.removePiece(move.getStartPosition());
        }

         setTeamTurn(team == WHITE ? BLACK : WHITE);
    }

    public void undoMakeMove(ChessMove move, ChessPiece movingPiece, ChessPiece dead) //helper method (3 arguments)
    {
        board.addPiece(move.getStartPosition(), movingPiece);
        board.removePiece(move.getEndPosition());

        if (dead != null)
        {
            board.addPiece(move.getEndPosition(), dead);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor)
    {
        for (int i = 1; i <= 8; i++)
        {
            for (int j = 1; j <= 8; j++)
            {
                ChessPosition po = new ChessPosition(i, j);

                if (board == null)
                {
                    break;
                }

                ChessPiece pi = board.getPiece(po);

                if (pi == null)
                {
                    continue;
                }

                ArrayList<ChessMove> unfilteredMoves = (ArrayList<ChessMove>) pi.pieceMoves(board, po);
                boolean bool = is_in_check_helper(unfilteredMoves, pi, teamColor);
                if (bool) {return true;}
            }
        }
        return false;
    }

    public boolean is_in_check_helper(ArrayList<ChessMove> unfilteredMoves, ChessPiece pi, TeamColor teamColor)
    {
        if (pi.getTeamColor() != teamColor)
        {
            for (ChessMove move : unfilteredMoves)
            {
                ChessPosition pos2 = new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn());
                ChessPiece pie = board.getPiece(pos2);

                if (pie != null && pie.getPieceType() == KING && pie.getTeamColor() == teamColor)
                {
                    return true;
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
    public boolean isInCheckmate(TeamColor teamColor)
    {
        if (!isInCheck(teamColor))
        {
            return false;
        }

       //if in check, King can either block with other piece or move
        for (int i = 1; i <= 8; i++)
        {
            for (int j = 1; j <= 8; j++)
            {
                ChessPosition pos = new ChessPosition(i, j);

                if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor)
                {
                    ArrayList<ChessMove> unfilteredMoves;
                    unfilteredMoves = (ArrayList<ChessMove>) board.getPiece(pos).pieceMoves(board, pos);

                    for (ChessMove move : unfilteredMoves)
                    {
                        ChessPiece movingPiece = board.getPiece(pos);
                        ChessPiece dead = board.getPiece(move.getEndPosition());
                        board.removePiece(pos);

                        if (move.getPromotionPiece() == null) //no pawn promote
                        {
                            board.addPiece(move.getEndPosition(), movingPiece);
                        }

                        else //pawn promote
                        {
                            ChessPiece pie = new ChessPiece(board.getPiece(pos).getTeamColor(), move.getPromotionPiece());
                            board.addPiece(move.getEndPosition(), pie);
                        }

                        if (!isInCheck(teamColor))
                        {
                            undoMakeMove(move, movingPiece, dead);
                            return false;
                        }

                        undoMakeMove(move, movingPiece, dead);
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
    public boolean isInStalemate(TeamColor teamColor)
    {
        if (isInCheck(teamColor) || isInCheckmate(teamColor))
        {
            return false;
        }

        for (int i = 1; i <= 8; i++)
        {
            for (int j = 1; j <= 8; j++)
            {
                ChessPosition po = new ChessPosition(i, j);
                if (board.getPiece(po) != null && board.getPiece(po).getTeamColor() == teamColor && !validMoves(po).isEmpty())
                {
                    return false;
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
