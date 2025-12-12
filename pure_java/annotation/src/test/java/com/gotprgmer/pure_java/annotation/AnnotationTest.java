package com.gotprgmer.pure_java.annotation;

import org.objectweb.asm.*;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AnnotationTest {
    private static final String BREAD_INTERNAL = "com/gotprgmer/pure_java/annotation/Bread";
    public static void main(String[] args) throws ClassNotFoundException, IOException {
//
        Class<?> b1Class = Class.forName("com.gotprgmer.pure_java.annotation.SourceBread");
//
        Class<?> b2Class = Class.forName("com.gotprgmer.pure_java.annotation.ClassBread");

        Class<?> b3Class = Class.forName("com.gotprgmer.pure_java.annotation.RuntimeBread");

        File file = new File("./pure_java/annotation/build/libs");
        File[] listFiles = file.listFiles();

        for (File f : listFiles) {
            if (!f.getName().endsWith(".jar")) continue;

            try (JarFile jar = new JarFile(f)) {
                jar.stream()
                        .filter(e -> !e.isDirectory())
                        .filter(e -> e.getName().endsWith(".class"))
                        .forEach(e -> {
                            try (InputStream in = jar.getInputStream(e)) {
                                ClassReader cr = new ClassReader(in);

                                // 1) Bread를 "참조"하는지 먼저 빠르게 판단 (상수풀 검색)
                                if (!referencesBread(cr)) return;

                                System.out.println("\n==============================");
                                System.out.println("JAR  : " + f.getAbsolutePath());
                                System.out.println("CLASS: " + e.getName());
                                System.out.println("==============================");

                                // 2) 전문 덤프
                                TraceClassVisitor tcv =
                                        new TraceClassVisitor(null, new Textifier(), new PrintWriter(System.out));
                                cr.accept(tcv, 0);

                            } catch (Exception ex) {
                                throw new RuntimeException("Failed to dump: " + e.getName() + " in " + f.getName(), ex);
                            }
                        });
            }
        }
        // annotation정보 읽기
        System.out.println(Arrays.toString(b1Class.getAnnotations()));

        System.out.println(Arrays.toString(b2Class.getAnnotations()));

        System.out.println(Arrays.toString(b3Class.getAnnotations()));

    }

    private static boolean referencesBread(ClassReader cr) {
        // this_class / super_class / interfaces는 기본적으로 확실히 검사
        if (BREAD_INTERNAL.equals(cr.getSuperName())) return true;
        // 구현한 인터페이스의 이름기반으로 검색
        for (String itf : cr.getInterfaces()) {
            if (BREAD_INTERNAL.equals(itf)) return true;
        }
        return false;
    }

}
