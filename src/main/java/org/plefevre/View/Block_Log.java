package org.plefevre.View;

import org.plefevre.Artifact;
import org.plefevre.Game;
import org.plefevre.Model.Hero;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Block_Log extends BlockRPG {
    public Block_Log(int x, int y, int w, int h) {
        super(x, y, w, h);
        useColor = true;
    }


    ArrayList<String> output = new ArrayList<>();
    ArrayList<byte[]> colors = new ArrayList<>();

    @Override
    public char[][] draw() {

        buffer = new char[rh][rw];
        color = new byte[rh][rw];

        drawCadre("Log ");
        if (focus) {
            setBorderColor(0, 0, rw, rh, (byte) 2);
            gestionFocus();
        }

        int nbToPrint = min(output.size(), rh - 2 - 2);

        for (int i = 0; i < nbToPrint; i++) {
            int id = output.size() - nbToPrint + i;
            setTextAt(buffer, output.get(id), 2, 1 + i);
            setColor(color, colors.get(id), 2, 1 + i);
        }

        return buffer;
    }

    private void gestionFocus() {
        if (Game.game.input.isEnter() && !Game.game.input.getText().isEmpty()) interprete(Game.game.input.getText());

        int y = 2;

        y += output.size();
        if (y > rh - 1)
            y = rh - 1;

        Game.game.input.setListen_x(rx + 2);
        Game.game.input.setListen_y(ry + y);
        Game.game.input.setListen_tap(true);


    }

    public void interprete(String text) {
        Game game = Game.game;
        if (text.isEmpty())
            return;

        addTextColor(text, (byte) 3);
        text = text.toLowerCase();

        if (text.equals("exit") || text.equals("quit")) System.exit(0);
        else {
            boolean redraw = true;
            if (text.equals("down") || text.equals("d")) game.moveHero(0, 1);
            else if (text.equals("up") || text.equals("u")) game.moveHero(0, -1);
            else if (text.equals("right") || text.equals("r")) game.moveHero(1, 0);
            else if (text.equals("left") || text.equals("l")) game.moveHero(-1, 0);
            else if (text.equals("help") || text.equals("h")) showHelp();
            else if (text.startsWith("show")) command_show(text);
            else if (text.startsWith("equip")) command_equip(text);
            else if (text.startsWith("unequip")) command_unequip(text);
            else if (text.startsWith("throw")) command_throw(text);
            else if (text.startsWith("use")) command_use(text);
            else if (text.startsWith("dimension")) addSimpleText(game.getRpgInterface().getH() + " * " + game.getRpgInterface().getW());
            else {
                redraw = false;
                addTextColorWord("Command not found, try 'help'", "1 1 1 1 -1");
            }

            if (redraw) {
                Game.game.input.setText("");
                Game.game.setRedraw(true);
            }
        }

        //todo : -Modal (fight, victory, defeat, lvlComplete)
        //
    }

    public void command_equip(String text) {
        Game game = Game.game;

        String[] tt = text.split(" ");
        if (tt.length != 2)
            addTextColorWord("Usage: equip id_inventory", "1 -1 -1");
        else {
            try {
                int id = Integer.parseInt(tt[1]);

                if (id < 0 || id >= Hero.INVENTORY_SIZE)
                    addTextColor("Outside index", (byte) 1);
                else {
                    Artifact artifact = game.getHero().getInventory(id);
                    if (artifact == null || (artifact.getType() != Artifact.TYPE_WEAPON && artifact.getType() != Artifact.TYPE_ARMOR & artifact.getType() != Artifact.TYPE_HELM))
                        addTextColor("No armor/weapon at this index", (byte) 1);
                    else if (artifact.getLvl() > game.getHero().getLvl())
                        addTextColor("Level too hight", (byte) 1);
                    else game.getHero().equip(id);
                }

            } catch (NumberFormatException e) {
                addTextColor("Not a number", (byte) 1);
            }
        }
    }

    public void command_use(String text) {
        Game game = Game.game;

        String[] tt = text.split(" ");
        if (tt.length != 2)
            addTextColorWord("Usage: use id_inventory", "1 -1 -1");
        else {
            try {
                int id = Integer.parseInt(tt[1]);

                if (id < 0 || id >= Hero.INVENTORY_SIZE)
                    addTextColor("Outside index", (byte) 1);
                else {
                    Artifact artifact = game.getHero().getInventory(id);
                    if (artifact == null || (artifact.getType() != Artifact.TYPE_POTION))
                        addTextColor("No potion at this index", (byte) 1);
                    else if (artifact.getLvl() > game.getHero().getLvl())
                        addTextColor("Level too hight", (byte) 1);
                    else game.getHero().use(id);
                }

            } catch (NumberFormatException e) {
                addTextColor("Not a number", (byte) 1);
            }
        }
    }

    public void command_throw(String text) {
        Game game = Game.game;

        String[] tt = text.split(" ");
        if (tt.length != 2)
            addTextColorWord("Usage: throw id_inventory", "1 -1 -1");
        else {
            try {
                int id = Integer.parseInt(tt[1]);

                if (id < 0 || id >= Hero.INVENTORY_SIZE)
                    addTextColor("Outside index", (byte) 1);
                else {
                    Artifact artifact = game.getHero().getInventory(id);
                    if (artifact == null)
                        addTextColor("No item at this index", (byte) 1);
                    else game.getHero().throwE(id);
                }

            } catch (NumberFormatException e) {
                addTextColor("Not a number", (byte) 1);
            }
        }
    }

    public void command_unequip(String text) {
        Game game = Game.game;

        String[] tt = text.split(" ");
        if (tt.length != 2)
            addTextColorWord("Usage: unequip weapon|armor|helm", "1 -1 -1");
        else {
            String type = tt[1].toLowerCase();

            int type_int = 0;
            if (type.equals("weapon")) type_int = Artifact.TYPE_WEAPON;
            if (type.equals("armor")) type_int = Artifact.TYPE_ARMOR;
            if (type.equals("helm")) type_int = Artifact.TYPE_HELM;

            if (type_int == 0)
                addTextColor("Type not recognized", (byte) 1);
            else if (game.getHero().getNbFreeInventory() == 0)
                addTextColor("No space left", (byte) 1);
            else {
                game.getHero().unequip(type_int, -1);
            }


        }
    }

    public void command_show(String text) {
        Game game = Game.game;

        String[] tt = text.split(" ");
        if (tt.length != 2)
            addTextColorWord("Usage: show id_inventory", "1 -1 -1");
        else {
            try {
                int id = Integer.parseInt(tt[1]);

                if (id < 0 || id >= Hero.INVENTORY_SIZE)
                    addTextColor("Outside index", (byte) 1);
                else {
                    Artifact artifact = game.getHero().getInventory(id);
                    if (artifact == null)
                        addTextColor("No artifact at this index", (byte) 1);
                    else {
                        String s = artifact.getStringLog();
                        String[] ss = s.split("\n");
                        for (int i = 0; i < ss.length; i++) {
                            addTextColor(ss[i], (byte) 6);
                        }
                    }
                }

            } catch (NumberFormatException e) {
                addTextColor("Not a number", (byte) 1);
            }
        }
    }

    public void addSimpleText(String txt) {
        addTextColor(txt, (byte) 0);
    }

    public void addTextColor(String txt, byte col) {
        byte[] arr = new byte[txt.length()];

        Arrays.fill(arr, col);

        output.add(txt);
        colors.add(arr);
    }

    public void addTextColorWord(String txt, String col) {
        byte[] arr = new byte[txt.length()];
        Arrays.fill(arr, (byte) 0);

        String[] cols = col.split(" ");
        int id = 0;
        byte curColor;

        curColor = Byte.parseByte(cols[id]);
        for (int i = 0; i < txt.length(); i++) {
            arr[i] = curColor;
            if (txt.charAt(i) == ' ' && i > 0 && txt.charAt(i - 1) != ' ') {
                id++;
                if (id < cols.length)
                    curColor = Byte.parseByte(cols[id]);
            }
        }

        output.add(txt);
        colors.add(arr);

    }

    public void showHelp() {

    }
}
