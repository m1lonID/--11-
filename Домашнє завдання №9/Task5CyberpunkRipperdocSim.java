package m1lon;

import java.util.Locale;

/**
 * Завдання 5. Cyberpunk 2077: клініка Ріппердока.
 * Один файл містить клас запуску та всі допоміжні класи (package-private).
 */
public class Task5CyberpunkRipperdocSim {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        System.out.println("================ 🦾 CYBERPUNK 2077: КЛІНІКА РІППЕРДОКА 🦾 ================");

        CyberMercenary david = new CyberMercenary("Девід Мартінес", 120, 1000);
        System.out.println("Пацієнт: " + david.getNickname() + " | " + david.getBioMonitor());

        System.out.println("\n[ХІРУРГІЧНЕ ВСТАНОВЛЕННЯ ІМПЛАНТІВ]:");
        david.installImplant(new SandevistanOs());
        david.installImplant(new MantisBlades());

        // Перевірка захисту від дублювання слота "Нейро-ОС".
        david.installImplant(new SandevistanOs());

        david.installImplant(new SubdermalTitaniumArmor());

        // Додатковий імплант зменшує людяність нижче 20, щоб
        // кіберпсихоз гарантовано активувався за правилом humanity < 20.
        david.installImplant(new KiroshiOptics());

        System.out.println("\n[ДОСЬЄ НАЙМАНЦЯ ПЕРЕД БОЄМ]:");
        System.out.println(david);

        AdamSmasher smasher = new AdamSmasher("Адам Смешер", 1800, 145, 75);
        System.out.println("\n--- ⚔️ БІЙ НА ВЕРШИНІ ARASAKA TOWER ПРОТИ АДАМА СМЕШЕРА ⚔️ ---");

        for (int round = 1; round <= 4 && smasher.isAlive() && david.isAlive(); round++) {
            System.out.println("\n========== РАУНД " + round + " ==========");

            if (round == 1) {
                david.activateImplantAbility("Нейро-ОС");
                david.activateImplantAbility("Руки");
            } else if (round == 3) {
                david.activateImplantAbility("Шкіра");
            }

            int dealt = david.attack(smasher);
            System.out.println("💥 " + david.getNickname() + " завдає " + dealt
                    + " шкоди! " + smasher.getName() + " втрачає HP. Залишок HP ворога: "
                    + smasher.getHp() + "/" + smasher.getMaxHp());

            if (!smasher.isAlive()) {
                System.out.println("🏆 Адам Смешер знешкоджений!");
                break;
            }

            int received = smasher.attack(david);
            System.out.println("☠️ " + smasher.getName() + " контратакує: "
                    + received + " шкоди. " + david.getNickname() + ": "
                    + david.getBioMonitor().getHp() + " HP.");

            david.finishTurn();
            if (!david.isAlive()) {
                System.out.println("💀 Нейронне перевантаження або атака ворога знищили найманця.");
                break;
            }

            // Імуноблокатор вводиться після демонстрації кіберпсихозу
            // та першого ходу, щоб зняти небезпечний стан під час бою.
            if (round == 2 && david.getBioMonitor().isCyberpsychosis()) {
                david.getBioMonitor().injectImmunoblockers(30);
                System.out.println("💉 Введено Імуноблокатор (+30 Людяності -> "
                        + david.getBioMonitor().getHumanity()
                        + "/100). Нейро-стабільність відновлено!");
            }
        }

        System.out.println("\n[ФІНАЛЬНИЙ СТАН]:");
        System.out.println(david.getBioMonitor());
        System.out.println(smasher);
        System.out.println("============================================================================");
    }
}

/** Інкапсульований монітор здоров'я та нейро-стабільності. */
class BioMonitor {
    private int hp;
    private final int maxHp;
    private int humanity;
    private boolean cyberpsychosis;

