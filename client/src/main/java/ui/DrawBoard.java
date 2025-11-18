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

    //hard-coded solution
    public void displayBoard(ChessGame.TeamColor teamColor)
    {
        if (teamColor == WHITE)
        {
            System.out.println(SET_TEXT_COLOR_WHITE + "8 " + TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_ROOK + GREEN_COLOR + SET_TEXT_COLOR_BLACK
                    + BLACK_KNIGHT + TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_BISHOP + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_QUEEN +
                    TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_KING + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_BISHOP + TAN_COLOR +
                    SET_TEXT_COLOR_BLACK + BLACK_KNIGHT + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_ROOK + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "7 " + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + TAN_COLOR + SET_TEXT_COLOR_BLACK
                    + BLACK_PAWN + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN +
                    GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + GREEN_COLOR +
                    SET_TEXT_COLOR_BLACK + BLACK_PAWN + TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "6 " + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY
                    + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "5 " + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY +
                    GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "4 " + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR +
                    EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "3 " + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY +
                    GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "2 " + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + GREEN_COLOR + SET_TEXT_COLOR_WHITE
                    + WHITE_PAWN + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN +
                    TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + TAN_COLOR +
                    SET_TEXT_COLOR_WHITE + WHITE_PAWN + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "1 " + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_ROOK + TAN_COLOR +
                    SET_TEXT_COLOR_WHITE + WHITE_KNIGHT + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_BISHOP + TAN_COLOR + SET_TEXT_COLOR_WHITE +
                    WHITE_QUEEN +
                    GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_KING + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_BISHOP +
                    GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_KNIGHT + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_ROOK
                    + DARK_GREY_COLOR + RESET_TEXT_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "   a  b  c  d  e  f  g  h ");
        }
        else
        {
            System.out.println(SET_TEXT_COLOR_WHITE + "1 " + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_ROOK + GREEN_COLOR + SET_TEXT_COLOR_WHITE
                    + WHITE_KNIGHT + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_BISHOP + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_KING +
                    TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_QUEEN + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_BISHOP + TAN_COLOR +
                    SET_TEXT_COLOR_WHITE + WHITE_KNIGHT + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_ROOK + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "2 " + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + TAN_COLOR + SET_TEXT_COLOR_WHITE
                    + WHITE_PAWN + GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN +
                    GREEN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + GREEN_COLOR +
                    SET_TEXT_COLOR_WHITE + WHITE_PAWN + TAN_COLOR + SET_TEXT_COLOR_WHITE + WHITE_PAWN + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "3 " + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR +
                    EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "4 " + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY +
                    GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "5 " + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR +
                    EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "6 " + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY +
                    GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "7 " + TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + GREEN_COLOR + SET_TEXT_COLOR_BLACK
                    + BLACK_PAWN + TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN +
                    TAN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + TAN_COLOR +
                    SET_TEXT_COLOR_BLACK + BLACK_PAWN + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_PAWN + DARK_GREY_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "8 " + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_ROOK + TAN_COLOR +
                    SET_TEXT_COLOR_BLACK + BLACK_KNIGHT + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_BISHOP + TAN_COLOR +
                    SET_TEXT_COLOR_BLACK + BLACK_KING + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_QUEEN + TAN_COLOR + SET_TEXT_COLOR_BLACK
                    + BLACK_BISHOP + GREEN_COLOR + SET_TEXT_COLOR_BLACK + BLACK_KNIGHT + TAN_COLOR + SET_TEXT_COLOR_BLACK +
                    BLACK_ROOK + DARK_GREY_COLOR + RESET_TEXT_COLOR);
            System.out.println(SET_TEXT_COLOR_WHITE + "   h  g  f  e  d  c  b  a ");
        }
    }

    public void displayBoard2(ChessBoard board)
    {
        this.board = board;
        eraseScreen();
        displayBoarders();
    }

    private void eraseScreen()
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
