import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @Author YZ
 * @Date 29/06/2018
 */
public class Modify {
    public static void main(String[]args) {
        String line = "import org.springframework.boot.autoconfigure.SpringBootApplication;";
        if (line.contains("@SpringBootApplication")) {
            System.out.println("werty");
        }
    }
}
