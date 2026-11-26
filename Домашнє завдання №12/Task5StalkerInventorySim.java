package m1lon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Task5StalkerInventorySim {
    public static void main(String[] args) {
        Stalker skif = new Stalker("Скіф");

        System.out.println("S.T.A.L.K.E.R. 2 - Спорядження сталкера");
        System.out.println("Сталкер: " + skif.getCallSign());

        Artifact meduza = new Artifact("ART-1", "Медуза", 0.4, -2.0, 15);
        Artifact soul = new Artifact("ART-2", "Душа", 0.5, 3.5, 25);
        Artifact meduza2 = new Artifact("ART-1", "Медуза", 0.4, -2.0, 15);
        Artifact compass = new Artifact("ART-3", "Компас", 0.6, 2.5, 40);

        System.out.println("\nПояс артефактів:");
        System.out.println("Медуза: " + skif.equipArtifact(meduza));
        System.out.println("Душа: " + skif.equipArtifact(soul));
        System.out.println("Повторна Медуза: " + skif.equipArtifact(meduza2));
        System.out.println("Компас: " + skif.equipArtifact(compass));

        Medkit medkit1 = new Medkit("MED-1", "Звичайна аптечка", 0.3, 1);
        Medkit medkit2 = new Medkit("MED-2", "Армійська аптечка", 0.4, 2);
        Detector detector1 = new Detector("DET-1", "Детектор Відгук", 0.5, 0);
        Detector detector2 = new Detector("DET-2", "Детектор Велес", 0.7, 85);

        skif.addToBackpack(medkit1);
        skif.addToBackpack(medkit2);
        skif.addToBackpack(detector1);
        skif.addToBackpack(detector2);

        medkit1.use();

        System.out.println("\nРюкзак:");
        skif.cleanBackpack();

        System.out.println("\nСтан сталкера:");
        System.out.println("Кількість артефактів: " + skif.getArtifactBelt().size());
        System.out.println("Захист: " + skif.getTotalProtection() + "%");
        System.out.printf("Радіація: %+.2f мЗв/хв%n", skif.getNetRadiation());

        System.out.print("Предмети в рюкзаку: ");
        for (ZoneItem item : skif.getBackpack()) {
            System.out.print(item.getName() + " ");
        }
        System.out.println();

        System.out.printf("Загальна вага: %.2f кг%n", skif.getTotalWeight());

        HashSet<String> skifLocations = new HashSet<>(Arrays.asList(
                "Залісся", "Кордон", "Звалище", "Янтар"));
        HashSet<String> allyLocations = new HashSet<>(Arrays.asList(
                "Залісся", "Янтар", "Росток", "Прип'ять"));

        skif.setVisitedLocations(skifLocations);

        System.out.println("\nЛокації Скіфа: " + skif.getVisitedLocations());
        System.out.println("Локації союзника: " + allyLocations);
        System.out.println("Спільні локації: " + skif.getCommonLocations(allyLocations));
        System.out.println("Нові локації: " + skif.getNewLocations(allyLocations));
    }
}

abstract class ZoneItem {
    private String id;
    private String name;
    private double weightKg;

    public ZoneItem(String id, String name, double weightKg) {
        if (id == null || id.isEmpty() || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("ID і назва не можуть бути порожніми");
        }
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Вага повинна бути більшою за 0");
        }
        this.id = id;
        this.name = name;
        this.weightKg = weightKg;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public abstract String getItemType();

    public abstract boolean isUsable();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ZoneItem)) {
            return false;
        }
        ZoneItem item = (ZoneItem) obj;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

class Artifact extends ZoneItem {
    private double radiation;
    private int protection;

    public Artifact(String id, String name, double weightKg, double radiation, int protection) {
        super(id, name, weightKg);
        this.radiation = radiation;
        this.protection = protection;
    }

    public double getRadiation() {
        return radiation;
    }

