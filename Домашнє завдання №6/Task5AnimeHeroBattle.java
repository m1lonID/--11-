package m1lon;
import java.util.Scanner;

public class Task5AnimeHeroBattle {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Asta asta = new Asta();

        String bossName = "Патри, Маг Света";
        int bossHp = 1200;
        int bossMaxHp = 1200;

        System.out.println("======================================");
        System.out.println("      АРЕНА КОРОЛЕВСТВА КЛОНОВ");
        System.out.println("======================================");
        System.out.println("Босс: " + bossName);
        System.out.println("HP босса: " + bossHp + "/" + bossMaxHp);

        while (asta.isAlive() && bossHp > 0) {

            asta.showStatus();

            System.out.println("\nHP босса: " + bossHp + "/" + bossMaxHp);
            System.out.println("\n--- Твой ход ---");
            System.out.println("[1] Удар мечом");
            System.out.println("[2] Использовать суперспособность");
            System.out.println("[3] Защитная стойка");
            System.out.println("[4] Посмотреть статус");
            System.out.print("Твой выбор: ");

            int choice;

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Введи число от 1 до 4!");
                scanner.next();
                continue;
            }

            int damage = 0;
            boolean defending = false;
            boolean actionDone = true;

            switch (choice) {
                case 1:
                    damage = asta.basicSwordAttack();
                    break;

                case 2:
                    System.out.println("\nВыбери навык:");
                    System.out.println("[1] Удар Демоноборца (25 энергии)");
                    System.out.println("[2] Черный Ураган (50 энергии)");
                    System.out.println("[3] Метеорит Демона (75 энергии)");
                    System.out.println("[4] Форма Черного Демона (100 энергии)");
                    System.out.print("Твой выбор: ");

                    int skill;

                    if (scanner.hasNextInt()) {
                        skill = scanner.nextInt() - 1;
                    } else {
                        scanner.next();
                        System.out.println("Нужно ввести число!");
                        continue;
                    }

                    if (skill < 0 || skill > 3) {
                        System.out.println("Неправильный номер навыка!");
                        actionDone = false;
                    } else if (asta.getAntiMagicEnergy() <
                            getSkillCost(skill)) {
                        System.out.println("Не хватает антимагии!");
                        actionDone = false;
                    } else {
                        damage = asta.useSkill(skill);
                    }
                    break;

                case 3:
                    defending = true;
                    System.out.println("Аста приготовился защищаться!");
                    break;

                case 4:
                    asta.showStatus();
                    actionDone = false;
                    break;

                default:
                    System.out.println("Такого действия нет!");
                    actionDone = false;
            }

            if (!actionDone) {
                continue;
            }

            bossHp = bossHp - damage;

            if (bossHp < 0) {
                bossHp = 0;
            }

            if (bossHp <= 0) {
                break;
            }

            System.out.println("\n--- Ход босса ---");

            int bossDamage = 100 + (int) (Math.random() * 61);

            int attackNumber = (int) (Math.random() * 3);

            if (attackNumber == 0) {
                System.out.println(bossName
                        + " использует «Променями Покарання Света»!");
            } else if (attackNumber == 1) {
                System.out.println(bossName
                        + " атакует магическим мечом!");
            } else {
                System.out.println(bossName
                        + " выпускает магический взрыв!");
            }

            if (defending) {
                bossDamage = bossDamage / 2;
                System.out.println("Аста блокирует часть урона!");
            }

            asta.takeDamage(bossDamage);
            asta.pushLimits();

            System.out.println("\n========== СТАТУС БОЯ ==========");
            System.out.println("Аста: " + asta.getHp()
                    + "/" + asta.getMaxHp() + " HP");
            System.out.println(bossName + ": "
                    + bossHp + "/" + bossMaxHp + " HP");
            System.out.println("================================");
        }

        System.out.println("\n========== КОНЕЦ БОЯ ==========");

        if (asta.isAlive()) {
            System.out.println("ПОБЕДА! Аста победил " + bossName + "!");
        } else {
            System.out.println("ПОРАЖЕНИЕ! Аста проиграл.");
        }

        scanner.close();
    }

    public static int getSkillCost(int skill) {
        if (skill == 0) {
            return 25;
        } else if (skill == 1) {
            return 50;
        } else if (skill == 2) {
            return 75;
        } else {
            return 100;
        }
    }
}
