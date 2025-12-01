package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Set;
import java.util.HashSet;
import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class DrawBoard
{
    private static final String GREEN_COLOR = "\u001b[48;2;122;148;90m";
    private static final String TAN_COLOR = "\u001b[48;2;221;225;196m";
    private static final String DARK_GREY_COLOR = "\u001b[48;2;41;45;57m";
    private static final String GREEN_COLOR_HIGHLIGHT = "\u001b[48;2;185;203;72m";
    private static final String TAN_COLOR_HIGHLIGHT = "\u001b[48;2;244;246;140m";
    ChessBoard board;


    public void displayBoard(ChessGame game, ChessGame.TeamColor teamColor)
    {
        displayBoard(game, teamColor, null);
    }

    public void displayBoard(ChessGame game, ChessGame.TeamColor teamColor, Set<ChessPosition> highlightedPositions)
    {
        if (game == null)
        {
            return;
        }
        this.board = game.getBoard();

        if (highlightedPositions == null)
        {
            highlightedPositions = new HashSet<>();
        }

        if (teamColor == WHITE)
        {
            for (int row = 8; row >= 1; row--)
            {
                System.out.print(SET_TEXT_COLOR_WHITE + row + " ");
                for (int col = 1; col <= 8; col++)
                {
                    ChessPosition pos = new ChessPosition(row, col);
                    String backgroundColor = getBackgroundColor(pos, highlightedPositions);
                    ChessPiece pie = board.getPiece(pos);
                    String pieceSymbol = pieceFinder(pie);
                    String textColor = (pie != null && pie.getTeamColor() == WHITE) ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
                    System.out.print(backgroundColor + textColor + pieceSymbol);
                }
                System.out.println(DARK_GREY_COLOR);
            }
            System.out.println(SET_TEXT_COLOR_WHITE + "   a  b  c  d  e  f  g  h " + RESET_TEXT_COLOR);
        }
        else
        {
            for (int row = 1; row <= 8; row++)
            {
                System.out.print(SET_TEXT_COLOR_WHITE + row + " ");
                for (int col = 8; col >= 1; col--)
                {
                    ChessPosition pos = new ChessPosition(row, col);
                    String backgroundColor = getBackgroundColor(pos, highlightedPositions);
                    ChessPiece piece = board.getPiece(pos);
                    String pieceSymbol = pieceFinder(piece);
                    String textColor = (piece != null && piece.getTeamColor() == WHITE) ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
                    System.out.print(backgroundColor + textColor + pieceSymbol);
                }
                System.out.println(DARK_GREY_COLOR);
            }
            System.out.println(SET_TEXT_COLOR_WHITE + "   h  g  f  e  d  c  b  a " + RESET_TEXT_COLOR);
        }
    }

    private String getBackgroundColor(ChessPosition pos, Set<ChessPosition> highlightedPositions)
    {
        boolean isHighlighted = highlightedPositions.contains(pos);
        boolean isLightSquare = ((pos.getRow() + pos.getColumn()) % 2 == 1);

        if (isHighlighted)
        {
            return isLightSquare ? TAN_COLOR_HIGHLIGHT : GREEN_COLOR_HIGHLIGHT;
        }
        else
        {
            return isLightSquare ? TAN_COLOR : GREEN_COLOR;
        }
    }

    private String pieceFinder(ChessPiece piece)
    {
        if (piece == null)
        {
            return EMPTY;
        }

        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        if (color == WHITE)
        {
            if (type == KING) {return WHITE_KING;}
            else if (type == QUEEN) {return WHITE_QUEEN;}
            else if (type == BISHOP) {return WHITE_BISHOP;}
            else if (type == KNIGHT) {return WHITE_KNIGHT;}
            else if (type == ROOK) {return WHITE_ROOK;}
            else if (type == PAWN) {return WHITE_PAWN;}
            else {return EMPTY;}
        }

        else
        {
            if (type == KING) {return BLACK_KING;}
            else if (type == QUEEN) {return BLACK_QUEEN;}
            else if (type == BISHOP) {return BLACK_BISHOP;}
            else if (type == KNIGHT) {return BLACK_KNIGHT;}
            else if (type == ROOK) {return BLACK_ROOK;}
            else if (type == PAWN) {return BLACK_PAWN;}
            else {return EMPTY;}
        }
    }
}
