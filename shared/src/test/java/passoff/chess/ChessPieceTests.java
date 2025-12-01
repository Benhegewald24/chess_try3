package passoff.chess;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;


public class ChessPieceTests extends EqualsTestingUtility<ChessPiece>
{
    public ChessPieceTests()
    {
        super("ChessPiece", "pieces");
    }

    @Override
    protected ChessPiece buildOriginal()
    {
        return new ChessPiece(WHITE, KING);
    }

    @Override
    protected Collection<ChessPiece> buildAllDifferent()
    {
        return List.of(
                new ChessPiece(BLACK, KING),
                new ChessPiece(WHITE, QUEEN),
                new ChessPiece(BLACK, QUEEN),
                new ChessPiece(WHITE, PAWN),
                new ChessPiece(BLACK, PAWN)
        );
    }


    @Test
    @DisplayName("Piece Move on All Pieces")
    public void pieceMoveAllPieces()
    {
        var board = new ChessBoard();

        // 6 piece types * 2 team colors = 12 different pieces
        Collection<ChessPiece> allPossiblePieces =
                Arrays.stream(ChessPiece.PieceType.values())
                .flatMap(pieceType -> Arrays.stream(ChessGame.TeamColor.values())
                .map(teamColor -> new ChessPiece(teamColor, pieceType))).toList();

        // 8 rows * 8 cols * 12 pieces = 768 evaluations - 32 pawns on back rows
        for (int i = 1; i <= 8; i++)
        {
            for (int j = 1; j <= 8; j++)
            {
                ChessPosition position = new ChessPosition(i, j);

                for (var piece : allPossiblePieces)
                {
                    if (piece.getPieceType() == PAWN && (i == 1 || i == 8))
                    {
                        continue;
                    }

                    board.addPiece(position, piece);
                    Assertions.assertDoesNotThrow(() -> piece.pieceMoves(board, position),
                            "No pieces anywhere on the board should throw an error. " + "Tested: " + piece + " at " + position + ".");
                }

                board.addPiece(position, null);
            }
        }
    }

}
