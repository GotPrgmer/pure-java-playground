package src.test.com.gotprgmer.pure_java.annotation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

public class AnnotationTest {
    public static void main(String[] args) throws ClassNotFoundException, IOException {

        Class<?> b1Class = Class.forName("src.main.com.gotprgmer.pure_java.annotation.SourceBread");

        Class<?> b2Class = Class.forName("src.main.com.gotprgmer.pure_java.annotation.ClassBread");

        Class<?> b3Class = Class.forName("src.main.com.gotprgmer.pure_java.annotation.RuntimeBread");

        File file = new File("src/main/com/gotprgmer/pure_java/annotation");
        System.out.println(Arrays.toString(file.list()));
        File[] listFiles = file.listFiles();

        // java 파일 읽기
        for(File f : Objects.requireNonNull(listFiles)) {
            if (f.getName().contains(".java")) {
                System.out.println("Reading file: " + f.getName());

                try {
                    // 파일의 모든 라인을 읽어서 콘솔에 출력
                    Files.lines(f.toPath()).forEach(System.out::println);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // annotation정보 읽기
        System.out.println(Arrays.toString(file.listFiles()));

        System.out.println(Arrays.toString(b1Class.getAnnotations()));

        System.out.println(Arrays.toString(b2Class.getAnnotations()));

        System.out.println(Arrays.toString(b3Class.getAnnotations()));

    }

}
