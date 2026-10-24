package m1lon;

import java.util.Scanner;

public class Task2DeliveryService {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ТАРИФІКАТОР СЛУЖБИ ДОСТАВКИ ===");

        double weight;
        int distance;
        int deliveryType;
        String premiumAnswer;

        while (true) {
            System.out.print("Введіть вагу відправлення (кг): ");
            if (scanner.hasNextDouble()) {
                weight = scanner.nextDouble();
                scanner.nextLine();

                if (weight > 0 && weight <= 50.0) {
                    break;
                }
                System.out.println("Помилка: вага має бути > 0 та <= 50.0 кг.");
            } else {
                System.out.println("Помилка: введіть число.");
                scanner.nextLine();
            }
        }


        while (true) {
            System.out.print("Введіть відстань транспортування (км): ");
            if (scanner.hasNextInt()) {
                distance = scanner.nextInt();
                scanner.nextLine();

                if (distance > 0) {
                    break;
                }
                System.out.println("Помилка: відстань має бути > 0 км.");
            } else {
                System.out.println("Помилка: введіть ціле число.");
                scanner.nextLine();
            }
        }


        while (true) {
            System.out.print("Оберіть пункт призначення (1 – Відділення, 2 – Поштомат, 3 – Кур'єр): ");
            if (scanner.hasNextInt()) {
                deliveryType = scanner.nextInt();
                scanner.nextLine();

                if (deliveryType >= 1 && deliveryType <= 3) {
                    break;
                }
                System.out.println("Помилка: оберіть 1, 2 або 3.");
            } else {
                System.out.println("Помилка: введіть число 1, 2 або 3.");
                scanner.nextLine();
            }
        }


        if (deliveryType == 2 && weight > 15.0) {
            System.out.println("Помилка: поштомат не приймає габаритні посилки вагою понад 15.0 кг.");
            scanner.close();
            return;
        }

        System.out.print("Чи є у вас карта Premium? (так/ні): ");
        premiumAnswer = scanner.nextLine();

        boolean isPremium = premiumAnswer.equalsIgnoreCase("так");


        double baseTariff = switch (deliveryType) {
            case 1 -> 50.0;
            case 2 -> weight > 15.0 ? -1.0 : 60.0;
            case 3 -> 100.0;
            default -> -1.0;
        };

        if (baseTariff < 0) {
            System.out.println("Помилка: невірний тип доставки.");
            scanner.close();
            return;
        }


        double distanceFee;

        if (distance <= 50) {
            distanceFee = 0.0;
        } else if (distance <= 200) {
            distanceFee = 35.0;
        } else {
            distanceFee = 80.0;
        }

        double totalBeforeDiscount = baseTariff + distanceFee;

        double discount = isPremium ? totalBeforeDiscount * 0.20 : 0.0;
        double total = totalBeforeDiscount - discount;

        String deliveryName = switch (deliveryType) {
            case 1 -> "Відділення";
            case 2 -> "Поштомат";
            case 3 -> "Кур'єр";
            default -> "Помилка";
        };

        System.out.println();
        System.out.println("------------- НАКЛАДНА ДОСТАВКИ -------------");
        System.out.printf("Тип доставки:              %s (базовий тариф: %.2f грн)%n",
                deliveryName, baseTariff);
        System.out.printf("Доплата за відстань:       %.2f грн (%d км)%n",
                distanceFee, distance);
        System.out.printf("Сума до знижки:             %.2f грн%n",
                totalBeforeDiscount);
        System.out.printf("Статус клієнта:             %s%n",
                isPremium ? "Premium (-20%)" : "Звичайний");
        System.out.println("---------------------------------------------");
        System.out.printf("РАЗОМ ДО СПЛАТИ:            %.2f грн%n", total);
        System.out.println("=============================================");

        scanner.close();
    }
}

