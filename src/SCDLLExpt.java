import java.io.PrintWriter;

/**
 * Some simple experiments with SimpleDLLs
 * @author Sam Rebelsky
 */
public class SCDLLExpt {
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);
    SimpleListExpt.expt1(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt2(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt3(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt4(pen, new SimpleCDLL<String>(), 3);
  } // main(String[]
} // SDLLExpt

