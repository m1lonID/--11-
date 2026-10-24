package m1lon;

import java.util.Scanner;

public class Task5BrawlStarsArena {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==============================================");
        System.out.println("       BRAWL STARS: BOSS FIGHT");
        System.out.println("==============================================");

        System.out.println("Оберіть свого бійця:");
        System.out.println("1. Шеллі (Shelly) - HP: 4000 | Атака: 1200 | Супер: 2800");
        System.out.println("2. Кольт (Colt) - HP: 3000 | Атака: 1600 | Супер: 3600");
        System.out.println("3. Ель Прімо (Primo) - HP: 6000 | Атака: 1100 | Супер: 2200");

        int choice = 0;

        for (; choice < 1 || choice > 3; ) {
            System.out.print("Ваш вибір (1-3): ");
            choice = scanner.nextInt();

            if (choice < 1 || choice > 3) {
                System.out.println("Введіть число від 1 до 3!");
            }
        }

        String playerName;
        int maxPlayerHp;
        int playerDamage;
        int superDamage;

        if (choice == 1) {
            playerName = "Шеллі";
            maxPlayerHp = 4000;
            playerDamage = 1200;
            superDamage = 2800;
        } else if (choice == 2) {
            playerName = "Кольт";
            maxPlayerHp = 3000;
            playerDamage = 1600;
            superDamage = 3600;
        } else {
            playerName = "Ель Прімо";
            maxPlayerHp = 6000;
            playerDamage = 1100;
            superDamage = 2200;
        }

        int playerHp = maxPlayerHp;
        int bossHp = 10000;
        int maxBossHp = 10000;
        int bossBaseDamage = 700;

        int ammo = 3;
        int superCharge = 0;
        int powerCubes = 0;
        int rounds = 0;

        boolean bossStunned = false;
        boolean bossKnockback = false;

        System.out.println("\nВи обрали бійця: " + playerName);
        System.out.println("Бій починається!");


        for (; playerHp > 0 && bossHp > 0; ) {
            rounds++;


            if (rounds > 1 && ammo < 3) {
                ammo++;
            }

            System.out.println("\n================ РАУНД "
                    + rounds + " ================");

            System.out.print("[" + playerName + "] HP: ");
            drawHealthBar(playerHp, maxPlayerHp);
            System.out.println(" " + playerHp + "/" + maxPlayerHp
                    + " | Набої: " + ammo + "/3"
                    + " | Супер: " + superCharge + "%");

            System.out.print("[Робо-Бос] HP: ");
            drawHealthBar(bossHp, maxBossHp);
            System.out.print(" " + bossHp + "/" + maxBossHp);

            if (bossHp > 6000) {
                System.out.println(" | Фаза: ЗВИЧАЙНА");
            } else if (bossHp >= 3000) {
                System.out.println(" | Фаза: ANGRY (+30% атаки)");
            } else {
                System.out.println(" | Фаза: ENRAGED (+70% атаки)");
            }

            System.out.println("Куби підсилення: " + powerCubes);

            System.out.println("\nОберіть дію:");
            System.out.println("1 - Базова атака");
            System.out.println("2 - СУПЕР-АТАКА");
            System.out.println("3 - Захисний маневр");
            System.out.println("4 - Пошук Power Cube");
            System.out.print("Ваш хід: ");

            int action = scanner.nextInt();
            boolean playerActed = false;


            if (action == 1) {
                if (ammo > 0) {
                    ammo--;

                    int damage = (int) (playerDamage
                            * (1 + powerCubes * 0.15));

                    bossHp = Math.max(0, bossHp - damage);
                    superCharge = Math.min(100, superCharge + 35);

                    System.out.println("\nШеллі/боєць атакує!");
                    System.out.println("Завдано " + damage + " шкоди!");

                    if (superCharge >= 100) {
                        System.out.println("СУПЕР ГОТОВИЙ!");
                    }

                    playerActed = true;
                } else {
                    System.out.println("Немає набоїв! Хід втрачено.");
                }

            } else if (action == 2) {
                if (superCharge >= 100) {
                    int damage = (int) (superDamage
                            * (1 + powerCubes * 0.15));

                    bossHp = Math.max(0, bossHp - damage);
                    superCharge = 0;

                    System.out.println("\nСУПЕР-АТАКА!");
                    System.out.println("Завдано " + damage + " шкоди!");

                    if (choice == 1) {
                        bossKnockback = true;
                        System.out.println("Боса відкинуто! "
                                + "Наступна атака слабша на 50%.");
                    } else if (choice == 3) {
                        bossStunned = true;
                        System.out.println("Боса оглушено! "
                                + "Він пропустить наступний хід.");
                    }

                    playerActed = true;
                } else {
                    System.out.println("Супер ще не заряджений! "
                            + "Потрібно 100%.");
                }

            } else if (action == 3) {
                ammo = Math.min(3, ammo + 2);

                int healing = (int) (maxPlayerHp * 0.15);
                playerHp = Math.min(maxPlayerHp, playerHp + healing);

                System.out.println("\nЗахисний маневр!");
                System.out.println("Відновлено HP: " + healing);
                System.out.println("Набої: " + ammo + "/3");

                playerActed = true;

            } else if (action == 4) {
                if (Math.random() < 0.6) {
                    powerCubes++;

                    System.out.println("\nВи знайшли Power Cube!");
                    System.out.println("Шкода збільшена на 15%.");
                } else {
                    System.out.println("\nPower Cube не знайдено.");
                }

                playerActed = true;

            } else {
                System.out.println("Невідома дія!");
            }


            if (bossHp > 0 && playerActed) {
                System.out.println("\nХід Робо-Боса:");

                if (bossStunned) {
                    System.out.println("Бос оглушений і пропускає хід!");
                    bossStunned = false;

                } else {
                    int bossDamage = bossBaseDamage;

                    if (bossHp <= 6000 && bossHp >= 3000) {
                        bossDamage = (int) (bossDamage * 1.3);
                    } else if (bossHp < 3000) {
                        bossDamage = (int) (bossDamage * 1.7);
                    }

                    if (bossKnockback) {
                        bossDamage = (int) (bossDamage * 0.5);
                        bossKnockback = false;
                    }

                    if (Math.random() < 0.15) {
                        System.out.println("Боєць ухиляється від удару!");
                    } else {
                        playerHp = Math.max(0, playerHp - bossDamage);

                        System.out.println("Бос завдає "
                                + bossDamage + " шкоди!");
                    }
                }
            }


        }

        System.out.println("\n==============================================");

        if (playerHp > 0) {
            System.out.println("ПЕРЕМОГА! РОБО-БОСА ЗНИЩЕНО!");
            System.out.println("Раундів зіграно: " + rounds);
            System.out.println("Зібрано Power Cubes: " + powerCubes);
            System.out.println("Нагорода: +10 Трофеїв, 100 Starr Drops!");
        } else {
            System.out.println("ПОРАЗКА! Сейф зруйновано!");
            System.out.println("Раундів зіграно: " + rounds);
            System.out.println("Спробуйте обрати Ель Прімо!");
        }

        System.out.println("==============================================");

        scanner.close();
    }


    public static void drawHealthBar(int currentHp, int maxHp) {
        int bars = (int) (((double) currentHp / maxHp) * 10);

        System.out.print("[");

        for (int i = 0; i < 10; i++) {
            if (i < bars) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }

        System.out.print("]");
    }
}
