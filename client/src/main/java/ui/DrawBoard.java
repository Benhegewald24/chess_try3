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
    private final String GREEN_COLOR = "\u001b[48;2;122;148;90m";
    private final String TAN_COLOR = "\u001b[48;2;221;225;196m";
    private final String DARK_GREY_COLOR = "\u001b[48;2;41;45;57m";
    private static final int BOARD_SIZE = 8;
    ChessBoard board;

    //hard-coded solution
    public void displayBoard()
    {
        System.out.println("8 " + GREEN_COLOR + BLACK_ROOK + TAN_COLOR + BLACK_KNIGHT + GREEN_COLOR + BLACK_BISHOP + TAN_COLOR + BLACK_QUEEN + GREEN_COLOR + BLACK_KING + TAN_COLOR + BLACK_BISHOP + GREEN_COLOR + BLACK_KNIGHT + TAN_COLOR + BLACK_ROOK + DARK_GREY_COLOR);
        System.out.println("7 " + TAN_COLOR + BLACK_PAWN + GREEN_COLOR + BLACK_PAWN + TAN_COLOR + BLACK_PAWN + GREEN_COLOR + BLACK_PAWN + TAN_COLOR + BLACK_PAWN + GREEN_COLOR + BLACK_PAWN + TAN_COLOR + BLACK_PAWN + GREEN_COLOR + BLACK_PAWN + DARK_GREY_COLOR);
        System.out.println("6 " + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + DARK_GREY_COLOR);
        System.out.println("5 " + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + DARK_GREY_COLOR);
        System.out.println("4 " + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + DARK_GREY_COLOR);
        System.out.println("3 " + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + TAN_COLOR + EMPTY + GREEN_COLOR + EMPTY + DARK_GREY_COLOR);
        System.out.println("2 " + GREEN_COLOR + WHITE_PAWN + TAN_COLOR + WHITE_PAWN + GREEN_COLOR + WHITE_PAWN + TAN_COLOR + WHITE_PAWN + GREEN_COLOR + WHITE_PAWN + TAN_COLOR + WHITE_PAWN + GREEN_COLOR + WHITE_PAWN + TAN_COLOR + WHITE_PAWN + DARK_GREY_COLOR);
        System.out.println("1 " + TAN_COLOR + WHITE_ROOK + GREEN_COLOR + WHITE_KNIGHT + TAN_COLOR + WHITE_BISHOP + GREEN_COLOR + WHITE_QUEEN + TAN_COLOR + WHITE_KING + GREEN_COLOR + WHITE_BISHOP + TAN_COLOR + WHITE_KNIGHT + GREEN_COLOR + WHITE_ROOK + DARK_GREY_COLOR);
        System.out.println("   a  b  c  d  e  f  g  h ");
    }

    public void displayBoard2(ChessBoard board)
    {
        this.board = board;
        erase_screen();
        display_boarders();
    }

    private void erase_screen()
    {
        System.out.println(ERASE_SCREEN);
    }

    private void display_boarders()
    {
        String letters = "   a  b  c  d  e  f  g  h";
        for (int i = 8; i > 0; i--)
        {
            System.out.print(i + " ");
            background_color();
        }
        System.out.println(letters);
    }

   int counter = 1;
    private String background_color_switch()
    {
        counter++;
        return counter % 2 == 0 ? TAN_COLOR : GREEN_COLOR;
    }

    private void background_color()
    {
        for (int i = 1; i <= BOARD_SIZE; i++)
        {
            System.out.print(background_color_switch());
            String piece = piece_finder();
            System.out.print(piece);
            //System.out.print(EMPTY);
        }
        counter++;
        System.out.println(DARK_GREY_COLOR);
    }

    private String piece_finder()
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
