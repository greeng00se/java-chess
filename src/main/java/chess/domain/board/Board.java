package chess.domain.board;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.Empty;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Board {

    private final Map<Position, Piece> board;
    private Color turn;

    public Board() {
        this(Collections.emptyMap(), Color.WHITE);
    }

    private Board(final Map<Position, Piece> board, final Color turn) {
        this.board = new HashMap<>(board);
        this.turn = turn;
    }

    public void initialize() {
        board.putAll(initializePiece(Color.WHITE, Rank.ONE));
        board.putAll(initializePawn(Color.WHITE, Rank.TWO));
        board.putAll(initializeEmptyPiece(List.of(Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX)));
        board.putAll(initializePawn(Color.BLACK, Rank.SEVEN));
        board.putAll(initializePiece(Color.BLACK, Rank.EIGHT));
    }

    private Map<Position, Piece> initializePiece(final Color color, final Rank rank) {
        final List<Piece> pieces = List.of(
                Rook.from(color), Knight.from(color), Bishop.from(color), Queen.from(color),
                King.from(color), Bishop.from(color), Knight.from(color), Rook.from(color)
        );
        final List<File> files = Arrays.stream(File.values()).collect(toList());

        return IntStream.range(0, pieces.size())
                .boxed()
                .collect(toMap(index -> Position.of(files.get(index), rank), pieces::get));
    }

    private Map<Position, Piece> initializePawn(final Color color, final Rank rank) {
        return Arrays.stream(File.values())
                .map(file -> Position.of(file, rank))
                .collect(toMap(Function.identity(), ignore -> Pawn.from(color)));
    }

    private Map<Position, Piece> initializeEmptyPiece(final List<Rank> ranks) {
        return ranks.stream()
                .flatMap(rank -> Arrays.stream(File.values()).map(file -> Position.of(file, rank)))
                .collect(toMap(Function.identity(), ignore -> Empty.create()));
    }

    public void move(final String source, final String target) {
        final Position sourcePosition = Position.from(source);
        final Position targetPosition = Position.from(target);
        final Piece piece = board.get(sourcePosition);

        validate(sourcePosition, targetPosition, piece);
        movePiece(sourcePosition, targetPosition, piece);
    }

    private void validate(final Position sourcePosition, final Position targetPosition, final Piece piece) {
        if (turn.isOpponent(piece.color())) {
            throw new IllegalArgumentException("상대방의 기물을 움직일 수 없습니다.");
        }
        if (!piece.isMovable(sourcePosition, targetPosition, board.get(targetPosition))) {
            throw new IllegalArgumentException("올바르지 않은 이동 명령어 입니다.");
        }
        if (isPieceExistsBetweenPosition(sourcePosition, targetPosition)) {
            throw new IllegalArgumentException("이동 경로에 다른 기물이 있을 수 없습니다.");
        }
    }

    private boolean isPieceExistsBetweenPosition(final Position sourcePosition, final Position targetPosition) {
        return sourcePosition.between(targetPosition).stream()
                .anyMatch(this::isPieceExists);
    }

    private boolean isPieceExists(final Position position) {
        return !board.get(position).equals(Empty.create());
    }

    private void movePiece(final Position sourcePosition, final Position targetPosition, final Piece piece) {
        board.put(targetPosition, piece);
        board.put(sourcePosition, Empty.create());
        turn = turn.nextTurn();
    }

    public boolean isInitialized() {
        return board.size() != 0;
    }

    public Map<Position, Piece> getBoard() {
        return Collections.unmodifiableMap(board);
    }
}
