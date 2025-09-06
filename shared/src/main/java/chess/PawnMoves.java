package chess;

import java.util.ArrayList;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PawnMoves
{
    public void move(ChessBoard board, ChessPosition startPosition, ArrayList<ChessMove> moves)
    {
        int rrow1 = startPosition.getRow();
        int ccol1 = startPosition.getColumn();
        ChessPosition po1 = new ChessPosition(rrow1, ccol1);

        if (rrow1 == 2 && board.getPiece(startPosition).getTeamColor() == WHITE) //White starting position
        {
            rrow1++;
            ChessPosition po2 = new ChessPosition(rrow1, ccol1);

            if (board.getPiece(po2) == null)
            {
                ChessMove mo2 = new ChessMove(startPosition, po2, null);
                moves.add(mo2);
            }

            rrow1++;
            ChessPosition po3 = new ChessPosition(rrow1, ccol1);

            if (board.getPiece(po3) == null)
            {
                ChessMove mo3 = new ChessMove(startPosition, po3, null);
                moves.add(mo3);
            }
        }

        int rrow4 = startPosition.getRow();
        int ccol4 = startPosition.getColumn();

        if (rrow4 != 2 && board.getPiece(startPosition).getTeamColor() == WHITE) //White move 1 forward
        {
            rrow4++;
            ChessPosition po4 = new ChessPosition(rrow4, ccol4);

            if (board.getPiece(po4) == null)
            {
                ChessMove mo4 = new ChessMove(startPosition, po4, null);
                moves.add(mo4);
            }

            ccol4++;
            ChessPosition po6 = new ChessPosition(rrow4, ccol4);
            if (ccol4 < 8 && board.getPiece(po6).getTeamColor() != board.getPiece(startPosition).getTeamColor()) //White capture right
            {
                ChessMove mo6 = new ChessMove(startPosition, po6, null);
                moves.add(mo6);
            }

            ccol4-=2;
            ChessPosition po7 = new ChessPosition(rrow4, ccol4);
            if (ccol4 > 1 && board.getPiece(po7).getTeamColor() != board.getPiece(startPosition).getTeamColor()) //White capture left
            {
                ChessMove mo7 = new ChessMove(startPosition, po7, null);
                moves.add(mo7);
            }
        }

        int rrow5 = startPosition.getRow();
        int ccol5 = startPosition.getColumn();

        if (rrow5 != 7 && board.getPiece(startPosition).getTeamColor() == BLACK) //Black move 1 forward
        {
            rrow5--;
            ChessPosition po5 = new ChessPosition(rrow5, ccol5);

            if (board.getPiece(po5) == null)
            {
                ChessMove mo5 = new ChessMove(startPosition, po5, null);
                moves.add(mo5);
            }

            ccol5++;
            ChessPosition po6 = new ChessPosition(rrow5, ccol5);
            if (ccol5 < 8 && board.getPiece(po6).getTeamColor() != board.getPiece(startPosition).getTeamColor()) // Black capture right
            {
                ChessMove mo6 = new ChessMove(startPosition, po6, null);
                moves.add(mo6);
            }

            ccol5-=2;
            ChessPosition po7 = new ChessPosition(rrow5, ccol5);
            if (ccol5 > 1 && board.getPiece(po7).getTeamColor() != board.getPiece(startPosition).getTeamColor()) //Black capture left
            {
                ChessMove mo7 = new ChessMove(startPosition, po7, null);
                moves.add(mo7);
            }
        }


        int rrow11 = startPosition.getRow();
        int ccol11 = startPosition.getColumn();
        ChessPosition po11 = new ChessPosition(rrow1, ccol1);

        if (rrow11 == 7 && board.getPiece(po11).getTeamColor() == BLACK) // Black Starting Position
        {
            rrow11--;
            ChessPosition po12 = new ChessPosition(rrow11, ccol11);

            if (board.getPiece(po12) == null)
            {
                ChessMove mo12 = new ChessMove(startPosition, po12, null);
                moves.add(mo12);
            }

            rrow11--;
            ChessPosition po13 = new ChessPosition(rrow11, ccol11);

            if (board.getPiece(po13) == null)
            {
                ChessMove mo13 = new ChessMove(startPosition, po13, null);
                moves.add(mo13);
            }
        }
    }
}
