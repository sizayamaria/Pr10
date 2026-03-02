import java.util.Scanner;
import java.util.InputMismatchException; // для перевірки числа

public class Main {

    static final int MAX = 15; // максимальна кількість користувачів
    static String[][] arr = new String[MAX][2]; // [i][0] - ім'я, [i][1] - пароль
    static int count = 0;

    static String[] forbidden = {"admin","pass","password","qwerty","ytrewq"}; // заборонені слова

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int menu;

        do {

            System.out.println("\n1 - реєстрація");
            System.out.println("2 - видалення");
            System.out.println("3 - аутентифікація");
            System.out.println("4 - список користувачів");
            System.out.println("0 - вихід");

            try {
                menu = sc.nextInt();
                sc.nextLine();
            }
            catch (InputMismatchException e) { // якщо введено не число
                System.out.println("Потрібно ввести число.");
                sc.nextLine();
                menu = -1;
                continue;
            }

            if (menu == 1) register(sc); // виклик реєстрації
            if (menu == 2) delete(sc); // виклик видалення
            if (menu == 3) login(sc); // виклик входу
            if (menu == 4) show(); // показати список

        } while (menu != 0); // умова завершення

        sc.close();
    }

    static void register(Scanner sc) {

        if (count >= MAX) { // якщо масив заповнений
            System.out.println("Більше користувачів додати не можна.");
            return;
        }

        System.out.print("Ім'я: ");
        String name = sc.nextLine();

        checkName(name);

        if (findUser(name) != -1) {
            System.out.println("Таке ім'я вже існує.");
            return;
        }

        System.out.print("Пароль: ");
        String pass = sc.nextLine(); // зчитування пароля

        checkPass(pass); // перевірка пароля

        arr[count][0] = name; // запис імені в масив
        arr[count][1] = pass; // запис пароля
        count++;

        System.out.println("Користувача додано.");
    }

    static void delete(Scanner sc) {

        System.out.print("Ім'я для видалення: ");
        String name = sc.nextLine();

        int index = findUser(name);

        if (index == -1) { // якщо не знайдено користувача
            System.out.println("Користувача не знайдено.");
            return;
        }

        for (int i = index; i < count - 1; i++) {
            arr[i][0] = arr[i + 1][0];
            arr[i][1] = arr[i + 1][1];
        }

        count--; // зменшення кількості користувачів
        System.out.println("Користувача видалено.");
    }

    static void login(Scanner sc) {

        System.out.print("Ім'я: ");
        String name = sc.nextLine();

        System.out.print("Пароль: ");
        String pass = sc.nextLine();

        int index = findUser(name);

        if (index != -1 && arr[index][1].equals(pass)) { // перевірка пароля
            System.out.println("Користувача аутентифіковано.");
        } else {
            System.out.println("Невірне ім'я або пароль.");
        }
    }

    static int findUser(String name) {

        for (int i = 0; i < count; i++) {
            if (arr[i][0].equals(name)) { // порівняння імені
                return i; // якщо знайдено тоді повертаємо індекс
            }
        }

        return -1; // якщо не знайдено
    }

    static void checkName(String name) {

        if (name == null || name.length() < 5) { // перевірка довжини
            throw new IllegalArgumentException("Ім'я мінімум 5 символів.");
        }

        for (int i = 0; i < name.length(); i++) { // перевірка кожного символу
            if (name.charAt(i) == ' ') { // якщо є пробіл
                throw new IllegalArgumentException("Ім'я без пробілів.");
            }
        }
    }

    static void checkPass(String pass) {

        if (pass == null || pass.length() < 10) { // перевірка довжини
            throw new IllegalArgumentException("Пароль мінімум 10 символів.");
        }

        int nums = 0;
        boolean spec = false; // перевірка символів

        for (int i = 0; i < pass.length(); i++) {

            char c = pass.charAt(i); // беремо символ

            if (c == ' ') { // якщо пробіл
                throw new IllegalArgumentException("Пароль без пробілів.");
            }

            if (c >= '0' && c <= '9') {
                nums++; // якщо цифра — рахуємо
            }
            else if ((c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z')) {
                // якщо буква — нічого не робимо
            }
            else {
                spec = true; // якщо інший символ — це спецсимвол
            }
        }

        for (int i = 0; i < forbidden.length; i++) { // перевірка заборонених слів
            if (pass.contains(forbidden[i])) {
                throw new IllegalArgumentException("Пароль містить заборонене слово.");
            }
        }

        if (nums < 3) {
            throw new IllegalArgumentException("Має бути мінімум 3 цифри.");
        }

        if (!spec) {
            throw new IllegalArgumentException("Потрібен спецсимвол.");
        }
    }

    static void show() {

        if (count == 0) { // якщо список пустий
            System.out.println("Поки що користувачів нема.");
            return;
        }

        for (int i = 0; i < count; i++) { // виведення списку
            System.out.println((i + 1) + ". " + arr[i][0]);
        }
    }
}