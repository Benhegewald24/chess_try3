package chess;
import java.util.ArrayList;
import static chess.ChessGame.TeamColor.BLACK;

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
                ChessMove mo42 = new ChessMove(startPosition, po5, ChessPiece.PieceType.BISHOP); moves.add(mo42);
                ChessMove mo43 = new ChessMove(startPosition, po5, ChessPiece.PieceType.KNIGHT); moves.add(mo43);
                ChessMove mo44 = new ChessMove(startPosition, po5, ChessPiece.PieceType.QUEEN); moves.add(mo44);
                ChessMove mo45 = new ChessMove(startPosition, po5, ChessPiece.PieceType.ROOK); moves.add(mo45);
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
                ChessMove mo52 = new ChessMove(startPosition, po6, ChessPiece.PieceType.BISHOP); moves.add(mo52);
                ChessMove mo53 = new ChessMove(startPosition, po6, ChessPiece.PieceType.KNIGHT); moves.add(mo53);
                ChessMove mo54 = new ChessMove(startPosition, po6, ChessPiece.PieceType.QUEEN); moves.add(mo54);
                ChessMove mo55 = new ChessMove(startPosition, po6, ChessPiece.PieceType.ROOK); moves.add(mo55);
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
                ChessMove mo52 = new ChessMove(startPosition, po7, ChessPiece.PieceType.BISHOP); moves.add(mo52);
                ChessMove mo53 = new ChessMove(startPosition, po7, ChessPiece.PieceType.KNIGHT); moves.add(mo53);
                ChessMove mo54 = new ChessMove(startPosition, po7, ChessPiece.PieceType.QUEEN); moves.add(mo54);
                ChessMove mo55 = new ChessMove(startPosition, po7, ChessPiece.PieceType.ROOK); moves.add(mo55);
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
}