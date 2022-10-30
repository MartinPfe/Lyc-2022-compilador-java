package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.factories.ParserFactory;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@Disabled
public class ParserTest {

    @Test
    @Disabled
    public void assignmentWithExpression() throws Exception {
        compilationSuccessful("c=d*(e- 21)/4");
    }

    @Test
    @Disabled
    public void syntaxError() {
        compilationError("1234");
    }

    @Test
    @Disabled
    void assignments() throws Exception {
        compilationSuccessful(readFromFile("assignments.txt"));
    }

    @Test
    @Disabled
    void declarations() throws Exception {
        compilationSuccessful(readFromFile("declarations.txt"));
    }

    @Test
    @Disabled
    void write() throws Exception {
        compilationSuccessful(readFromFile("write.txt"));
    }

    @Test
    @Disabled
    void read() throws Exception {
        compilationSuccessful(readFromFile("read.txt"));
    }

    @Test
    @Disabled
    void comment() throws Exception {
        compilationSuccessful(readFromFile("comment.txt"));
    }

    @Test
    @Disabled
    void init() throws Exception {
        compilationSuccessful(readFromFile("init.txt"));
    }

    @Test
    @Disabled
    void and() throws Exception {
        compilationSuccessful(readFromFile("and.txt"));
    }

    @Test
    @Disabled
    void or() throws Exception {
        compilationSuccessful(readFromFile("or.txt"));
    }

    @Test
    @Disabled
    void not() throws Exception {
        compilationSuccessful(readFromFile("not.txt"));
    }

    @Test
    @Disabled
    void ifStatement() throws Exception {
        compilationSuccessful(readFromFile("if.txt"));
    }

    @Test
    @Disabled
    void ifStatementWithConstant() throws Exception {
        compilationSuccessful(readFromFile("if-constantes.txt"));
    }

    @Test
    @Disabled
    void ifStatementWithConstantsInverted() throws Exception {
        compilationSuccessful(readFromFile("if-constantes-invertido.txt"));
    }

    @Test
    @Disabled
    void ifStatementWithMultipleConditionAndConstants() throws Exception {
        compilationSuccessful(readFromFile("if-compuesto-constantes.txt"));
    }

    @Test
    @Disabled
    void whileStatement() throws Exception {
        compilationSuccessful(readFromFile("while.txt"));
    }

    private void compilationSuccessful(String input) throws Exception {
        assertThat(scan(input).sym).isEqualTo(ParserSym.EOF);
    }

    private void compilationError(String input){
        assertThrows(Exception.class, () -> scan(input));
    }

    private Symbol scan(String input) throws Exception {
        return ParserFactory.create(input).parse();
    }

    private String readFromFile(String fileName) throws IOException {
        InputStream resource = getClass().getResourceAsStream("/%s".formatted(fileName));
        assertThat(resource).isNotNull();
        return IOUtils.toString(resource, StandardCharsets.UTF_8);
    }
}
