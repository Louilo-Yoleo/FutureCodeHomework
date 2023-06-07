package org.future.code.homework;

import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

/**
 * Правила выполнения домашнего задания:
 * <p>
 * 1. Вся работа выполняется в файле доамашнего задания
 * Если вас просят создать класс - его надо создать вложенным, т.е. внутри класса HomeWorkN
 * вы создаете класс MyClass {} (все в одном файле)
 * 2. Задание считается выполненным если все тесты пройдены.
 * Тест выполнен если напротив него находится знак ✅
 * 3. Что бы запустить тесты нажмите значек запуска кода напротив мтода main.
 * ‼️ВАЖНО‼️ Вся работа в модкле ведется на JDK-17, перед запуском скачайте его и установите
 * правильный JDK и уровень языка в Settings -> Project Structure
 */
public class HomeWork3 {

    private static final Map<Integer, Class<? extends Exception>> classesCorrelation = Map.of(
            1, SocketException.class,
            2, ClassNotFoundException.class,
            3, AccessDeniedException.class,
            4, ExportException.class
    );

    public static void raiseException(Integer exceptionId) throws SocketException, ClassNotFoundException, AccessDeniedException, ExportException { // Подсказка: throws Exception в итоге надо поменять на перечисление имен исключений
        // Напиши свой код тут для задания 1
        switch (exceptionId) {
            case (1):
                throw new SocketException();
            case (2):
                throw new ClassNotFoundException();
            case (3):
                throw new AccessDeniedException();
            case (4):
                throw new ExportException();
        }
    }

    public static String catchException(Integer integer) {
        // Напиши свой код тут для задания 2
        try {
            raiseException(integer);
        } catch (SocketException e) {
            return "SocketException";
        } catch (ClassNotFoundException e) {
            return "ClassNotFoundException";
        } catch (AccessDeniedException e) {
            return "HomeWork3$AccessDeniedException";
        } catch (ExportException e) {
            return "HomeWork3$ExportException";
        }
        return "Exception";
    }

    public static void main(String[] args) {
        testFirstTask();
        testSecondTask();
    }

    public static void testFirstTask() {
        System.out.println("\nTests for raiseException");
        AntiCheat.run();
        for (Integer exceptionId : classesCorrelation.keySet().stream().sorted().toList()) {
            try {
                raiseException(exceptionId);
                printTestCase(exceptionId, "Должна была выкинуться ошибка", "Ошибка не выкинулась", 45);
            } catch (Exception e) {
                printTestCase(exceptionId, classesCorrelation.get(exceptionId).toString(), e.getClass().toString(), 45);
            }
        }
    }

    /* Техническая секция - сюда писать ничего не надо */

    public static void testSecondTask() {
        System.out.println("\nTests for catchException");
        AntiCheat.run();
        Function<String, String> f = str -> {
            List<String> r = Arrays.stream(str.split("\\.")).toList();
            return r.get(r.size() - 1);
        };
        for (Integer exceptionId : classesCorrelation.keySet().stream().sorted().toList())
            printTestCase(exceptionId, f.apply(classesCorrelation.get(exceptionId).toString()), catchException(exceptionId), 30);
    }

    public static String constLen(String str, int len) {
        StringBuilder sb = new StringBuilder(str);
        while (len-- - str.length() > 0) sb.append(" ");
        return sb.toString();
    }

    public static void printTestCase(int n, String exp, String act, int minLen) {
        Function<String, String> green = str -> "\u001B[34m" + str + "\u001B[0m";
        Function<String, String> yellow = str -> "\u001B[33m" + str + "\u001B[0m";
        System.out.print("TEST CASE " + constLen(String.valueOf(n), 4));
        System.out.print("Ожидание: " + yellow.apply(constLen(exp, minLen)) + " Реальность: " + green.apply(constLen(act, minLen) + " "));
        if (Objects.equals(exp, act)) System.out.print("✅");
        else System.out.print("❌");
        System.out.println();
    }

    /**
     * Задание:
     * <p>
     * 1. Реализуйте метод raiseException(ниже) который принимет число и выбрасывает исключения:
     * если число 1 то выбросить SocketException
     * если число 2 то выбросить ClassNotFoundException
     * если число 3 то выбросить AccessDeniedException
     * если число 4 то выбросить ExportException
     * <p>
     * 2. Реализуйте метод catchException который вызывает raiseException затем ловит каждое
     * исключение отдельным блоком catch и возвращяет его название:
     * для SocketException вернуть строку "SocketException"
     * для ClassNotFoundException вернуть строку "ClassNotFoundException"
     * для AccessDeniedException вернуть строку "AccessDeniedException"
     * для ExportException вернуть строку "ExportException"
     */

    static class ExportException extends Exception {
        public ExportException() {
        }

        public ExportException(String message) {
            super(message);
        }
    }

    static class AccessDeniedException extends Exception {
        public AccessDeniedException() {
        }

        public AccessDeniedException(String message) {
            super(message);
        }
    }

    public static class AntiCheat {
        public static void run() {
            String sb = "";
            List<String> antiCheatList = new ArrayList<>();
            antiCheatList.addAll(classesCorrelation.keySet().stream().map(Object::toString).toList());
            antiCheatList.addAll(classesCorrelation.values().stream().map(Object::toString).toList());
            antiCheatList.add(sb);
            calcHash(antiCheatList);
        }

        public static String bytesToHex(byte[] bytes) {
            char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
            }
            return new String(hexChars);
        }

        public static void calcHash(List<String> list) {
            String total = String.join("", list);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(total.getBytes());
                byte[] digest = md.digest();
                System.out.println("AntiCheatCheck: " + bytesToHex(digest));
            } catch (NoSuchAlgorithmException ignored) {
            }
        }
    }

}