    public BioMonitor(int maxHp) {
        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp має бути > 0");
        }
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.humanity = 100;
        this.cyberpsychosis = false;
    }

    public void reduceHumanity(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Вартість людяності не може бути від'ємною");
        }
        humanity = Math.max(0, humanity - cost);
        if (humanity < 20) {
            cyberpsychosis = true;
            System.out.println("⚠️ [КРИТИЧНЕ ПОПЕРЕДЖЕННЯ BIO-MONITOR]: Людяність впала до "
                    + humanity + "/100! СТАН: КІБЕРПСИХОЗ (атака x1.5, саморуйнування)!");
        }
    }

    public void injectImmunoblockers(int restoreAmount) {
        if (restoreAmount <= 0) {
            throw new IllegalArgumentException("Доза має бути > 0");
        }
        humanity = Math.min(100, humanity + restoreAmount);
        if (humanity >= 20) {
            cyberpsychosis = false;
        }
    }

    public int applyDamage(int rawDamage, int totalArmor) {
        int actualDamage = Math.max(5, rawDamage - Math.max(0, totalArmor));
        hp = Math.max(0, hp - actualDamage);
        return actualDamage;
    }

    public void restoreHealth(int amount) {
        if (amount > 0) {
            hp = Math.min(maxHp, hp + amount);
        }
    }

    public boolean isCyberpsychosis() {
        return cyberpsychosis;
    }

    public int getHumanity() {
        return humanity;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public String toString() {
        int filled = (int) Math.round(10.0 * hp / maxHp);
        String hpBar = "█".repeat(filled) + "░".repeat(10 - filled);
        int humanFilled = humanity / 10;
        String humanityBar = "█".repeat(humanFilled) + "░".repeat(10 - humanFilled);
        return "BioMonitor: HP [" + hpBar + "] " + hp + "/" + maxHp
                + " | Humanity [" + humanityBar + "] " + humanity + "/100 "
                + (cyberpsychosis ? "[🔥 CYBERPSYCHOSIS]" : "[Стабільний]");
    }
}

/** Базовий абстрактний клас усіх кіберімплантів. */
abstract class AbstractCyberImplant {
    private final String name;
    private final String slot;
    private final int humanityCost;

    protected AbstractCyberImplant(String name, String slot, int humanityCost) {
        if (name == null || name.isBlank() || slot == null || slot.isBlank()
                || humanityCost < 0) {
            throw new IllegalArgumentException("Некоректні параметри імпланта");
        }
        this.name = name;
        this.slot = slot;
        this.humanityCost = humanityCost;
    }

    public String getName() {
        return name;
    }

    public String getSlot() {
        return slot;
    }

    public int getHumanityCost() {
        return humanityCost;
    }

    public abstract int getAttackBonus();

    public abstract int getArmorBonus();

    public abstract String activateSpecialAbility();

    @Override
    public String toString() {
        return "[" + slot + "] " + name + " (+" + getAttackBonus()
                + " ATK, +" + getArmorBonus() + " DEF, -" + humanityCost + " Humanity)";
    }
}

class SandevistanOs extends AbstractCyberImplant {
    public SandevistanOs() {
        super("Militech Sandevistan Apogee", "Нейро-ОС", 35);
    }

    @Override
    public int getAttackBonus() {
        return 90;
    }

    @Override
    public int getArmorBonus() {
        return 20;
    }

    @Override
    public String activateSpecialAbility() {
        return "⚡ САНДЕВІСТАН АКТИВОВАНО: Час сповільнено на 85%! Вороги завмерли на місці!";
    }
}

class MantisBlades extends AbstractCyberImplant {
    public MantisBlades() {
        super("Термальні Клинки Богомола", "Руки", 25);
    }

    @Override
    public int getAttackBonus() {
        return 110;
    }

    @Override
    public int getArmorBonus() {
        return 5;
    }

    @Override
    public String activateSpecialAbility() {
        return "🔥 Вихор розпечених клинків Богомола розтинає броню ворога!";
    }
}

class SubdermalTitaniumArmor extends AbstractCyberImplant {
    public SubdermalTitaniumArmor() {
        // За умовою: імплант забирає 20 очок людяності.
        super("Підшкірна Титанова Броня", "Шкіра", 20);
    }

    @Override
    public int getAttackBonus() {
        return 10;
    }

    @Override
    public int getArmorBonus() {
        return 65;
    }

    @Override
    public String activateSpecialAbility() {
        return "🛡️ Нано-пластини поглинають кінетичний удар!";
    }
}

/** Додатковий імплант оптики: потрібен для демонстрації порога humanity < 20. */
class KiroshiOptics extends AbstractCyberImplant {
    public KiroshiOptics() {
        super("Kiroshi Optics Mk.3", "Оптика", 5);
    }

    @Override
    public int getAttackBonus() {
        return 0;
    }

    @Override
    public int getArmorBonus() {
        return 0;
    }

    @Override
    public String activateSpecialAbility() {
        return "👁️ Kiroshi: ціль захоплена, слабкі місця Адама Смешера підсвічені!";
    }
}

/** Найманець — головний композитний об'єкт. */
class CyberMercenary {
    private final String nickname;
    private final int baseAttack;
    private final BioMonitor bioMonitor;
    private final AbstractCyberImplant[] implants;
    private int implantCount;

    public CyberMercenary(String nickname, int baseAttack, int maxHp) {
        if (nickname == null || nickname.isBlank() || baseAttack < 0) {
            throw new IllegalArgumentException("Некоректні параметри найманця");
        }
        this.nickname = nickname;
        this.baseAttack = baseAttack;
        this.bioMonitor = new BioMonitor(maxHp);
        this.implants = new AbstractCyberImplant[4];
        this.implantCount = 0;
    }

