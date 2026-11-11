package m1lon;

/**
 * Практична робота №10. Завдання 5.
 * Демонстрація абстрактних класів, інтерфейсів, поліморфізму,
 * Pattern Matching instanceof та final-методів на прикладі The Witcher 3.
 */

// ---------- Інтерфейси бойових здібностей (CAN-DO) ----------

interface FlyingMonster {
    boolean isAirborne();
    void knockDownToGround();
}

interface RegeneratingMonster {
    int regenerateHealth();
    void applyMoonDustBomb();
}

interface SpectralPhantom {
    boolean isIncorporeal();
    void bindWithYrdenSign();
}

// ---------- Спільна абстрактна сутність монстра (IS-A) ----------

abstract class AbstractMonster {
    private final String name;
    private final String bestiaryCategory;
    protected int hp;
    protected final int maxHp;

    protected AbstractMonster(String name, String bestiaryCategory, int maxHp) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Monster name cannot be empty");
        }
        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp must be positive");
        }
        this.name = name;
        this.bestiaryCategory = bestiaryCategory;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public final String getName() {
        return name;
    }

    public final String getBestiaryCategory() {
        return bestiaryCategory;
    }

    public final int getHp() {
        return hp;
    }

    public final int getMaxHp() {
        return maxHp;
    }

    public abstract int calculateMonsterAttack();

    public abstract String getBattleCry();

    public final boolean isAlive() {
        return hp > 0;
    }

    /**
     * Звичайна шкода. Конкретні монстри можуть перевизначити метод,
     * щоб врахувати власний захист.
     */
    public void receiveDamage(int dmg) {
        if (dmg <= 0 || !isAlive()) {
            return;
        }
        hp = Math.max(0, hp - dmg);
    }

    protected final int restoreHealth(int amount) {
        if (amount <= 0 || !isAlive()) {
            return 0;
        }
        int oldHp = hp;
        hp = Math.min(maxHp, hp + amount);
        return hp - oldHp;
    }

    protected final String healthBar() {
        int width = 20;
        int filled = (int) Math.round((double) hp / maxHp * width);
        return "[" + "#".repeat(filled) + "-".repeat(width - filled) + "]";
    }

    @Override
    public String toString() {
        return name + " [" + bestiaryCategory + " | HP: " + hp + "/" + maxHp
                + " " + healthBar() + "]";
    }
}

// ---------- Конкретні монстри ----------

class RoyalGriffin extends AbstractMonster implements FlyingMonster {
    private boolean airborne = true;
    private final double altitudeMeters;

    public RoyalGriffin(String name, int hp, double altitudeMeters) {
        super(name, "Гібриди", hp);
        this.altitudeMeters = altitudeMeters;
    }

    public double getAltitudeMeters() {
        return altitudeMeters;
    }

    @Override
    public boolean isAirborne() {
        return airborne;
    }

    @Override
    public void knockDownToGround() {
        airborne = false;
    }

    @Override
    public int calculateMonsterAttack() {
        return 85;
    }

    @Override
    public String getBattleCry() {
        return "Грифон розправляє крила й видає оглушливий крик!";
    }

    @Override
    public void receiveDamage(int dmg) {
        // Поки грифон у повітрі, він отримує лише 50% шкоди.
        super.receiveDamage(airborne ? (int) Math.ceil(dmg * 0.5) : dmg);
    }
}

class Noonwraith extends AbstractMonster implements SpectralPhantom {
    private boolean incorporeal = true;

    public Noonwraith(String name, int hp) {
        super(name, "Примари", hp);
    }

    @Override
    public boolean isIncorporeal() {
        return incorporeal;
    }

    @Override
    public void bindWithYrdenSign() {
        incorporeal = false;
    }

    @Override
    public int calculateMonsterAttack() {
        return 65;
    }

    @Override
    public String getBattleCry() {
        return "Полудниця здіймає примарний виття над полем!";
    }

