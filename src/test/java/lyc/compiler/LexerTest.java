package lyc.compiler;

import lyc.compiler.factories.LexerFactory;
import lyc.compiler.model.CompilerException;
import lyc.compiler.model.InvalidIntegerException;
import lyc.compiler.model.InvalidLengthException;
import lyc.compiler.model.UnknownCharacterException;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static com.google.common.truth.Truth.assertThat;
import static lyc.compiler.constants.Constants.MAX_CSTRING;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Disabled
public class LexerTest {

  private Lexer lexer;

  @Test
  //@Disabled
  public void comment() throws Exception{
    scan("/*This is a comment*/");
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }

  @Test
  //@Disabled
  public void invalidStringConstantLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan("\"%s\"".formatted(getRandomString()));
      nextToken();
    });
  }

  @Test
  //@Disabled
  public void invalidIdLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan(getRandomString());
      nextToken();
    });
  }

  @Test
  //@Disabled
  public void invalidPositiveIntegerConstantValue() {
    assertThrows(InvalidIntegerException.class, () -> {
      scan("%d".formatted(9223372036854775807L));
      nextToken();
    });
  }

  @Test
  //@Disabled
  public void invalidNegativeIntegerConstantValue() {
    assertThrows(InvalidIntegerException.class, () -> {
      scan("%d".formatted(-9223372036854775807L));
      nextToken();
    });
  }

  @Test
  //@Disabled
  public void assignmentWithExpressions() throws Exception {
    scan("c=d*(e- 21)/4");
    assertThat(nextToken()).isEqualTo(ParserSym.T_ID);
    assertThat(nextToken()).isEqualTo(ParserSym.T_ASIG);
    assertThat(nextToken()).isEqualTo(ParserSym.T_ID);
    assertThat(nextToken()).isEqualTo(ParserSym.T_MUL);
    assertThat(nextToken()).isEqualTo(ParserSym.T_PARA);
    assertThat(nextToken()).isEqualTo(ParserSym.T_ID);
    assertThat(nextToken()).isEqualTo(ParserSym.T_RES);
    assertThat(nextToken()).isEqualTo(ParserSym.T_CINT);
    assertThat(nextToken()).isEqualTo(ParserSym.T_PARC);
    assertThat(nextToken()).isEqualTo(ParserSym.T_DIV);
    assertThat(nextToken()).isEqualTo(ParserSym.T_CINT);
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }

  @Test
  //@Disabled
  public void unknownCharacter() {
    assertThrows(UnknownCharacterException.class, () -> {
      scan("#");
      nextToken();
    });
  }

  @AfterEach
  public void resetLexer() {
    lexer = null;
  }

  private void scan(String input) {
    lexer = LexerFactory.create(input);
  }

  private int nextToken() throws IOException, CompilerException {
    return lexer.next_token().sym;
  }

  private static String getRandomString() {
    return new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS)
            .withinRange('a', 'z')
            .build().generate(MAX_CSTRING * 2);
  }
}
