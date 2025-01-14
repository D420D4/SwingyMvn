package org.plefevre.Controller;

import org.plefevre.Model.Effect;
import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;

import java.util.ArrayList;

public class HeroController {
    private Hero hero;

    public void setCurrentHero(Hero hero) {
        this.hero = hero;
    }

    public void setInitValue() {
        hero.setMana(hero.getMaxMana());
        hero.setPv(hero.getMaxPV());

    }

    public boolean moveHero(int dx, int dy, Map map) {
        Map.Tile tile = map.getTile(hero.getX() + dx, hero.getY() + dy);

        boolean noMove = false;
        if (tile == null) {
            return true;

        } else {
            if (tile.isWater())
                noMove = true;
        }

        if (!noMove) {
            hero.setX(hero.getX() + dx);
            hero.setY(hero.getY() + dy);
        }


        hero.regenerate();

        updateEffect();

        return false;
    }

    private void updateEffect() {
        ArrayList<Effect> effects = hero.getEffects();

        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).decreaseDuration();
            if (effects.get(i).getDuration() <= 0)
                effects.remove(i);
        }
    }

    public void addXP(Map map) {
        hero.addXp(map.getSize() * map.getSize() * 4);
    }
}
