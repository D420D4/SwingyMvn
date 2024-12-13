package org.plefevre;

import org.plefevre.View.Menu_Choose_Hero;

public abstract class Menu {
    public static Menu menu;

    public static void init()
    {
        menu = new Menu_Choose_Hero();
    }

    public abstract boolean interaction();
}
