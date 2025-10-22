package chess;
import java.util.ArrayList;

public class KnightMoves
{
    public void move(ChessBoard board, ChessPosition startPosition, ArrayList<ChessMove> moves)
    {
        int rrow1 = startPosition.getRow();
        int ccol1 = startPosition.getColumn();

        if (rrow1 < 7 && ccol1 < 8) //NNE
        {
            rrow1+=2;
            ccol1++;
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

        if (rrow2 < 8 && ccol2 < 7) //NEE
        {
            rrow2++;
            ccol2+=2;
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

        if (rrow3 > 1 && ccol3 < 7) //SEE
        {
            rrow3--;
            ccol3+=2;
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

        if (rrow4 > 2 && ccol4 < 8) //SSE
        {
            rrow4-=2;
            ccol4++;
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

        if (rrow5 > 2 && ccol5 > 1) //SSW
        {
            rrow5-=2;
            ccol5--;
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

        if (rrow6 > 1 && ccol6 > 2) //SWW
        {
            rrow6--;
            ccol6-=2;
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

        if (rrow7 < 8 && ccol7 > 2) //NWW
        {
            rrow7++;
            ccol7-=2;
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

        int rrow8 = startPosition.getRow();
        int ccol8 = startPosition.getColumn();

        if (rrow8 < 7 && ccol8 > 1) //NNW
        {
            rrow8+=2;
            ccol8--;
            ChessPosition po8 = new ChessPosition(rrow8, ccol8);

            if (board.getPiece(po8) == null)
            {
                ChessMove mo8 = new ChessMove(startPosition, po8, null);
                moves.add(mo8);
            }

            if (board.getPiece(po8) != null && board.getPiece(po8).getTeamColor() != board.getPiece(startPosition).getTeamColor())
            {
                ChessMove mo8 = new ChessMove(startPosition, po8, null);
                moves.add(mo8);
            }
        }
    }
}