    @Override
    public void receiveDamage(int dmg) {
        // Без Ірдена привид поглинає 80% шкоди.
        super.receiveDamage(incorporeal ? (int) Math.ceil(dmg * 0.2) : dmg);
    }
}

class HigherVampire extends AbstractMonster
        implements FlyingMonster, RegeneratingMonster {
    private boolean airborne = true;
    private boolean regenerationBlocked = false;
    private final int bloodRegen;

    public HigherVampire(String name, int hp, int bloodRegen) {
        super(name, "Вампіри", hp);
        this.bloodRegen = bloodRegen;
    }

    @Override
    public boolean isAirborne() {
        return airborne;
    }

    @Override
    public void knockDownToGround() {
        airborne = false;
    }

    @Override
    public int regenerateHealth() {
        if (regenerationBlocked || !isAlive()) {
            return 0;
        }
        return restoreHealth(bloodRegen);
    }

    @Override
    public void applyMoonDustBomb() {
        regenerationBlocked = true;
    }

    public boolean isRegenerationBlocked() {
        return regenerationBlocked;
    }

    @Override
    public int calculateMonsterAttack() {
        return 140;
    }

    @Override
    public String getBattleCry() {
        return "Детлафф розчиняється в кривавому тумані!";
    }
}

// ---------- Відьмак-мисливець ----------

class WitcherHunter {
    private final String hunterName;
    private final String schoolName;
    private final int silverSwordAttack;
    private final int signIntensityPercent;
    private final int mutagenBonus = 30;

    public WitcherHunter(String hunterName, String schoolName,
                         int silverSwordAttack, int signIntensityPercent) {
        this.hunterName = hunterName;
        this.schoolName = schoolName;
        this.silverSwordAttack = silverSwordAttack;
        this.signIntensityPercent = signIntensityPercent;
    }

    public void printHunterInfo() {
        System.out.printf(
                "Мисливець: %s (%s | Срібний меч: %d ATK | Інтенсивність Знаків: %d%%)%n",
                hunterName, schoolName, silverSwordAttack, signIntensityPercent
        );
    }

    /**
     * Тактичний аналіз виконується за можливостями інтерфейсів,
     * а не за конкретними класами монстрів.
     */
    public void applyBestiaryTactics(AbstractMonster target) {
        if (target == null || !target.isAlive()) {
            return;
        }

        if (target instanceof SpectralPhantom sp && sp.isIncorporeal()) {
            System.out.println("🟣 [instanceof SpectralPhantom] Ґеральт креслить на землі "
                    + "магічне коло Знаку ІРДЕН! Примару матеріалізовано!");
            sp.bindWithYrdenSign();
        }

        if (target instanceof FlyingMonster fm && fm.isAirborne()) {
            if (target instanceof RoyalGriffin) {
                System.out.println("💨 [instanceof FlyingMonster] Ґеральт складає пальці "
                        + "у Знак ААРД! Грифона збито на землю!");
            } else {
                System.out.println("💨 [instanceof FlyingMonster] Телеїнетичний імпульс "
                        + "Аарду перериває піке вампіра!");
            }
            fm.knockDownToGround();
        }

        if (target instanceof RegeneratingMonster rm) {
            if (target instanceof HigherVampire) {
                System.out.println("💣 [instanceof RegeneratingMonster] Ґеральт підриває "
                        + "бомбу 'Місячний Пил'! Срібні ошурки блокують регенерацію!");
            } else {
                System.out.println("💣 [instanceof RegeneratingMonster] Бомба 'Місячний Пил' "
                        + "блокує відновлення здоров'я Лісовика!");
            }
            rm.applyMoonDustBomb();
        }
    }

