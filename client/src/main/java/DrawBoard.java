import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawBoard
{
    private final String GREEN_COLOR = "\u001b[48;2;122;148;90m";
    private final String TAN_COLOR = "\u001b[48;2;221;225;196m";
    private final String DARK_GREY_COLOR = "\u001b[48;2;41;45;57m";

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
//    public void displayBoard()
//    {
//        for (int i = 8; i > 0; i--)
//        {
//            System.out.println(i);
//            System.out.print(TAN_COLOR + BLACK_ROOK);
//        }
//
//        String letters = "  a  b  c  d  e  f  g  h";
//        System.out.println(letters);
//    }
}
