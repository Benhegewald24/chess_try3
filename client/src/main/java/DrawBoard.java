import static ui.EscapeSequences.*;

public class DrawBoard
{
    public void displayBoard()
    {
        System.out.println("\u001b[31;44;1m 8" + BLACK_ROOK + BLACK_KNIGHT + BLACK_BISHOP + BLACK_QUEEN + BLACK_KING + BLACK_BISHOP + BLACK_KNIGHT + BLACK_ROOK);
        System.out.println("\u001b[31;44;1m 7" + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN);
        System.out.println("\u001b[31;44;1m 6" + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY);
        System.out.println("\u001b[31;44;1m 5" + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY);
        System.out.println("\u001b[31;44;1m 4" + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY);
        System.out.println("\u001b[31;44;1m 3" + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY + EMPTY);
        System.out.println("\u001b[31;44;1m 2" + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN);
        System.out.println("\u001b[31;44;1m 1" + WHITE_ROOK + WHITE_KNIGHT + WHITE_BISHOP + WHITE_QUEEN + WHITE_KING + WHITE_BISHOP + WHITE_KNIGHT + WHITE_ROOK);
        System.out.println("\u001b[31;44;1m   a  b  c  d  e  f  g  h ");
    }
}
