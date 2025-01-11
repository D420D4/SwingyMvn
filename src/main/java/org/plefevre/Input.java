package org.plefevre;

import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Input {

    String text = "";
    boolean enter = false;
    boolean listen_tap = false; //Quand false on ecout juste une touche
    boolean reload = false;
    boolean moveCursor = false;
    int listen_x = 0;
    int listen_y = 0;

    int touch = -1;

    boolean is_tab;
    int count = 0;

    public void listen() {

        is_tab = false;
        enter = false;
        touch = -1;
        count++;
/*
        if(reload)
        {
            reload = false;
            return;
        }
*/
        try (Terminal terminal = TerminalBuilder.terminal()) {
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            if (!listen_tap) {
                terminal.enterRawMode();
                int c = terminal.reader().read();  // Read a single character from the terminal
                switch (c) {
                    case 27: // ESC sequence, typically used for arrow keys
                        if (terminal.reader().read() == '[') {  // Expecting a sequence starting with ESC + [
                            int arrowKey = terminal.reader().read();
                            switch (arrowKey) {
                                case 'A':
                                    touch = 1;
                                    break;
                                case 'B':
                                    touch = 2;
                                    break;
                                case 'C':
                                    touch = 3;
                                    break;
                                case 'D':
                                    touch = 4;
                                    break;
                            }
                        }
                        break;
                    case '\t': // Tab key
                        is_tab = true;
                        break;
                    case '\r': // Enter key
                        touch = 5;
                        break;
                    default:
//                        text += (char) c;
                        break;
                }


            } else {
                reader.getWidgets().put("interruptOnTab", new Widget() {
                    @Override
                    public boolean apply() {
                        System.out.println("Touche Tab détectée, interrompre la lecture !");
                        String partialLine = reader.getBuffer().toString();
                        is_tab = true;
                        throw new UserInterruptException(partialLine);
                    }
                });

                KeyMap<Binding> keyMap = reader.getKeyMaps().get(LineReader.MAIN);
                keyMap.bind(reader.getWidgets().get("interruptOnTab"), "\t"); // Associer la touche "Tab"

                String ignoredPrefix = "\001"; // Equivalent de LineReader.PROMPT_IGNORED_PREFIX
                String ignoredSuffix = "\002"; // Equivalent de LineReader.PROMPT_IGNORED_SUFFIX

                // Séquence ANSI pour déplacer le curseur
//            String moveCursor = "\033[" + 50 + ";" + 5 + "H";

                // Construire le prompt avec la séquence de déplacement du curseur
                String prompt = "$> ";

                if (moveCursor) {
                    String moveCursor = "\033[" + listen_y + ";" + listen_x + "H";  // 30 = ligne, 10 = colonne
                    terminal.writer().print(moveCursor);
                }
                terminal.flush();

                text = reader.readLine(prompt);
                enter = true;
            }
        } catch (UserInterruptException e) {
            text = e.getPartialLine();
        } catch (IOException e) {
            System.err.println("Erreur d'entrée/sortie : " + e.getMessage());
        }

    }

    public void reload() {
        reload = true;
        touch = 0;
    }

    public int getListen_x() {
        return listen_x;
    }

    public int getListen_y() {
        return listen_y;
    }

    public int getTouch() {
        return touch;
    }

    public boolean isIs_tab() {
        return is_tab;
    }

    public boolean isListen_tap() {
        return listen_tap;
    }

    public void setListen_tap(boolean listen_tap) {
        this.listen_tap = listen_tap;
    }

    public void setListen_x(int listen_x) {
        this.listen_x = listen_x;
    }

    public void setListen_y(int listen_y) {
        this.listen_y = listen_y;
    }

    public boolean isEnter() {
        return enter;
    }

    public String getText() {
        return text;
    }

    public void setText(String s) {
        text = s;
    }

    public void setMoveCursor(boolean moveCursor) {
        this.moveCursor = moveCursor;
    }

    @Override
    public String toString() {
        return "Input{" +
                "text='" + text + '\'' +
                ", enter=" + enter +
                ", listen_tap=" + listen_tap +
                ", reload=" + reload +
                ", moveCursor=" + moveCursor +
                ", listen_x=" + listen_x +
                ", listen_y=" + listen_y +
                ", touch=" + touch +
                ", is_tab=" + is_tab +
                ", count=" + count +
                '}';
    }
}