    public boolean installImplant(AbstractCyberImplant implant) {
        if (implant == null) {
            System.out.println("❌ Неможливо встановити null-імплант.");
            return false;
        }
        if (implantCount >= implants.length) {
            System.out.println("❌ Усі кібернетичні слоти зайняті!");
            return false;
        }
        for (int i = 0; i < implantCount; i++) {
            if (implants[i].getSlot().equalsIgnoreCase(implant.getSlot())) {
                System.out.println("❌ Помилка слота: Слот [" + implant.getSlot()
                        + "] вже зайнятий імплантом '" + implants[i].getName() + "'!");
                return false;
            }
        }

        implants[implantCount++] = implant;
        bioMonitor.reduceHumanity(implant.getHumanityCost());
        System.out.println("🔧 Встановлено [" + implant.getSlot() + "]: "
                + implant.getName() + " (-" + implant.getHumanityCost()
                + " Людяності -> залишок: " + bioMonitor.getHumanity() + ")");
        return true;
    }

    public int getTotalAttack() {
        int total = baseAttack;
        for (int i = 0; i < implantCount; i++) {
            total += implants[i].getAttackBonus();
        }
        return bioMonitor.isCyberpsychosis() ? (int) Math.round(total * 1.5) : total;
    }

    public int getTotalArmor() {
        int total = 0;
        for (int i = 0; i < implantCount; i++) {
            total += implants[i].getArmorBonus();
        }
        return total;
    }

    public void activateImplantAbility(String slot) {
        for (int i = 0; i < implantCount; i++) {
            if (implants[i].getSlot().equalsIgnoreCase(slot)) {
                System.out.println(implants[i].activateSpecialAbility());
                return;
            }
        }
        System.out.println("⚠️ Імплант у слоті " + slot + " не знайдено.");
    }

    public int attack(AdamSmasher target) {
        if (target == null || !target.isAlive()) {
            return 0;
        }
        return target.takeDamage(getTotalAttack());
    }

    public void finishTurn() {
        if (bioMonitor.isCyberpsychosis() && bioMonitor.isAlive()) {
            int overloadDamage = Math.max(1, (int) Math.ceil(bioMonitor.getHp() * 0.10));
            bioMonitor.applyDamage(overloadDamage, 0);
            System.out.println("🧠 Кіберпсихоз: нервове перевантаження завдало "
                    + overloadDamage + " шкоди (10% поточного HP)!");
        }
    }

    public boolean isAlive() {
        return bioMonitor.isAlive();
    }

    public String getNickname() {
        return nickname;
    }

    public BioMonitor getBioMonitor() {
        return bioMonitor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int implantBonus = 0;
        for (int i = 0; i < implantCount; i++) {
            implantBonus += implants[i].getAttackBonus();
        }
        sb.append("CyberMercenary{nick='").append(nickname)
                .append("', TotalATK=").append(getTotalAttack())
                .append(" (База: ").append(baseAttack)
                .append(" + Імпланти: ").append(implantBonus)
                .append((bioMonitor.isCyberpsychosis() ? ", лють x1.5" : ""))
                .append("), TotalDEF=").append(getTotalArmor()).append("}\n");
        sb.append("  ├─ ").append(bioMonitor).append("\n");
        sb.append("  └─ Встановлений хром (").append(implantCount)
                .append("/").append(implants.length).append("):\n");
        for (int i = 0; i < implantCount; i++) {
            sb.append("       ").append(i + 1).append(". ")
                    .append(implants[i]).append("\n");
        }
        return sb.toString();
    }
}

/** Спрощена модель боса Адама Смешера. */
class AdamSmasher {
    private final String name;
    private final int maxHp;
    private int hp;
    private final int attack;
    private final int armor;

    public AdamSmasher(String name, int hp, int attack, int armor) {
        if (name == null || name.isBlank() || hp <= 0 || attack < 0 || armor < 0) {
            throw new IllegalArgumentException("Некоректні параметри Адама Смешера");
        }
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.attack = attack;
        this.armor = armor;
    }

    public int takeDamage(int rawDamage) {
        int actualDamage = Math.max(5, rawDamage - armor);
        hp = Math.max(0, hp - actualDamage);
        return actualDamage;
    }

    public int attack(CyberMercenary target) {
        return target.getBioMonitor().applyDamage(attack, target.getTotalArmor());
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public String toString() {
        return name + " [HP: " + hp + "/" + maxHp + ", ATK: " + attack
                + ", броня: " + armor + "]";
    }
}
