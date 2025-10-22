package chess;

import java.util.ArrayList;

public class KingMovesHelper
{
    public void move(ChessBoard board, ChessPosition startPosition, ArrayList<ChessMove> moves)
    {
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
