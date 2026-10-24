package m1lon;

public class Task4HeronGeometry {
    public static void main(String[] args) {
        double a = 3.0;
        double b = 4.0;
        double c = 5.0;
        boolean exists = (a + b > c) && (a + c > b) && (b + c > a);
        boolean rich = (a == b) && (b == c);
        boolean gg = (a == b) || (b == c) || (a == c);
        boolean right = (Math.pow(a, 2) + Math.pow(b, 2) == Math.pow(c, 2))
                || (Math.pow(a, 2) + Math.pow(c, 2) == Math.pow(b, 2))
                || (Math.pow(b, 2) + Math.pow(c, 2) == Math.pow(a, 2));
        System.out.println("Сторони трикутника: a = 3.0, b = 4.0, c = 5.0");
        System.out.println("--------------------------------------------------");
        System.out.println("Чи існує такий трикутник:      "+ exists);
        System.out.println("Чи є рівностороннім:           "+rich);
        System.out.println("Чи є рівнобедреним:            "+gg);
        System.out.println("Чи є прямокутним:              "+right);

    }
}