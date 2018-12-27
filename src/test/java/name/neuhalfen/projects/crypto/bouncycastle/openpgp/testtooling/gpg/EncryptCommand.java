package name.neuhalfen.projects.crypto.bouncycastle.openpgp.testtooling.gpg;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import org.bouncycastle.util.io.Streams;

public class EncryptCommand implements Command {

  private final byte[] plaintext;
  private final String recipient;

  public EncryptCommand(final byte[] plaintext, final String recipient) {
    this.plaintext = plaintext;
    this.recipient = recipient;
  }

  public final static class EncryptCommandResult implements Result<EncryptCommand> {

    private final int exitCode;
    private final byte[] output;

    private EncryptCommandResult(final int exitCode, final byte[] output) {
      this.exitCode = exitCode;
      this.output = output;
    }

    @Override
    public int exitCode() {
      return exitCode;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", EncryptCommandResult.class.getSimpleName() + "[", "]")
          .add("exitCode=" + exitCode)
          .add("output=" + Arrays.toString(output))
          .toString();
    }
  }

  @Override
  public List<String> getArgs() {
    return asList("--encrypt", "--batch","--armor", "--recipient", recipient);
  }

  public void io(OutputStream outputStream, InputStream inputStream, InputStream errorStream)
      throws IOException {
    outputStream.write(plaintext);
    outputStream.close();
  }

  @Override
  public EncryptCommandResult parse(InputStream stdout, int exitCode) {
    //  nothing to do
    byte[] output;
    try {
      final byte[] bytes = Streams.readAll(stdout);
      output = Arrays.copyOf(bytes,bytes.length);
    } catch (IOException e) {
      output = null;
    }
    return new EncryptCommandResult(exitCode, output);
  }
}