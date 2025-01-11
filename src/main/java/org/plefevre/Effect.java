package org.plefevre;

public class Effect {
    int duration;
    int attack;
    int defense;

    public Effect(int duration, int attack, int defense) {
        this.duration = duration;
        this.attack = attack;
        this.defense = defense;
    }

    public int getDuration() {
        return duration;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public void decreaseDuration()
    {
        duration--;
    }
}
