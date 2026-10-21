package m1lon;

import java.util.Random;
import java.util.Scanner;

public class Task5RobloxPetSimulator {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        String[] petNames = {
                "Dog", "Cat", "Dragon", "Unicorn", "Huge Titanic Cat"
        };

        int[] petPowers = {15, 25, 120, 350, 2000};
        int[] dropChances = {45, 30, 15, 8, 2};

        String[] inventoryNames = new String[8];
        int[] inventoryPowers = new int[8];
        int petCount = 0;

        int coins = 300;
        boolean running = true;

        while (running) {
            System.out.println("\n================================");
            System.out.println("     ROBLOX: PET SIMULATOR");
            System.out.println("================================");
            System.out.println("Баланс: " + coins + " монет");
            System.out.println("Питомцев в рюкзаке: " + petCount + "/8");

            System.out.println("\n1 - Открыть яйцо (100 монет)");
            System.out.println("2 - Открыть рюкзак-инвентарь");
            System.out.println("3 - Подсчитать силу команды");
            System.out.println("4 - Фармить монеты (+50...150)");
            System.out.println("0 - Выйти из игры");
            System.out.print("Ваш выбор: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
                continue;
            }

            switch (choice) {
                case 1:
                    if (coins < 100) {
                        System.out.println("Недостаточно монет!");
                        break;
                    }

                    if (petCount >= inventoryNames.length) {
                        System.out.println(
                                "Рюкзак переполнен! Продайте или удалите питомцев."
                        );
                        break;
                    }

                    coins -= 100;

                    System.out.println("🥚 Яйцо раскалывается...");

                    int roll = random.nextInt(100) + 1;
                    int cumulative = 0;
                    int selectedPet = 0;

                    for (int i = 0; i < dropChances.length; i++) {
                        cumulative += dropChances[i];
                        if (roll <= cumulative) {
                            selectedPet = i;
                            break;
                        }
                    }

                    if (selectedPet == 4) {
                        System.out.println(
                                "⭐⭐⭐ НЕВЕРОЯТНО! СУПЕР-ДЖЕКПОТ! ⭐⭐⭐"
                        );
                    }

                    System.out.println(
                            "Вам выпал: " + petNames[selectedPet]
                                    + "! (Сила: " + petPowers[selectedPet] + ")"
                    );

                    inventoryNames[petCount] = petNames[selectedPet];
                    inventoryPowers[petCount] = petPowers[selectedPet];
                    petCount++;

                    System.out.println(
                            "Питомец добавлен! Остаток монет: " + coins
                    );
                    break;

                case 2:
                    System.out.println("\n--- ВАШ РЮКЗАК ---");

                    if (petCount == 0) {
                        System.out.println("Рюкзак пуст!");
                    } else {
                        for (int i = 0; i < petCount; i++) {
                            System.out.println(
                                    "[Слот " + (i + 1) + "] "
                                            + inventoryNames[i]
                                            + " (Сила: " + inventoryPowers[i] + ")"
                            );
                        }
                    }
                    break;

                case 3:
                    int totalPower = 0;
                    int bestPet = -1;

                    for (int i = 0; i < petCount; i++) {
                        totalPower += inventoryPowers[i];

                        if (bestPet == -1
                                || inventoryPowers[i]
                                > inventoryPowers[bestPet]) {
                            bestPet = i;
                        }
                    }

                    System.out.println("\n--- СТАТИСТИКА КОМАНДЫ ---");
                    System.out.println(
                            "Количество питомцев: " + petCount
                    );
                    System.out.println(
                            "Общая сила команды: " + totalPower
                    );

                    if (bestPet != -1) {
                        System.out.println(
                                "🔥 Лучший питомец: "
                                        + inventoryNames[bestPet]
                                        + " (Сила: "
                                        + inventoryPowers[bestPet] + ")"
                        );
                    } else {
                        System.out.println("У вас пока нет питомцев!");
                    }
                    break;

                case 4:
                    int earned = random.nextInt(101) + 50;
                    coins += earned;

                    System.out.println(
                            "Вы фармили монеты и заработали "
                                    + earned + " монет!"
                    );
                    System.out.println("Баланс: " + coins);
                    break;

                case 0:
                    running = false;
                    System.out.println("Вы вышли из игры!");
                    break;

                default:
                    System.out.println("Неверный пункт меню!");
            }
        }

        sc.close();
    }
}
