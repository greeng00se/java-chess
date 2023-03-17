package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CommandTest {

    @Test
    void 올바른_입력값이_아니라면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> Command.from("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 명령어를 입력해주세요.");
    }

    @Test
    void Command가_정상_반환된다() {
        // given
        final Command command = Command.from("start");

        // expect
        assertThat(command).isEqualTo(Command.START);
    }

    @Test
    void 명령어가_올바른_길이가_아니라면_예외를_던진다() {
        // given
        final List<String> invalidCommands = List.of("move", "e2", "e4", "e6");

        // expect
        assertThatThrownBy(() -> Command.validateCommandSize(invalidCommands.size(), Command.MOVE_COMMAND_SIZE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 명령어를 입력해주세요.");
    }
}
