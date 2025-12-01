package chess;
import java.util.ArrayList;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessPiece.PieceType.*;

public class PawnMovesHelper
{
    public void move(ChessBoard board, ChessPosition startPosition, ArrayList<ChessMove> moves) {
        int rrow5 = startPosition.getRow();
        int ccol5 = startPosition.getColumn();

        if (rrow5 != 7 && board.getPiece(startPosition).getTeamColor() == BLACK) { //Black move 1 forward
            rrow5--;
            ChessPosition po5 = new ChessPosition(rrow5, ccol5);

            if (board.getPiece(po5) == null && rrow5 != 1) {
                ChessMove mo5 = new ChessMove(startPosition, po5, null);
                moves.add(mo5);
            }

            if (board.getPiece(po5) == null && rrow5 == 1) {
                helperFunction(startPosition, moves, po5);
            }

            ccol5++;
            ChessPosition po6 = new ChessPosition(rrow5, ccol5);
            if (ccol5 < 9 && board.getPiece(po6) != null && board.getPiece(po6).getTeamColor()
                    != board.getPiece(startPosition).getTeamColor() && rrow5 != 1) { // Black capture right
                ChessMove mo6 = new ChessMove(startPosition, po6, null);
                moves.add(mo6);
            }

            if (ccol5 < 9 && board.getPiece(po6) != null && board.getPiece(po6).getTeamColor()
                    != board.getPiece(startPosition).getTeamColor() && rrow5 == 1) { // Black capture right
                helperFunction(startPosition, moves, po6);
            }

            ccol5 -= 2;
            ChessPosition po7 = new ChessPosition(rrow5, ccol5);
            if (ccol5 > 0 && board.getPiece(po7) != null && board.getPiece(po7).getTeamColor()
                    != board.getPiece(startPosition).getTeamColor() && rrow5 != 1) { //Black capture left
                ChessMove mo7 = new ChessMove(startPosition, po7, null);
                moves.add(mo7);
            }

            if (ccol5 > 0 && board.getPiece(po7) != null && board.getPiece(po7).getTeamColor()
                    != board.getPiece(startPosition).getTeamColor() && rrow5 == 1) { //Black capture left
                helperFunction(startPosition, moves, po7);
            }
        }

        int rrow11 = startPosition.getRow();
        int ccol11 = startPosition.getColumn();
        ChessPosition po11 = new ChessPosition(rrow11, ccol11);

        if (board.getPiece(po11) != null && rrow11 == 7 && board.getPiece(po11).getTeamColor() == BLACK) { // Black Starting Position
            rrow11--;
            ChessPosition po12 = new ChessPosition(rrow11, ccol11);
            ccol11++;
            ChessPosition po6 = new ChessPosition(rrow11, ccol11);

            if (ccol11 < 9 && board.getPiece(po6) != null && board.getPiece(po6).getTeamColor()
                    != board.getPiece(startPosition).getTeamColor()) { //White capture right
                ChessMove mo6 = new ChessMove(startPosition, po6, null);
                moves.add(mo6);
            }

            ccol11 -= 2;
            ChessPosition po7 = new ChessPosition(rrow11, ccol11);
            if (ccol11 > 0 && board.getPiece(po7) != null && board.getPiece(po7).getTeamColor()
                    != board.getPiece(startPosition).getTeamColor()) { //White capture left
                ChessMove mo7 = new ChessMove(startPosition, po7, null);
                moves.add(mo7);
            }
            ccol11++;

            if (board.getPiece(po12) == null) {
                ChessMove mo12 = new ChessMove(startPosition, po12, null);
                moves.add(mo12);

                rrow11--;
                ChessPosition po13 = new ChessPosition(rrow11, ccol11);

                if (board.getPiece(po13) == null) {
                    ChessMove mo13 = new ChessMove(startPosition, po13, null);
                    moves.add(mo13);
                }
            }
        }
    }

    private void helperFunction(ChessPosition startPosition, ArrayList<ChessMove> moves, ChessPosition pos) {
        ChessMove mo1 = new ChessMove(startPosition, pos, BISHOP);
        moves.add(mo1);
        ChessMove mo2 = new ChessMove(startPosition, pos, KNIGHT);
        moves.add(mo2);
        ChessMove mo3 = new ChessMove(startPosition, pos, QUEEN);
        moves.add(mo3);
        ChessMove mo4 = new ChessMove(startPosition, pos, ROOK);
        moves.add(mo4);}}