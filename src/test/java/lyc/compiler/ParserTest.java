package lyc.compiler;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java_cup.runtime.Symbol;
import lyc.compiler.factories.ParserFactory;

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
    void write() throws Exception {
        compilationSuccessful(readFromFile("write.txt"));
    }

    @Test
    @Disabled
    void read() throws Exception {
        compilationSuccessful(readFromFile("read.txt"));
    }

    //no anda
    @Test
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
    void whileStatement() throws Exception {
        compilationSuccessful(readFromFile("while.txt"));
    }

    private void compilationSuccessful(String input) throws Exception {
        System.out.println("compilationSuccessful:");

        var scanned = scan(input);
        System.out.println("Scanned:");
        System.out.println(scanned);

        var symbol = scan(input).sym;

        System.out.println("Symbol:");
        System.out.println(symbol);

        assertThat(scan(input).sym).isEqualTo(ParserSym.EOF);
    }

    private void compilationError(String input){
        assertThrows(Exception.class, () -> scan(input));
    }

    private Symbol scan(String input) throws Exception {
        return ParserFactory.create(input).parse();
    }

    private String readFromFile(String fileName) throws IOException {
        System.out.println("Print 1:");

        InputStream resource = getClass().getResourceAsStream("/%s".formatted(fileName));
        System.out.println("Resource:");
        System.out.println(resource);
        assertThat(resource).isNotNull();
        System.out.println("Print 3");
        String result = IOUtils.toString(resource, StandardCharsets.UTF_8);
        System.out.println("Result");
        System.out.println(result);

        return result;
    }
}
