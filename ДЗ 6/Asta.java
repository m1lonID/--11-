package m1lon;
public class Asta {


    private String name = "Аста";
    private int hp = 1000;
    private int maxHp = 1000;
    private int antiMagicEnergy = 20;
    private int baseAttack = 120;
    private int defense = 35;
    private boolean isBlackFormActive = false;
    private String[] skills = {
            "Удар Демоноборца",
            "Черный Ураган",
            "Метеорит Демона",
            "Форма Черного Демона"
    };

    private boolean limitUsed = false;

    public void showStatus() {
        System.out.println("\n===== СТАТУС АСТЫ =====");
        System.out.println("Имя: " + name);
        System.out.println("HP: " + hp + "/" + maxHp);

        int percent = hp * 100 / maxHp;
        System.out.print("Здоровье: [");

        for (int i = 0; i < 10; i++) {
            if (i < percent / 10) {
                System.out.print("#");
            } else {
                System.out.print("-");
            }
        }

        System.out.println("] " + percent + "%");
        System.out.println("Антимагия: " + antiMagicEnergy + "/100");
        System.out.println("Черная форма: " + isBlackFormActive);
    }

    public int basicSwordAttack() {
        int damage = baseAttack;

        if (isBlackFormActive) {
            damage = damage * 2;
        }

        antiMagicEnergy = antiMagicEnergy + 20;

        if (antiMagicEnergy > 100) {
            antiMagicEnergy = 100;
        }

        System.out.println(name + " атакует мечом Демоноборца!");
        System.out.println("Урон: " + damage);
        System.out.println("Антимагия: " + antiMagicEnergy + "/100");

        return damage;
    }

    public int useSkill(int skillIndex) {
        if (skillIndex < 0 || skillIndex > 3) {
            System.out.println("Такого навыка нет!");
            return 0;
        }

        int energy = 0;
        int damage = 0;

        if (skillIndex == 0) {
            energy = 25;
            damage = 220;
        } else if (skillIndex == 1) {
            energy = 50;
            damage = 380;
        } else if (skillIndex == 2) {
            energy = 75;
            damage = 550;
        } else if (skillIndex == 3) {
            energy = 100;
        }

        if (antiMagicEnergy < energy) {
            System.out.println("Недостаточно антимагии!");
            return 0;
        }

        if (skillIndex == 3 && isBlackFormActive) {
            System.out.println("Черная форма уже активна!");
            return 0;
        }

        antiMagicEnergy = antiMagicEnergy - energy;

        System.out.println(name + " использует навык: "
                + skills[skillIndex]);

        if (skillIndex == 3) {
            isBlackFormActive = true;
            hp = hp + 300;

            if (hp > maxHp) {
                hp = maxHp;
            }

            System.out.println("Аста активировал Черную форму!");
            System.out.println("Восстановлено 300 HP.");
            System.out.println("Сила следующих атак увеличена!");
        } else {
            System.out.println("Нанесено урона: " + damage);
        }

        return damage;
    }

    public void takeDamage(int incomingDmg) {
        int realDamage = incomingDmg - defense;

        if (realDamage < 15) {
            realDamage = 15;
        }

        hp = hp - realDamage;

        if (hp < 0) {
            hp = 0;
        }

        System.out.println("Аста получил " + realDamage + " урона.");
        System.out.println("Осталось HP: " + hp + "/" + maxHp);
    }

    public void pushLimits() {
        if (!limitUsed && hp > 0 && hp <= maxHp * 25 / 100) {
            limitUsed = true;

            hp = hp + 150;

            if (hp > maxHp) {
                hp = maxHp;
            }

            antiMagicEnergy = antiMagicEnergy + 50;

            if (antiMagicEnergy > 100) {
                antiMagicEnergy = 100;
            }

            System.out.println("\nАста вспоминает: «Моя магия — "
                    + "никогда не сдаваться!»");
            System.out.println("Аста преодолевает свои пределы!");
            System.out.println("+150 HP и +50 антимагии!");
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAntiMagicEnergy() {
        return antiMagicEnergy;
    }
}
