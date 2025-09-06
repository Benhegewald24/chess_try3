package chess;

import java.util.ArrayList;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

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

            if (board.getPiece(po4) == null && rrow4 != 8)
            {
                ChessMove mo4 = new ChessMove(startPosition, po4, null);
                moves.add(mo4);
            }

            if (board.getPiece(po4) == null && rrow4 == 8)
            {
                ChessMove mo2 = new ChessMove(startPosition, po4, BISHOP);
                moves.add(mo2);
                ChessMove mo22 = new ChessMove(startPosition, po4, KNIGHT);
                moves.add(mo22);
                ChessMove mo32 = new ChessMove(startPosition, po4, QUEEN);
                moves.add(mo32);
                ChessMove mo42 = new ChessMove(startPosition, po4, ROOK);
                moves.add(mo42);
            }

            ccol4++;
            ChessPosition po6 = new ChessPosition(rrow4, ccol4);
            if (ccol4 < 8 && board.getPiece(po6).getTeamColor() != board.getPiece(startPosition).getTeamColor() && rrow4 != 8) //White capture right
            {
                ChessMove mo6 = new ChessMove(startPosition, po6, null);
                moves.add(mo6);
            }

            if (ccol4 < 8 && board.getPiece(po6).getTeamColor() != board.getPiece(startPosition).getTeamColor() && rrow4 == 8) //White capture right with promotion
            {
                ChessMove mo22 = new ChessMove(startPosition, po4, BISHOP);
                moves.add(mo22);
                ChessMove mo23 = new ChessMove(startPosition, po4, KNIGHT);
                moves.add(mo23);
                ChessMove mo24 = new ChessMove(startPosition, po4, QUEEN);
                moves.add(mo24);
                ChessMove mo25 = new ChessMove(startPosition, po4, ROOK);
                moves.add(mo25);
            }

            ccol4-=2;
            ChessPosition po7 = new ChessPosition(rrow4, ccol4);
            if (ccol4 > 1 && board.getPiece(po7).getTeamColor() != board.getPiece(startPosition).getTeamColor() && rrow4 != 8) //White capture left
            {
                ChessMove mo7 = new ChessMove(startPosition, po7, null);
                moves.add(mo7);
            }

            if (ccol4 > 1 && board.getPiece(po7).getTeamColor() != board.getPiece(startPosition).getTeamColor() && rrow4 == 8) //White capture left
            {
                ChessMove mo32 = new ChessMove(startPosition, po4, BISHOP);
                moves.add(mo32);
                ChessMove mo33 = new ChessMove(startPosition, po4, KNIGHT);
                moves.add(mo33);
                ChessMove mo34 = new ChessMove(startPosition, po4, QUEEN);
                moves.add(mo34);
                ChessMove mo35 = new ChessMove(startPosition, po4, ROOK);
                moves.add(mo35);
            }
        }

        int rrow5 = startPosition.getRow();
        int ccol5 = startPosition.getColumn();

        if (rrow5 != 7 && board.getPiece(startPosition).getTeamColor() == BLACK) //Black move 1 forward
        {
            rrow5--;
            ChessPosition po5 = new ChessPosition(rrow5, ccol5);

            if (board.getPiece(po5) == null && rrow5 != 1)
            {
                ChessMove mo5 = new ChessMove(startPosition, po5, null);
                moves.add(mo5);
            }

            if (board.getPiece(po5) == null && rrow5 == 1)
            {
                ChessMove mo42 = new ChessMove(startPosition, po5, BISHOP);
                moves.add(mo42);
                ChessMove mo43 = new ChessMove(startPosition, po5, KNIGHT);
                moves.add(mo43);
                ChessMove mo44 = new ChessMove(startPosition, po5, QUEEN);
                moves.add(mo44);
                ChessMove mo45 = new ChessMove(startPosition, po5, ROOK);
                moves.add(mo45);
            }

            ccol5++;
            ChessPosition po6 = new ChessPosition(rrow5, ccol5);
            if (ccol5 < 8 && board.getPiece(po6).getTeamColor() != board.getPiece(startPosition).getTeamColor() && rrow5 != 1) // Black capture right
            {
                ChessMove mo6 = new ChessMove(startPosition, po6, null);
                moves.add(mo6);
            }

            if (ccol5 < 8 && board.getPiece(po6).getTeamColor() != board.getPiece(startPosition).getTeamColor() && rrow5 == 1) // Black capture right
            {
                ChessMove mo52 = new ChessMove(startPosition, po5, BISHOP);
                moves.add(mo52);
                ChessMove mo53 = new ChessMove(startPosition, po5, KNIGHT);
                moves.add(mo53);
                ChessMove mo54 = new ChessMove(startPosition, po5, QUEEN);
                moves.add(mo54);
                ChessMove mo55 = new ChessMove(startPosition, po5, ROOK);
                moves.add(mo55);
            }

            ccol5-=2;
            ChessPosition po7 = new ChessPosition(rrow5, ccol5);
            if (ccol5 > 1 && board.getPiece(po7).getTeamColor() != board.getPiece(startPosition).getTeamColor() && rrow5 != 1) //Black capture left
            {
                ChessMove mo7 = new ChessMove(startPosition, po7, null);
                moves.add(mo7);
            }

            if (ccol5 > 1 && board.getPiece(po7).getTeamColor() != board.getPiece(startPosition).getTeamColor() && rrow5 == 1) //Black capture left
            {
                ChessMove mo52 = new ChessMove(startPosition, po5, BISHOP);
                moves.add(mo52);
                ChessMove mo53 = new ChessMove(startPosition, po5, KNIGHT);
                moves.add(mo53);
                ChessMove mo54 = new ChessMove(startPosition, po5, QUEEN);
                moves.add(mo54);
                ChessMove mo55 = new ChessMove(startPosition, po5, ROOK);
                moves.add(mo55);
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
