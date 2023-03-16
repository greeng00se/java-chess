package chess.domain.board;

import java.util.Arrays;

public enum Rank {
    ONE("1", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    ;

    private final String command;
    private final int position;

    Rank(final String command, final int position) {
        this.command = command;
        this.position = position;
    }

    public static Rank from(final String command) {
        return Arrays.stream(values())
                .filter(value -> value.command.equalsIgnoreCase(command))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("랭크는 1 ~ 8 사이의 값이어야 합니다."));
    }

    public static Rank from(final int position) {
        return Arrays.stream(values())
                .filter(value -> value.position == position)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("위치 값은 1 ~ 8 사이의 값이어야 합니다."));
    }

    public int calculateGap(final Rank target) {
        return position - target.position;
    }
}