    public int getProtection() {
        return protection;
    }

    @Override
    public String getItemType() {
        return "Артефакт";
    }

    @Override
    public boolean isUsable() {
        return true;
    }
}

class Medkit extends ZoneItem {
    private int usesLeft;

    public Medkit(String id, String name, double weightKg, int usesLeft) {
        super(id, name, weightKg);
        if (usesLeft < 0) {
            throw new IllegalArgumentException("Кількість зарядів не може бути від'ємною");
        }
        this.usesLeft = usesLeft;
    }

    public void use() {
        if (usesLeft > 0) {
            usesLeft--;
        }
    }

    public int getUsesLeft() {
        return usesLeft;
    }

    @Override
    public String getItemType() {
        return "Аптечка";
    }

    @Override
    public boolean isUsable() {
        return usesLeft > 0;
    }
}

class Detector extends ZoneItem {
    private int batteryPercent;

    public Detector(String id, String name, double weightKg, int batteryPercent) {
        super(id, name, weightKg);
        if (batteryPercent < 0 || batteryPercent > 100) {
            throw new IllegalArgumentException("Заряд має бути від 0 до 100");
        }
        this.batteryPercent = batteryPercent;
    }

    public int getBatteryPercent() {
        return batteryPercent;
    }

    @Override
    public String getItemType() {
        return "Детектор";
    }

    @Override
    public boolean isUsable() {
        return batteryPercent > 0;
    }
}

class Stalker {
    private String callSign;
    private HashSet<Artifact> artifactBelt;
    private ArrayList<ZoneItem> backpack;
    private HashSet<String> visitedLocations;

    public Stalker(String callSign) {
        this.callSign = callSign;
        artifactBelt = new HashSet<>();
        backpack = new ArrayList<>();
        visitedLocations = new HashSet<>();
    }

    public String getCallSign() {
        return callSign;
    }

    public boolean equipArtifact(Artifact artifact) {
        if (artifactBelt.size() >= 4) {
            return false;
        }
        return artifactBelt.add(artifact);
    }

    public void addToBackpack(ZoneItem item) {
        backpack.add(item);
    }

    public int cleanBackpack() {
        int removed = 0;
        Iterator<ZoneItem> iterator = backpack.iterator();

        while (iterator.hasNext()) {
            ZoneItem item = iterator.next();

            if (!item.isUsable()) {
                System.out.println("Видалено: " + item.getName());
                iterator.remove();
                removed++;
            }
        }
        return removed;
    }

    public double getTotalWeight() {
        double weight = 0;

        for (Artifact artifact : artifactBelt) {
            weight += artifact.getWeightKg();
        }

        for (ZoneItem item : backpack) {
            weight += item.getWeightKg();
        }

        return weight;
    }

    public double getNetRadiation() {
        double radiation = 0;

        for (Artifact artifact : artifactBelt) {
            radiation += artifact.getRadiation();
        }

        return radiation;
    }

    public int getTotalProtection() {
        int protection = 0;

        for (Artifact artifact : artifactBelt) {
            protection += artifact.getProtection();
        }

        return protection;
    }

    public HashSet<Artifact> getArtifactBelt() {
        return new HashSet<>(artifactBelt);
    }

    public ArrayList<ZoneItem> getBackpack() {
        return new ArrayList<>(backpack);
    }

    public void setVisitedLocations(HashSet<String> locations) {
        visitedLocations = new HashSet<>(locations);
    }

    public HashSet<String> getVisitedLocations() {
        return new HashSet<>(visitedLocations);
    }

    public HashSet<String> getCommonLocations(HashSet<String> ally) {
        HashSet<String> result = new HashSet<>(visitedLocations);
        result.retainAll(ally);
        return result;
    }

    public HashSet<String> getNewLocations(HashSet<String> ally) {
        HashSet<String> result = new HashSet<>(ally);
        result.removeAll(visitedLocations);
        return result;
    }
}
