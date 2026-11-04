package m1lon;

class DungeonEntity {
    private String name;
    private int level;
    protected int hp;
    protected int maxHp;
    protected int baseAttack;
    protected int defense;

    public DungeonEntity(String name, int level, int maxHp, int baseAttack, int defense) {
        this.name = (name == null || name.trim().isEmpty()) ? "Невідома сутність" : name;
        this.level = Math.max(1, level);
        this.maxHp = Math.max(1, maxHp);
        this.hp = this.maxHp;
        this.baseAttack = Math.max(0, baseAttack);
        this.defense = Math.max(0, defense);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getDefense() {
        return defense;
    }

    public int takeDamage(int rawDamage) {
        int actual = Math.max(5, rawDamage - defense);
        hp = Math.max(0, hp - actual);
        return actual;
    }

    public int performAttack() {
        return baseAttack;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    protected String buildHealthBar() {
        int filled = (int) Math.round((double) hp / maxHp * 10);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        bar.append("]");
        return bar.toString();
    }

    protected void increaseLevel() {
        level++;
    }

    @Override
    public String toString() {
        return String.format(
                "DungeonEntity{name='%s', lvl=%d, HP=%s %d/%d, ATK=%d, DEF=%d}",
                name, level, buildHealthBar(), hp, maxHp, baseAttack, defense
        );
    }
}

class Hunter extends DungeonEntity {
    private char rank;
    protected int mana;
    protected int maxMana;

    public Hunter(String name, int level, int maxHp, int baseAttack,
                  int defense, char rank, int maxMana) {
        super(name, level, maxHp, baseAttack, defense);
        this.rank = isValidRank(rank) ? rank : 'E';
        this.maxMana = Math.max(0, maxMana);
        this.mana = this.maxMana;
    }

    private boolean isValidRank(char rank) {
        return rank == 'E' || rank == 'D' || rank == 'C'
                || rank == 'B' || rank == 'A' || rank == 'S';
    }

    public char getRank() {
        return rank;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int useManaSkill(String skillName, int manaCost, double multiplier) {
        if (manaCost > 0 && mana >= manaCost && multiplier > 0) {
            mana -= manaCost;
            System.out.println("✨ " + skillName + "! Витрачено " + manaCost + " MP.");
            return (int) (performAttack() * multiplier);
        }

        System.out.println("⚠️ Недостатньо мани або неправильні параметри навички. Звичайна атака!");
        return performAttack();
    }

    @Override
    public String toString() {
        return "Hunter{" + super.toString()
                + String.format(" | Ранг: [%c] | MP: %d/%d", rank, mana, maxMana) + "}";
    }
}

class DungeonBoss extends DungeonEntity {
    private String bossTitle;
    private boolean enraged;

    public DungeonBoss(String name, int level, int maxHp, int baseAttack,
                       int defense, String bossTitle) {
        super(name, level, maxHp, baseAttack, defense);
        this.bossTitle = (bossTitle == null || bossTitle.trim().isEmpty())
                ? "Невідомий бос" : bossTitle;
        this.enraged = false;
    }

    public String getBossTitle() {
        return bossTitle;
    }

    public boolean isEnraged() {
        return enraged;
    }

    @Override
    public int takeDamage(int rawDamage) {
        int damage = super.takeDamage(rawDamage);

        if (hp > 0 && hp <= maxHp * 0.4 && !enraged) {
            enraged = true;
            System.out.println("🔥 БОС ВПАДАЄ У ФАЗУ ЛЮТІ! Атака зросла на 50%!");
        }

        return damage;
    }

    @Override
    public int performAttack() {
        if (enraged) {
            return (int) (super.performAttack() * 1.5);
        }
        return super.performAttack();
    }

    @Override
    public String toString() {
        return "DungeonBoss{" + super.toString()
                + String.format(" | Титул: '%s', Лють: %s", bossTitle, enraged) + "}";
    }
}

class ShadowMonarch extends Hunter {
    private String[] shadowNames = new String[5];
    private int[] shadowBonuses = new int[5];
    private int shadowCount = 0;

    public ShadowMonarch(String name, int level, int maxHp, int maxMana,
                         int baseAttack, int defense) {
        super(name, level, maxHp, baseAttack, defense, 'S', maxMana);
    }

    public boolean arise(String shadowName, int bonusAtk) {
        if (mana < 30) {
            System.out.println("⚠️ Недостатньо мани для призову тіні! Потрібно 30 MP.");
            return false;
        }

        if (shadowCount >= shadowNames.length) {
            System.out.println("⚠️ Тіньова армія вже заповнена (5/5)!");
            return false;
        }

        if (shadowName == null || shadowName.trim().isEmpty() || bonusAtk < 0) {
            System.out.println("⚠️ Неправильні дані тіні.");
            return false;
        }

        mana -= 30;
        shadowNames[shadowCount] = shadowName;
        shadowBonuses[shadowCount] = bonusAtk;
        shadowCount++;

        System.out.println("🌑 «ПОВСТАНЬ!» Тінь [" + shadowName + " (+" + bonusAtk
                + " ATK)] приєдналася до легіону! (" + shadowCount + "/5)");
        return true;
    }

    public int getTotalShadowBonus() {
        int total = 0;
        for (int i = 0; i < shadowCount; i++) {
            total += shadowBonuses[i];
        }
        return total;
    }

    public int getShadowCount() {
        return shadowCount;
    }

    @Override
    public int performAttack() {
        return super.performAttack() + getTotalShadowBonus();
    }

    public void levelUp() {
        increaseLevel();
        maxHp += 150;
        hp = maxHp;
        maxMana += 50;
        mana = maxMana;
        baseAttack += 25;
        System.out.println("🔔 [СИСТЕМА]: РІВЕНЬ ПІДВИЩЕНО! Новий рівень: "
                + getLevel() + "!");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("ShadowMonarch{").append(super.toString()).append("}\n");
        result.append("  👥 Тіньовий Легіон (").append(shadowCount).append("/5): ");

        if (shadowCount == 0) {
            result.append("поки що порожній");
        } else {
            result.append("[");
            for (int i = 0; i < shadowCount; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(i + 1).append(". ").append(shadowNames[i])
                        .append(" (+").append(shadowBonuses[i]).append(" ATK)");
            }
            result.append("]");
        }

        result.append(" | Сумарний бонус тіней: +")
                .append(getTotalShadowBonus()).append(" ATK");
        return result.toString();
    }
}

public class Task5ShadowMonarchRpg {

    private static void runBattle(ShadowMonarch monarch, DungeonBoss boss) {
        int round = 1;

        System.out.println("\n[БОЙОВА ЗУСТРІЧ]: " + boss.getName()
                + " — " + boss.getBossTitle());

        while (monarch.isAlive() && boss.isAlive()) {
            System.out.println("\n--- РАУНД " + round + " ---");

            int attack;
            if (monarch.getMana() >= 40) {
                attack = monarch.useManaSkill("Темний удар", 40, 1.5);
            } else {
                attack = monarch.performAttack();
                System.out.println("🗡️ " + monarch.getName() + " атакує разом із тінями!");
            }

            int dealt = boss.takeDamage(attack);
            System.out.println("💥 " + boss.getName() + " отримує " + dealt
                    + " шкоди! HP: " + boss.getHp() + "/" + boss.getMaxHp());

            if (!boss.isAlive()) {
                System.out.println("🏆 ПЕРЕМОГА! " + boss.getName() + " подолано!");
                break;
            }

            int received = monarch.takeDamage(boss.performAttack());
            System.out.println("👊 " + monarch.getName() + " отримує " + received
                    + " шкоди. HP: " + monarch.getHp() + "/" + monarch.getMaxHp());

            if (!monarch.isAlive()) {
                System.out.println("❌ Мисливець більше не може продовжувати бій.");
                break;
            }

            round++;
        }
    }

    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("       ⚔️ SOLO LEVELING: ПІДЗЕМЕЛЛЯ S-РАНГУ ⚔️");
        System.out.println("==============================================================");

        ShadowMonarch monarch = new ShadowMonarch(
                "Сон Джін-Ву", 45, 1200, 300, 180, 45
        );

        System.out.println("\n[ДОСЬЄ МИСЛИВЦЯ]:");
        System.out.println(monarch);

        DungeonBoss igris = new DungeonBoss(
                "Лицар Ігріс", 50, 520, 90, 25, "Елітний лицар червоного рангу"
        );

        runBattle(monarch, igris);

        if (!igris.isAlive() && monarch.isAlive()) {
            monarch.arise("Лицар Ігріс", 90);
        }

        DungeonBoss beru = new DungeonBoss(
                "Король Мурах Беру", 80, 1250, 120, 60,
                "Кошмар острова Чеджу"
        );

        System.out.println("\n[БОС ПІДЗЕМЕЛЛЯ]:");
        System.out.println(beru);

        runBattle(monarch, beru);

        if (!beru.isAlive() && monarch.isAlive()) {
            monarch.levelUp();
            monarch.arise("Генерал Беру", 180);
        }

        System.out.println("\n================ ФІНАЛЬНЕ ДОСЬЄ ================");
        System.out.println(monarch);
        System.out.println("=================================================");
    }
}