    /**
     * Фінальний шаблонний метод: спочатку контрзаходи бестіарію,
     * потім вибір меча/мастила та завдання срібної шкоди.
     */
    public final int executeSilverStrike(AbstractMonster target) {
        if (target == null || !target.isAlive()) {
            return 0;
        }

        applyBestiaryTactics(target);

        int damage = silverSwordAttack + mutagenBonus;
        String attackDescription;

        if (target instanceof RoyalGriffin) {
            // 220 ATK меча + 30 бонусної шкоди від мутагену.
            attackDescription = "Вихор срібного меча";
        } else if (target instanceof SpectralPhantom) {
            damage += 10; // додатковий бонус мастила проти примар
            attackDescription = "Срібний клинок з мастилом проти примар";
        } else if (target instanceof HigherVampire) {
            damage += 80; // критичний бонус школи Вовка
            attackDescription = "Критичний випад школи Вовка";
        } else {
            damage += 30; // мастило проти реліктів
            attackDescription = "Потужний удар срібного меча";
        }

        target.receiveDamage(damage);
        int actualDamage = damage;
        System.out.printf("⚔️ [final executeSilverStrike] %s завдає %d шкоди! "
                        + "(Залишок HP: %d/%d)%n",
                attackDescription, actualDamage, target.getHp(), target.getMaxHp());
        return actualDamage;
    }

    /**
     * Додаткова назва з формулювання теми. Метод також final і використовує
     * той самий незмінний бойовий алгоритм.
     */
    public final int executeWitcherStrike(AbstractMonster target) {
        return executeSilverStrike(target);
    }
}

// ---------- Клас запуску завдання ----------

public class Task5WitcherBestiaryAndMutagenSim {
    public static void main(String[] args) {
        System.out.println("================ 🐺 ВІДЬМАК 3: БЕСТІАРІЙ ТА ПОЛІМОРФНЕ ПОЛЮВАННЯ 🐺 ================");

        WitcherHunter geralt = new WitcherHunter(
                "Ґеральт із Рівії", "Школа Вовка", 220, 150
        );
        geralt.printHunterInfo();

        // Поліморфний масив: усі контракти мають спільний тип AbstractMonster.
        AbstractMonster[] contracts = {
                new RoyalGriffin("Королівський Грифон", 400, 35.0),
                new Noonwraith("Полудниця з Білого Саду", 300),
                new HigherVampire("Вищий Вампір Детлафф", 650, 120),
                new AncientLeshen("Прадавній Лісовик", 720, 80)
        };

        for (int i = 0; i < contracts.length; i++) {
            AbstractMonster monster = contracts[i];

            System.out.printf("%n--- КОНТРАКТ №%d: %s ---%n", i + 1, monster);

            if (monster instanceof RoyalGriffin griffin && griffin.isAirborne()) {
                System.out.println("🦅 Грифон ширяє високо в небі! "
                        + "Удар мечем без підготовки завдав би лише 50% шкоди.");
            } else if (monster instanceof Noonwraith phantom && phantom.isIncorporeal()) {
                System.out.println("👻 Примара нематеріальна (поглинає 80% ударів).");
            } else if (monster instanceof HigherVampire vampire) {
                System.out.println("🦇 Множинний контракт (FlyingMonster + RegeneratingMonster)!");
                vampire.receiveDamage(240); // у повітрі це 120 фактичної шкоди за попередній хід
                int regenerated = vampire.regenerateHealth();
                System.out.println("   [Регенерація до застосування бомби] +" + regenerated + " HP.");
            } else if (monster instanceof AncientLeshen leshen) {
                System.out.println("🌲 Бос-релікт (final class AncientLeshen), тотемів: "
                        + leshen.getForestTotems() + ".");
                leshen.receiveDamage(80); // імітація втрати HP за попередній хід
                System.out.println("   [Регенерація до застосування бомби] +"
                        + leshen.regenerateHealth() + " HP.");
            }

            geralt.executeSilverStrike(monster);

            if (!monster.isAlive()) {
                System.out.println("☠️ Контракт виконано: чудовисько переможене.");
            } else {
                System.out.println("   Стан після сутички: " + monster);
            }
        }

        System.out.println("====================================================================================");
        System.out.println("Бестіарій опрацьовано. Усі контракти пройдено через поліморфний масив AbstractMonster[].");
    }
}
