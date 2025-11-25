package chess;
import java.util.ArrayList;

public class BishopMoves {
    public void move(ChessBoard board, ChessPosition startPosition, ArrayList<ChessMove> moves) {
        int rrow1 = startPosition.getRow();
        int ccol1 = startPosition.getColumn();

        while (rrow1 < 8 && ccol1 < 8) { //NE
            rrow1++;
            ccol1++;
            ChessPosition po1 = new ChessPosition(rrow1, ccol1);

            if (helper(board, startPosition, moves, po1)) break;
        }

        int rrow2 = startPosition.getRow();
        int ccol2 = startPosition.getColumn();

        while (rrow2 > 1 && ccol2 < 8) { //SE
            rrow2--;
            ccol2++;
            ChessPosition po2 = new ChessPosition(rrow2, ccol2);

            if (helper(board, startPosition, moves, po2)) break;
        }

        int rrow3 = startPosition.getRow();
        int ccol3 = startPosition.getColumn();

        while (rrow3 > 1 && ccol3 > 1) { //SW
            rrow3--;
            ccol3--;
            ChessPosition po3 = new ChessPosition(rrow3, ccol3);

            if (helper(board, startPosition, moves, po3)) break;
        }

        int rrow4 = startPosition.getRow();
        int ccol4 = startPosition.getColumn();

        while (rrow4 < 8 && ccol4 > 1) { //NW
            rrow4++;
            ccol4--;
            ChessPosition po4 = new ChessPosition(rrow4, ccol4);

            if (helper(board, startPosition, moves, po4)) break;
        }
    }

    private boolean helper(ChessBoard board, ChessPosition startPosition, ArrayList<ChessMove> moves, ChessPosition po1) {
        if (board.getPiece(po1) == null) {
            ChessMove mo1 = new ChessMove(startPosition, po1, null);
            moves.add(mo1);
        }

        else if (board.getPiece(po1).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
            ChessMove mo1 = new ChessMove(startPosition, po1, null);
            moves.add(mo1);
            return true;
        }

        else {
            return true;
        }
        return false;
    }
}
