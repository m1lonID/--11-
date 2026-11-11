package m1lon;

public final class AncientLeshen extends AbstractMonster
        implements RegeneratingMonster {
    private boolean regenerationBlocked = false;
    private final int forestTotems;

    public AncientLeshen(String name, int hp, int forestTotems) {
        super(name, "Релікти", hp);
        this.forestTotems = forestTotems;
    }

    public int getForestTotems() {
        return forestTotems;
    }

    @Override
    public int regenerateHealth() {
        if (regenerationBlocked || !isAlive()) {
            return 0;
        }
        return restoreHealth(80);
    }

    @Override
    public void applyMoonDustBomb() {
        regenerationBlocked = true;
    }

    @Override
    public int calculateMonsterAttack() {
        return 110;
    }

    @Override
    public String getBattleCry() {
        return "Прадавній Лісовик кличе на допомогу сили лісу!";
    }
}
