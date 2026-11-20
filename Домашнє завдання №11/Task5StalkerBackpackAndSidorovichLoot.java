package m1lon;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

class Artifact {
    private final String name;
    private final int costKarbovanets;
    private final double radiationLevel;

    public Artifact(String name, int costKarbovanets, double radiationLevel) {
        this.name = name;
        this.costKarbovanets = costKarbovanets;
        this.radiationLevel = radiationLevel;
    }

    public String getName() {
        return name;
    }

    public int getCostKarbovanets() {
        return costKarbovanets;
    }

    public double getRadiationLevel() {
        return radiationLevel;
    }

    public boolean isRadioactiveDud(double maxSafeRadiation) {
        return radiationLevel > maxSafeRadiation || costKarbovanets <= 0;
    }

    @Override
    public String toString() {
        return String.format("[Артефакт: '%s' | Ціна: %,6d крб | Радіація: %5.1f мкЗв/год]",
                name, costKarbovanets, radiationLevel);
    }
}

public class Task5StalkerBackpackAndSidorovichLoot {
    private static final double MAX_SAFE_RADIATION = 50.0;
    private static final int LEAD_CONTAINER_MIN_COST = 15_000;

    public static void demonstrateFailFastOnBackpack(List<Artifact> backpack) {
        // Працюємо з копією, щоб демонстрація помилки не змінила справжній рюкзак.
        List<Artifact> testBackpack = new ArrayList<>(backpack);
        try {
            for (Artifact artifact : testBackpack) {
                if (artifact.isRadioactiveDud(MAX_SAFE_RADIATION)) {
                    testBackpack.remove(artifact); // Навмисна помилка для демонстрації.
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ⚡ [Перевірка КПК]: Спроба прямого backpack.remove() у циклі for-each перехоплена -> ConcurrentModificationException!");
        }
    }

    public static int purgeRadioactiveDudsWithIterator(List<Artifact> backpack, double maxRadiation) {
        Iterator<Artifact> iterator = backpack.iterator();
        int removedDuds = 0;

        while (iterator.hasNext()) {
            Artifact current = iterator.next();
            if (current.isRadioactiveDud(maxRadiation)) {
                System.out.printf("  ☢️ [Iterator.remove] Викинуто небезпечну пустушку: '%s' (Радіація: %.1f мкЗв/год)%n",
                        current.getName(), current.getRadiationLevel());
                iterator.remove();
                removedDuds++;
            }
        }
        return removedDuds;
    }

    public static void distributeAndRankForSidorovich(
            List<Artifact> cleanLoot,
            List<Artifact> leadContainer,
            List<Artifact> standardPouch) {

        // Сортування за спаданням ціни через базові операції get() та set().
        for (int i = 0; i < cleanLoot.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < cleanLoot.size(); j++) {
                if (cleanLoot.get(j).getCostKarbovanets()
                        > cleanLoot.get(maxIndex).getCostKarbovanets()) {
                    maxIndex = j;
                }
            }
            if (maxIndex != i) {
                Artifact temp = cleanLoot.get(i);
                cleanLoot.set(i, cleanLoot.get(maxIndex));
                cleanLoot.set(maxIndex, temp);
            }
        }

        // Після ранжування розкладаємо артефакти по контейнерах у тому самому порядку.
        for (int i = 0; i < cleanLoot.size(); i++) {
            Artifact artifact = cleanLoot.get(i);
            if (artifact.getCostKarbovanets() >= LEAD_CONTAINER_MIN_COST) {
                leadContainer.add(artifact);
            } else {
                standardPouch.add(artifact);
            }
        }
    }

    public static void findTopArtifactAndPrintReceipt(List<Artifact> cleanLoot) {
        int maxCost = Integer.MIN_VALUE;
        int totalCost = 0;
        Artifact topArtifact = null;

        System.out.println("\n  📋 Фінальний чек та ранжований список хабару у Сидоровича:");
        for (int i = 0; i < cleanLoot.size(); i++) {
            Artifact artifact = cleanLoot.get(i);
            totalCost += artifact.getCostKarbovanets();

            if (artifact.getCostKarbovanets() > maxCost) {
                maxCost = artifact.getCostKarbovanets();
                topArtifact = artifact;
            }

            String medal;
            if (i == 0) medal = "🥇";
            else if (i == 1) medal = "🥈";
            else if (i == 2) medal = "🥉";
            else medal = "   ";

            System.out.printf("    %s #%d. '%-20s' — %,6d крб (Фон: %.1f мкЗв/год)%n",
                    medal, i + 1, artifact.getName(), artifact.getCostKarbovanets(),
                    artifact.getRadiationLevel());
        }

        System.out.println("---------------------------------------------------------------------------------------------------");
        if (topArtifact != null) {
            System.out.printf("  👑 Найдорожчий артефакт рейду: '%s' (%,d крб)%n",
                    topArtifact.getName(), maxCost);
        }
        System.out.printf("  💰 ПІДСУМКОВА ВАРТІСТЬ ЧИСТОГО ХАБАРУ: %,d крб%n", totalCost);
        System.out.printf("  💬 Сидорович: «Непоганий улов із Дикого Сходу, Скіфе! Добре, що радіоактивні пустушки відсіяв — тримай свої %,d карбованців!»%n",
                totalCost);
    }

    private static double calculateTotalRadiation(List<Artifact> artifacts) {
        double total = 0.0;
        for (int i = 0; i < artifacts.size(); i++) {
            total += artifacts.get(i).getRadiationLevel();
        }
        return total;
    }

    private static void printContainer(String title, List<Artifact> container) {
        System.out.printf("  %s (%d шт.): [", title, container.size());
        for (int i = 0; i < container.size(); i++) {
            Artifact artifact = container.get(i);
            System.out.printf("'%s' (%,d крб)", artifact.getName(), artifact.getCostKarbovanets());
            if (i < container.size() - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        var backpack = new ArrayList<Artifact>();
        backpack.add(new Artifact("Бенгальський вогонь", 9_500, 14.5));
        backpack.add(new Artifact("Фоняча Пустушка", 0, 92.0));
        backpack.add(new Artifact("Крівник", 18_500, 26.0));
        backpack.add(new Artifact("Аномальний Шлак", 1_200, 135.4));
        backpack.add(new Artifact("Виверт", 11_000, 18.0));
        backpack.add(new Artifact("Іржава Пустушка", 500, 78.5));
        backpack.add(new Artifact("Компас", 45_000, 32.0));

        System.out.println("================ ☢️ S.T.A.L.K.E.R. 2: РЕЙД ДИКИМ СХОДОМ ЗОНИ ТА ЛАВКА СИДОРОВИЧА ☢️ ================");
        System.out.printf("Сталкер: Скіф | Маршрут: Дикий Схід Зони -> Бункер Сидоровича | Допустима норма радіації: %.1f мкЗв/год%n",
                MAX_SAFE_RADIATION);

        System.out.printf("\n[1. ВМІСТ РЮКЗАКА ПІСЛЯ ВИЛАЗКИ (ДО ЧИСТКИ — %d об'єктів у ArrayList)]:%n", backpack.size());
        for (int i = 0; i < backpack.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, backpack.get(i));
        }
        double initialRadiation = calculateTotalRadiation(backpack);
        System.out.printf("---------------------------------------------------------------------------------------------------%nВсього знахідок у рюкзаку: %d шт. | Сумарний фон рюкзака: %.1f мкЗв/год (⚠️ НЕБЕЗПЕЧНИЙ РІВЕНЬ!)%n",
                backpack.size(), initialRadiation);

        System.out.println("\n[2. ДЕЗАКТИВАЦІЯ ТА ВІДСІЮВАННЯ ПУСТУШОК ЧЕРЕЗ ITERATOR]:");
        demonstrateFailFastOnBackpack(backpack);
        System.out.println("  -> Запускаємо безпечне очищення рюкзака через Iterator<Artifact> та iterator.remove()...");
        int removed = purgeRadioactiveDudsWithIterator(backpack, MAX_SAFE_RADIATION);
        System.out.printf("  ✅ Очищення завершено! Вилучено радіоактивних пустушок: %d шт.%n", removed);
        System.out.printf("  Залишилося чистих артефактів у рюкзаку: %d шт. | Сумарний фон: %.1f мкЗв/год%n",
                backpack.size(), calculateTotalRadiation(backpack));

        ArrayList<Artifact> leadSpecialContainer = new ArrayList<>();
        ArrayList<Artifact> standardBeltContainer = new ArrayList<>();
        distributeAndRankForSidorovich(backpack, leadSpecialContainer, standardBeltContainer);

        System.out.println("\n[3. КОНТЕЙНЕРИ АРТЕФАКТІВ ТА РАНЖУВАННЯ ЗНАХІДОК ДЛЯ СИДОРОВИЧА]:");
        printContainer("🛡️ Свинцевий спецконтейнер (>= 15 000 крб)", leadSpecialContainer);
        printContainer("🎒 Стандартний контейнер   (< 15 000 крб)", standardBeltContainer);
        findTopArtifactAndPrintReceipt(backpack);
        System.out.println("===================================================================================================");
    }
}
