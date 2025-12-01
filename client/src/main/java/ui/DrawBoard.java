package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class DrawBoard
{
    private static final String GREEN_COLOR = "\u001b[48;2;122;148;90m";
    private static final String TAN_COLOR = "\u001b[48;2;221;225;196m";
    private static final String DARK_GREY_COLOR = "\u001b[48;2;41;45;57m";
    private static final int BOARD_SIZE = 8;
    ChessBoard board;

    public void displayBoard(ChessGame.TeamColor teamColor)
    {
        if (board == null)
        {
            return;}
        displayBoardFromPerspective(teamColor);
    }

    public void displayBoard(ChessGame game, ChessGame.TeamColor teamColor)
    {
        if (game == null || game.getBoard() == null)
        {
            return;}
        this.board = game.getBoard();
        displayBoardFromPerspective(teamColor);
    }

    private void displayBoardFromPerspective(ChessGame.TeamColor teamColor)
    {
        if (teamColor == WHITE)
        {
            for (int row = 8; row >= 1; row--)
            {
                System.out.print(SET_TEXT_COLOR_WHITE + row + " ");
                for (int col = 1; col <= 8; col++)
                {
                    String backgroundColor = ((row + col) % 2 == 0) ? TAN_COLOR : GREEN_COLOR;
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(pos);
                    String pieceSymbol = getPieceSymbol(piece);
                    String textColor = (piece != null && piece.getTeamColor() == WHITE) ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
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
                    String backgroundColor = ((row + col) % 2 == 0) ? TAN_COLOR : GREEN_COLOR;
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(pos);
                    String pieceSymbol = getPieceSymbol(piece);
                    String textColor = (piece != null && piece.getTeamColor() == WHITE) ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
                    System.out.print(backgroundColor + textColor + pieceSymbol);
                }
                System.out.println(DARK_GREY_COLOR);
            }
            System.out.println(SET_TEXT_COLOR_WHITE + "   h  g  f  e  d  c  b  a " + RESET_TEXT_COLOR);
        }
    }

    private String getPieceSymbol(ChessPiece piece)
    {
        if (piece == null)
        {
            return EMPTY;}

        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        if (color == WHITE)
        {
            if (type == KING)
            {
                return WHITE_KING;}
            else if (type == QUEEN)
            {
                return WHITE_QUEEN;}
            else if (type == BISHOP)
            {
                return WHITE_BISHOP;}
            else if (type == KNIGHT)
            {
                return WHITE_KNIGHT;}
            else if (type == ROOK)
            {
                return WHITE_ROOK;}
            else if (type == PAWN)
            {
                return WHITE_PAWN;}
            else
            {
                return EMPTY;}
        }
        else
        {
            if (type == KING)
            {
                return BLACK_KING;}
            else if (type == QUEEN)
            {
                return BLACK_QUEEN;}
            else if (type == BISHOP)
            {
                return BLACK_BISHOP;}
            else if (type == KNIGHT)
            {
                return BLACK_KNIGHT;}
            else if (type == ROOK)
            {
                return BLACK_ROOK;}
            else if (type == PAWN)
            {
                return BLACK_PAWN;}
            else
            {
                return EMPTY;}
        }
    }

    public void displayBoard2(ChessBoard board)
    {
        this.board = board;
        clearBoard();
        displayBoarders();
    }

    private void clearBoard()
    {
        System.out.println(ERASE_SCREEN);
    }

    private void displayBoarders()
    {
        String letters = "   a  b  c  d  e  f  g  h";
        for (int i = 8; i > 0; i--)
        {
            System.out.print(i + " ");
            backgroundColor();
        }
        System.out.println(letters);
    }

   int counter = 1;
    private String backgroundColorSwitch()
    {
        counter++;
        return counter % 2 == 0 ? TAN_COLOR : GREEN_COLOR;
    }

    private void backgroundColor()
    {
        for (int i = 1; i <= BOARD_SIZE; i++)
        {
            System.out.print(backgroundColorSwitch());
            String piece = pieceFinder();
            //System.out.print(piece);
            System.out.print(EMPTY);
        }
        counter++;
        System.out.println(DARK_GREY_COLOR);
    }

    private String pieceFinder()
    {
        // start at a8 for white player view
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                ChessPosition pos = new ChessPosition(i, j);
                if (board.getPiece(pos).getPieceType() == ROOK && board.getPiece(pos).getTeamColor() == BLACK)
                {
                    return BLACK_ROOK;
                }
                if (board.getPiece(pos).getPieceType() == KNIGHT && board.getPiece(pos).getTeamColor() == BLACK)
                {
                    return BLACK_KNIGHT;
                }
                if (board.getPiece(pos).getPieceType() == BISHOP && board.getPiece(pos).getTeamColor() == BLACK)
                {
                    return BLACK_BISHOP;
                }
                if (board.getPiece(pos).getPieceType() == QUEEN && board.getPiece(pos).getTeamColor() == BLACK)
                {
                    return BLACK_QUEEN;
                }
                if (board.getPiece(pos).getPieceType() == KING && board.getPiece(pos).getTeamColor() == BLACK)
                {
                    return BLACK_KING;
                }
                if (board.getPiece(pos).getPieceType() == PAWN && board.getPiece(pos).getTeamColor() == BLACK)
                {
                    return BLACK_PAWN;
                }
                if (board.getPiece(pos).getPieceType() == ROOK && board.getPiece(pos).getTeamColor() == WHITE)
                {
                    return WHITE_ROOK;
                }
                if (board.getPiece(pos).getPieceType() == KNIGHT && board.getPiece(pos).getTeamColor() == WHITE)
                {
                    return WHITE_KNIGHT;
                }
                if (board.getPiece(pos).getPieceType() == BISHOP && board.getPiece(pos).getTeamColor() == WHITE)
                {
                    return WHITE_BISHOP;
                }
                if (board.getPiece(pos).getPieceType() == QUEEN && board.getPiece(pos).getTeamColor() == WHITE)
                {
                    return WHITE_QUEEN;
                }
                if (board.getPiece(pos).getPieceType() == KING && board.getPiece(pos).getTeamColor() == WHITE)
                {
                    return WHITE_KING;
                }
                if (board.getPiece(pos).getPieceType() == PAWN && board.getPiece(pos).getTeamColor() == WHITE)
                {
                    return WHITE_PAWN;
                }
            }
        }
        return EMPTY;
    }
}
