package chess;
import java.util.ArrayList;

public class KingMoves
{
    public void move(ChessBoard board, ChessPosition startPosition, ArrayList<ChessMove> moves)
    {
        int rrow1 = startPosition.getRow();
        int ccol1 = startPosition.getColumn();

        if (rrow1 < 8) //N
        {
            rrow1++;
            ChessPosition po1 = new ChessPosition(rrow1, ccol1);

            if (board.getPiece(po1) == null)
            {
                ChessMove mo1 = new ChessMove(startPosition, po1, null);
                moves.add(mo1);
            }

            if (board.getPiece(po1) != null && board.getPiece(po1).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo1 = new ChessMove(startPosition, po1, null);
                moves.add(mo1);
            }
        }

        int rrow2 = startPosition.getRow();
        int ccol2 = startPosition.getColumn();

        if (ccol2 < 8) //E
        {
            ccol2++;
            ChessPosition po2 = new ChessPosition(rrow2, ccol2);

            if (board.getPiece(po2) == null)
            {
                ChessMove mo2 = new ChessMove(startPosition, po2, null);
                moves.add(mo2);
            }

            if (board.getPiece(po2) != null && board.getPiece(po2).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo2 = new ChessMove(startPosition, po2, null);
                moves.add(mo2);
            }
        }

        int rrow3 = startPosition.getRow();
        int ccol3 = startPosition.getColumn();

        if (rrow3 > 1) //S
        {
            rrow3--;
            ChessPosition po3 = new ChessPosition(rrow3, ccol3);

            if (board.getPiece(po3) == null)
            {
                ChessMove mo3 = new ChessMove(startPosition, po3, null);
                moves.add(mo3);
            }

            if (board.getPiece(po3) != null && board.getPiece(po3).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo3 = new ChessMove(startPosition, po3, null);
                moves.add(mo3);
            }
        }

        int rrow4 = startPosition.getRow();
        int ccol4 = startPosition.getColumn();

        if (ccol4 > 1) //W
        {
            ccol4--;
            ChessPosition po4 = new ChessPosition(rrow4, ccol4);

            if (board.getPiece(po4) == null)
            {
                ChessMove mo4 = new ChessMove(startPosition, po4, null);
                moves.add(mo4);
            }

            if (board.getPiece(po4) != null && board.getPiece(po4).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo4 = new ChessMove(startPosition, po4, null);
                moves.add(mo4);
            }
        }

        int rrow5 = startPosition.getRow();
        int ccol5 = startPosition.getColumn();

        if (rrow5 < 8 && ccol5 < 8) //NE
        {
            rrow5++;
            ccol5++;
            ChessPosition po5 = new ChessPosition(rrow5, ccol5);

            if (board.getPiece(po5) == null)
            {
                ChessMove mo5 = new ChessMove(startPosition, po5, null);
                moves.add(mo5);
            }

            if (board.getPiece(po5) != null && board.getPiece(po5).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo5 = new ChessMove(startPosition, po5, null);
                moves.add(mo5);
            }
        }

        int rrow6 = startPosition.getRow();
        int ccol6 = startPosition.getColumn();

        if (rrow6 > 1 && ccol6 < 8) //SE
        {
            rrow6--;
            ccol6++;
            ChessPosition po6 = new ChessPosition(rrow6, ccol6);

            if (board.getPiece(po6) == null)
            {
                ChessMove mo6 = new ChessMove(startPosition, po6, null);
                moves.add(mo6);
            }

            if (board.getPiece(po6) != null && board.getPiece(po6).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo6 = new ChessMove(startPosition, po6, null);
                moves.add(mo6);
            }
        }

        int rrow7 = startPosition.getRow();
        int ccol7 = startPosition.getColumn();

        if (rrow7 > 1 && ccol7 > 1) //SW
        {
            rrow7--;
            ccol7--;
            ChessPosition po7 = new ChessPosition(rrow7, ccol7);

            if (board.getPiece(po7) == null)
            {
                ChessMove mo7 = new ChessMove(startPosition, po7, null);
                moves.add(mo7);
            }

            if (board.getPiece(po7) != null && board.getPiece(po7).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo7 = new ChessMove(startPosition, po7, null);
                moves.add(mo7);
            }
        }

        int rrow88 = startPosition.getRow();
        int ccol88 = startPosition.getColumn();

        if (rrow88 < 8 && ccol88 > 1) //NW
        {
            rrow88++;
            ccol88--;
            ChessPosition po88 = new ChessPosition(rrow88, ccol88);

            if (board.getPiece(po88) == null)
            {
                ChessMove mo88 = new ChessMove(startPosition, po88, null);
                moves.add(mo88);
            }

            if (board.getPiece(po88) != null && board.getPiece(po88).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo88 = new ChessMove(startPosition, po88, null);
                moves.add(mo88);
            }
        }
    }
}
