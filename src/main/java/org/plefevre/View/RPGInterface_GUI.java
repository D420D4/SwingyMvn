package org.plefevre.View;

import org.plefevre.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class RPGInterface_GUI extends JFrame {

    private Hero hero;
    private Map map;


    private MapCanvas mapCanvas;

    private JPanel panelMap;
    private JPanel panelHero;
    private JPanel panelInventory;

    private JList<String> inventoryList;
    private JLabel itemInfoLabel;
    private JButton throwBtn, useBtn, equipBtn;
    private JButton unequipWeaponBtn, unequipArmorBtn, unequipHelmBtn;

    private JLabel labelName;
    private JLabel labelClass;
    private JLabel labelAttack;
    private JLabel labelDefense;
    private JLabel labelHP;

    private JLabel labelWeaponValue;
    private JLabel labelArmorValue;
    private JLabel labelHelmValue;

    private DefaultListModel<String> inventoryModel;

    //FightDialog
    private JDialog currentFightDialog;
    private JLabel monsterLabel;
    private JLabel heroLabel;
    private JTextArea actionLog;
    private JButton attackBtn = new JButton("Attack");
    private JButton blockBtn = new JButton("Block");
    private JButton chargeBtn = new JButton("Charge");

    public RPGInterface_GUI(Hero hero, Map map) {
        super("RPG Swing Interface");
        this.hero = hero;
        this.map = map;

        customizeUI();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(230, 230, 230));
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;


        panelMap = createMapPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panelMap, gbc);

        JPanel rightColumn = new JPanel(new GridBagLayout());
        rightColumn.setSize(new Dimension(400, 0));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(rightColumn, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        panelHero = createHeroPanel();
        rightColumn.add(panelHero, gbc);


        gbc.gridy = 1;
        gbc.weighty = 0.6;
        panelInventory = createInventoryPanel();
        rightColumn.add(panelInventory, gbc);
        setVisible(true);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void setArrowKeyListener(KeyListener kl) {
        panelMap.addKeyListener(kl);
        panelMap.setFocusable(true);
        panelMap.requestFocusInWindow();
    }

    private JPanel createMapPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Map"));
        panel.setBackground(Color.WHITE);
        panel.setFocusable(true);
        panel.requestFocus();

        mapCanvas = new MapCanvas(map, hero);
        panel.add(mapCanvas, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHeroPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Hero Details", 0, 0, new Font("Arial", Font.BOLD, 16), Color.BLACK));
        panel.setBackground(Color.WHITE);

        Dimension fixedSize = new Dimension(300, 300);
        panel.setPreferredSize(fixedSize);
        panel.setMinimumSize(fixedSize);
        panel.setMaximumSize(fixedSize);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 2, 10, 5));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoPanel.add(new JLabel("Name:", JLabel.RIGHT));
        labelName = new JLabel(hero.getName());
        infoPanel.add(labelName);

        infoPanel.add(new JLabel("Class:", JLabel.RIGHT));
        labelClass = new JLabel(hero.getClassName());
        infoPanel.add(labelClass);

        infoPanel.add(new JLabel("Attack:", JLabel.RIGHT));
        labelAttack = new JLabel(String.valueOf(hero.getAttackPoint()));
        infoPanel.add(labelAttack);

        infoPanel.add(new JLabel("Defense:", JLabel.RIGHT));
        labelDefense = new JLabel(String.valueOf(hero.getDefensePoint()));
        infoPanel.add(labelDefense);

        infoPanel.add(new JLabel("Hit Points:", JLabel.RIGHT));
        labelHP = new JLabel(String.valueOf(hero.getHit_point()));
        infoPanel.add(labelHP);

        panel.add(infoPanel, BorderLayout.NORTH);

        JPanel equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new GridLayout(3, 2, 10, 5));
        equipmentPanel.setBackground(Color.WHITE);
        equipmentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Equipment", 0, 0, new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY));

        JLabel labelWeapon = new JLabel("Weapon:");
        labelWeaponValue = new JLabel(hero.getCurrent_weapon() != null ? hero.getCurrent_weapon().getName() : "None");
        equipmentPanel.add(labelWeapon);
        equipmentPanel.add(labelWeaponValue);

        JLabel labelArmor = new JLabel("Armor:");
        labelArmorValue = new JLabel(hero.getCurrent_armor() != null ? hero.getCurrent_armor().getName() : "None");
        equipmentPanel.add(labelArmor);
        equipmentPanel.add(labelArmorValue);

        JLabel labelHelm = new JLabel("Helm:");
        labelHelmValue = new JLabel(hero.getCurrent_helm() != null ? hero.getCurrent_helm().getName() : "None");
        equipmentPanel.add(labelHelm);
        equipmentPanel.add(labelHelmValue);

        panel.add(equipmentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        unequipWeaponBtn = new JButton("Unequip Weapon");
        unequipWeaponBtn.setEnabled(hero.getCurrent_weapon() != null);
        addHoverEffect(unequipWeaponBtn);
        buttonPanel.add(unequipWeaponBtn);

        unequipArmorBtn = new JButton("Unequip Armor");
        unequipArmorBtn.setEnabled(hero.getCurrent_armor() != null);
        addHoverEffect(unequipArmorBtn);
        buttonPanel.add(unequipArmorBtn);

        unequipHelmBtn = new JButton("Unequip Helm");
        unequipHelmBtn.setEnabled(hero.getCurrent_helm() != null);
        addHoverEffect(unequipHelmBtn);
        buttonPanel.add(unequipHelmBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);


        return panel;
    }


    public void setOnUnequipWeaponListener(ActionListener listener) {
        unequipWeaponBtn.addActionListener(listener);
    }

    public void setOnUnequipArmorListener(ActionListener listener) {
        unequipArmorBtn.addActionListener(listener);
    }

    public void setOnUnequipHelmListener(ActionListener listener) {
        unequipHelmBtn.addActionListener(listener);
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        int nb = 0;
        for (int i = 0; i < Hero.INVENTORY_SIZE; i++) {
            if (hero.getInventory(i) != null)
                nb++;
        }
        panel.setBorder(BorderFactory.createTitledBorder("Inventory [" + nb + "/" + Hero.INVENTORY_SIZE + "]"));
        panel.setBackground(Color.WHITE);
        panel.setSize(new Dimension(400, 0));

        inventoryModel = new DefaultListModel<>();
        for (int i = 0; i < Hero.INVENTORY_SIZE; i++) {
            if (hero.getInventory(i) != null) {
                inventoryModel.addElement(hero.getInventory(i).getName());
            } else {
                inventoryModel.addElement("(empty)");
            }
        }

        inventoryList = new JList<>(inventoryModel);
        inventoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        inventoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = inventoryList.getSelectedIndex();
                updateItemDetails(selectedIndex);
            }
        });

        panel.add(new JScrollPane(inventoryList), BorderLayout.CENTER);

        JPanel detailPanel = new JPanel();
        Dimension fixedSize = new Dimension(300, 150);
        detailPanel.setPreferredSize(fixedSize);
        detailPanel.setMinimumSize(fixedSize);
        detailPanel.setMaximumSize(fixedSize);
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        detailPanel.setBackground(Color.WHITE);

        itemInfoLabel = new JLabel("Select an item to see details");
        itemInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(itemInfoLabel);

        detailPanel.add(Box.createVerticalStrut(10));

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));
        buttonBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonBar.setOpaque(false);

        throwBtn = new JButton("Throw");
        useBtn = new JButton("Use");
        equipBtn = new JButton("Equip");

        addHoverEffect(throwBtn);
        addHoverEffect(useBtn);
        addHoverEffect(equipBtn);

        throwBtn.setEnabled(false);
        useBtn.setEnabled(false);
        equipBtn.setEnabled(false);

        buttonBar.add(throwBtn);
        buttonBar.add(Box.createHorizontalStrut(10));
        buttonBar.add(useBtn);
        buttonBar.add(Box.createHorizontalStrut(10));
        buttonBar.add(equipBtn);

        detailPanel.add(buttonBar);

        panel.add(detailPanel, BorderLayout.SOUTH);

        return panel;
    }


    private void updateItemDetails(int index) {
        if (index < 0) {
            itemInfoLabel.setText("Select an item to see details");
            throwBtn.setEnabled(false);
            useBtn.setEnabled(false);
            equipBtn.setEnabled(false);
            return;
        }


        Artifact artifact = hero.getInventory(index);
        if (artifact == null) {
            itemInfoLabel.setText("(empty slot)");
            throwBtn.setEnabled(false);
            useBtn.setEnabled(false);
            equipBtn.setEnabled(false);
        } else {

            ArrayList<Artifact.PassivEffect> passivEffects = artifact.getPassivEffect();

            String effect = "";
            for (Artifact.PassivEffect passivEffect : passivEffects) {
                String val;
                if (passivEffect.getValue() >= 0) val = "+" + passivEffect.getValue();
                else val = passivEffect.getValue() + "";

                if (!effect.isEmpty()) effect += "<br/>";
                effect += val + " " + passivEffect.getName();
            }

            String info = "<html><b>Name:</b> " + artifact.getName() + "<br/>"
                    + "<b>Level:</b> " + artifact.getLvl() + "<br/>"
                    + "<b>Attack:</b> " + artifact.getAttack(hero) + "<br/>"
                    + "<b>Defense:</b> " + artifact.getDefense(hero) + "<br/>"
                    + "<b>Effect:</b> " + effect + "<br/>"
                    + "</html>";
            itemInfoLabel.setText(info);


            throwBtn.setEnabled(true);


            boolean isPotion = (artifact.getType() == Artifact.TYPE_POTION);
            boolean isEquipable = (artifact.getType() == Artifact.TYPE_WEAPON
                    || artifact.getType() == Artifact.TYPE_ARMOR
                    || artifact.getType() == Artifact.TYPE_HELM);

            useBtn.setEnabled(isPotion);
            equipBtn.setEnabled(isEquipable);
        }
    }

    public int getSelectedInventoryIndex() {
        return inventoryList.getSelectedIndex();
    }

    public Artifact getSelectedArtifact() {
        int idx = inventoryList.getSelectedIndex();
        if (idx < 0) return null;
        return hero.getInventory(idx);
    }

    public void setOnThrow(ActionListener al) {
        throwBtn.addActionListener(al);
    }

    public void setOnUse(ActionListener al) {
        useBtn.addActionListener(al);
    }

    public void setOnEquip(ActionListener al) {
        equipBtn.addActionListener(al);
    }

    public void showLvlChooseFightDialog(Monster monster) {
        int choice = JOptionPane.showOptionDialog(
                this,
                "You meet a monster (" + monster.getName() + " lvl " + monster.getLvl() + "). Fight or run?",
                "Fight?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Fight", "Try to run"},
                "Fight"
        );

        boolean fight = false;
        if (choice == JOptionPane.YES_OPTION) {
            fight = true;
        } else {
            if (Math.random() > 0.5) {
                fight = true;
                JOptionPane.showMessageDialog(this, "You failed to run away!");
            } else
                JOptionPane.showMessageDialog(this, "You successfully run away!");
        }

        refreshMap();
        if (fight) {
            showFightDialog(monster);
        }
    }

    public void showVictoryDialog(ArrayList<Artifact> artifacts, int xpWon) {
        JDialog victoryDialog = new JDialog(this, "Victory!", true);
        victoryDialog.setSize(400, 300);
        victoryDialog.setLocationRelativeTo(this);
        victoryDialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(220, 255, 220));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Congratulations! You won " + xpWon + " XP!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(15));

        JLabel artifactLabel = new JLabel("Artifacts loot:");
        artifactLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(artifactLabel);


        for (Artifact art : artifacts) {
            contentPanel.add(new JLabel(" - " + art.getName()));
        }

        JButton continueBtn = new JButton("Continue");
        continueBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addHoverEffect(continueBtn);
        continueBtn.addActionListener(e -> {


            victoryDialog.dispose();
        });
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(continueBtn);

        victoryDialog.add(contentPanel, BorderLayout.CENTER);
        victoryDialog.setVisible(true);
    }

    public int showDefeatDialog() {
        return JOptionPane.showOptionDialog(
                this,
                "You are dead :/ Continue?",
                "Defeat",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                new Object[]{"Restart", "Leave"},
                "Restart"
        );
    }

    public void showFightDialog(Monster monster) {
        currentFightDialog = new JDialog(this, "Fight with " + monster.getName(), true);
        currentFightDialog.setSize(900, 600);
        currentFightDialog.setModal(true);
        currentFightDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        currentFightDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(currentFightDialog,
                        "Nice try! But you can't run away from a fight!");
            }
        });

        currentFightDialog.setLocationRelativeTo(this);
        currentFightDialog.setLayout(new BorderLayout());

        JPanel fightPanel = new JPanel(new BorderLayout());
        fightPanel.setBackground(new Color(245, 245, 245));
        currentFightDialog.add(fightPanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(new Color(230, 230, 230));
        fightPanel.add(infoPanel, BorderLayout.NORTH);

        JPanel monsterPanel = new JPanel();
        monsterPanel.setBackground(new Color(250, 220, 220));
        monsterPanel.setLayout(new BoxLayout(monsterPanel, BoxLayout.Y_AXIS));
        monsterPanel.setBorder(BorderFactory.createTitledBorder("Monster"));
        monsterLabel = new JLabel("<html><h2>" + monster.getName() + "</h2>" +
                "Level: " + monster.getLvl() + "<br/>" +
                "HP: " + monster.getPv() + "/" + monster.getMaxPV() + "</html>");
        monsterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        monsterPanel.add(monsterLabel);
        infoPanel.add(monsterPanel);

        JPanel heroPanel = new JPanel();
        heroPanel.setBackground(new Color(220, 250, 220));
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(BorderFactory.createTitledBorder("Hero"));
        heroLabel = new JLabel("<html><h2>" + hero.getName() + "</h2>" +
                "Level: " + hero.getLvl() + "<br/>" +
                "HP: " + hero.getPv() + "/" + hero.getMaxPV() + "<br/>" +
                "Mana: " + hero.getMana() + "/" + hero.getMaxMana() + "</html>");
        heroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        heroPanel.add(heroLabel);
        infoPanel.add(heroPanel);

        actionLog = new JTextArea(10, 50);
        actionLog.setEditable(false);
        actionLog.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane logScrollPane = new JScrollPane(actionLog);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Battle Log"));
        fightPanel.add(logScrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        actionPanel.setBackground(new Color(240, 240, 240));
        fightPanel.add(actionPanel, BorderLayout.SOUTH);

        addHoverEffect(attackBtn);
        actionPanel.add(attackBtn);

        addHoverEffect(blockBtn);
        actionPanel.add(blockBtn);

        addHoverEffect(chargeBtn);
        actionPanel.add(chargeBtn);

        currentFightDialog.setVisible(true);
    }

    public void setOnAttack(ActionListener al) {
        if (attackBtn != null) {
            attackBtn.addActionListener(al);
        }
    }

    public void setOnDefense(ActionListener al) {
        if (blockBtn != null) {
            blockBtn.addActionListener(al);
        }
    }

    public void setOnCharge(ActionListener al) {
        if (chargeBtn != null) {
            chargeBtn.addActionListener(al);
        }
    }

    public void updateMonsterInfo(Monster monster) {
        if (monsterLabel != null) {
            monsterLabel.setText("<html><h2>" + monster.getName() + "</h2>" +
                    "Level: " + monster.getLvl() + "<br/>" +
                    "HP: " + monster.getPv() + "/" + monster.getMaxPV() + "<br/>" +
                    "Mana: " + monster.getMana() + "/" + monster.getMaxMana() + "</html>");
        }
    }

    public void updateHeroInfo(Hero hero) {
        if (heroLabel != null) {
            heroLabel.setText("<html><h2>" + hero.getName() + "</h2>" +
                    "Level: " + hero.getLvl() + "<br/>" +
                    "HP: " + hero.getPv() + "/" + hero.getMaxPV() + "<br/>" +
                    "Mana: " + hero.getMana() + "/" + hero.getMaxMana() + "</html>");
        }
    }

    public void updateLog(Log log) {
        if (actionLog != null) {
            actionLog.setText("");
            ArrayList<String> output = log.getOutput();
            for (int i = 0; i < output.size(); i++) actionLog.append(output.get(i) + "\n");
            actionLog.setCaretPosition(actionLog.getDocument().getLength());
        }
    }

    public void closeFightDialog() {
        if (currentFightDialog != null) {
            currentFightDialog.dispose();

            refreshMap();
            refreshInventory();
            refreshHero();
        }
    }

    public int showLvlUpDialog() {
        String[] options = {"Attack", "Defense", "Hit Point"};
        return JOptionPane.showOptionDialog(
                this,
                "Congratulation! Choose how to distribute your point! (" + hero.getPoint_to_distribute() + ")",
                "Level Up",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    public int showLvlCompleteDialog() {
        int choice = JOptionPane.showOptionDialog(
                this,
                "Congratulation! Next level?",
                "Level complete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Continue", "Leave"},
                "Continue"
        );
        return choice;
    }


    public void refreshMap() {
        mapCanvas.repaint();
    }

    public void refreshHero() {
        labelName.setText("Name: " + hero.getName());
        labelClass.setText("Class: " + hero.getClassName());
        labelAttack.setText("Attack: " + hero.getAttackPoint());
        labelDefense.setText("Defense: " + hero.getDefensePoint());
        labelHP.setText("Hit Points: " + hero.getHit_point());

        labelWeaponValue.setText(hero.getCurrent_weapon() != null ? hero.getCurrent_weapon().getName() : "None");
        labelArmorValue.setText(hero.getCurrent_armor() != null ? hero.getCurrent_armor().getName() : "None");
        labelHelmValue.setText(hero.getCurrent_helm() != null ? hero.getCurrent_helm().getName() : "None");

        unequipWeaponBtn.setEnabled(hero.getCurrent_weapon() != null);
        unequipArmorBtn.setEnabled(hero.getCurrent_armor() != null);
        unequipHelmBtn.setEnabled(hero.getCurrent_helm() != null);

        panelHero.revalidate();
        panelHero.repaint();
    }

    public void refreshInventory() {
        inventoryModel.clear();
        for (int i = 0; i < Hero.INVENTORY_SIZE; i++) {
            if (hero.getInventory(i) != null) {
                inventoryModel.addElement(hero.getInventory(i).getName());
            } else {
                inventoryModel.addElement("(empty)");
            }
        }
        inventoryList.revalidate();
        inventoryList.repaint();
    }


    private void customizeUI() {
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 12));
    }

    private void addHoverEffect(final JButton button) {
        final Color defaultBackground = new Color(200, 200, 200, 180);
        final Color hoverBackground = new Color(100, 150, 255, 200);

        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(defaultBackground);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateBackground(button, button.getBackground(), hoverBackground, 200);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateBackground(button, button.getBackground(), defaultBackground, 200);
            }
        });
    }

    private void animateBackground(final JComponent component, final Color start, final Color end, final int duration) {
        final int frames = 20;
        final int frameTime = duration / frames;
        final float rStep = (end.getRed() - start.getRed()) / (float) frames;
        final float gStep = (end.getGreen() - start.getGreen()) / (float) frames;
        final float bStep = (end.getBlue() - start.getBlue()) / (float) frames;
        final float aStep = (end.getAlpha() - start.getAlpha()) / (float) frames;

        new Thread(() -> {
            for (int i = 0; i < frames; i++) {
                final int r = (int) (start.getRed() + rStep * i);
                final int g = (int) (start.getGreen() + gStep * i);
                final int b = (int) (start.getBlue() + bStep * i);
                final int a = (int) (start.getAlpha() + aStep * i);
                SwingUtilities.invokeLater(() -> {
                    component.setBackground(new Color(r, g, b, a));
                    component.repaint();
                });
                try {
                    Thread.sleep(frameTime);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }


    private static class MapCanvas extends JPanel {
        private Map map;
        private Hero hero;


        private static final int TILE_SIZE = 40;

        public MapCanvas(Map map, Hero hero) {
            this.map = map;
            this.hero = hero;


            int preferredWidth = map.getSize() * TILE_SIZE;
            int preferredHeight = map.getSize() * TILE_SIZE;
            setPreferredSize(new Dimension(preferredWidth, preferredHeight));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (map == null) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int mapWidth = map.getSize() * TILE_SIZE;
            int mapHeight = map.getSize() * TILE_SIZE;

            int canvasWidth = getWidth();
            int canvasHeight = getHeight();

            int offsetX = 0;
            int offsetY = 0;

            if (mapWidth <= canvasWidth && mapHeight <= canvasHeight) {
                offsetX = (canvasWidth - mapWidth) / 2;
                offsetY = (canvasHeight - mapHeight) / 2;
            } else {
                int heroPx = hero.getX() * TILE_SIZE;
                int heroPy = hero.getY() * TILE_SIZE;

                offsetX = canvasWidth / 2 - (heroPx + TILE_SIZE / 2);
                offsetY = canvasHeight / 2 - (heroPy + TILE_SIZE / 2);

                if (offsetX > 0) {
                    offsetX = 0;
                }
                if (offsetX < canvasWidth - mapWidth) {
                    offsetX = canvasWidth - mapWidth;
                }
                if (offsetY > 0) {
                    offsetY = 0;
                }
                if (offsetY < canvasHeight - mapHeight) {
                    offsetY = canvasHeight - mapHeight;
                }
            }

            g2.translate(offsetX, offsetY);

            for (int y = 0; y < map.getSize(); y++) {
                for (int x = 0; x < map.getSize(); x++) {
                    drawTile(g2, x, y, map.tiles[y][x]);
                }
            }

            drawHero(g2, hero);

            g2.dispose();
        }

        private void drawTile(Graphics2D g2, int x, int y, Map.Tile tile) {
            int px = x * TILE_SIZE;
            int py = y * TILE_SIZE;

            if (tile.isMountain()) {
                Stroke stroke = g2.getStroke();
                g2.setStroke(new BasicStroke(2f));

                Polygon mountainShape = new Polygon();
                mountainShape.addPoint(px + TILE_SIZE / 2, py + 5);
                mountainShape.addPoint(px + 5, py + TILE_SIZE - 5);
                mountainShape.addPoint(px + TILE_SIZE - 5, py + TILE_SIZE - 5);

                g2.setPaint(new Color(199, 127, 68));
                g2.fillRect(px, py, TILE_SIZE, TILE_SIZE);

                g2.setPaint(new Color(140, 80, 30));
                g2.fill(mountainShape);

                g2.setColor(Color.BLACK);
                g2.draw(mountainShape);

                g2.setStroke(stroke);

            } else if (tile.isWater()) {

                Stroke stroke = g2.getStroke();
                g2.setStroke(new BasicStroke(2f));
                GradientPaint gp = new GradientPaint(
                        px, py, new Color(30, 120, 200),
                        px, py + TILE_SIZE, new Color(30, 180, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(px, py, TILE_SIZE, TILE_SIZE);


                g2.setColor(new Color(255, 255, 255, 80));
                for (int wave = 0; wave < 3; wave++) {
                    int waveY = py + 10 + wave * 8;
                    g2.drawArc(px + 5, waveY, TILE_SIZE - 10, 10, 0, 180);
                }

                g2.setStroke(stroke);

            } else {

                GradientPaint gp = new GradientPaint(
                        px, py, new Color(70, 200, 70),
                        px + TILE_SIZE, py + TILE_SIZE, new Color(50, 150, 50)
                );
                g2.setPaint(gp);
                g2.fillRect(px, py, TILE_SIZE, TILE_SIZE);


                g2.setColor(new Color(30, 100, 30));
                Random random = new Random(x * 100L + y);

                for (int i = 0; i < 2; i++) {
                    int gx = px + 5 + (int) (random.nextFloat() * (TILE_SIZE - 10));
                    int gy = py + 5 + (int) (random.nextFloat() * (TILE_SIZE - 10));
                    g2.drawLine(gx, gy, gx, gy - 5);
                    g2.drawLine(gx + 2, gy, gx + 2, gy - 4);
                }
            }
            if (tile.getMonster() != null)
                drawMonster(g2, tile.getMonster(), x, y);
        }

        private void drawHero(Graphics2D g2, Hero hero) {
            int heroX = hero.getX() * TILE_SIZE;
            int heroY = hero.getY() * TILE_SIZE;


            Ellipse2D circle = new Ellipse2D.Double(heroX + 4, heroY + 4, TILE_SIZE - 8, TILE_SIZE - 8);


            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(circle);


            g2.setColor(Color.RED);
            g2.fill(circle);


            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            String label = "H";
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getAscent();
            int tx = (int) (circle.getCenterX() - textWidth / 2.0);
            int ty = (int) (circle.getCenterY() + textHeight / 3.0);
            g2.drawString(label, tx, ty);

        }

        private void drawMonster(Graphics2D g2, Monster monster, int x, int y) {

            int mx = x * TILE_SIZE;
            int my = y * TILE_SIZE;

            Polygon diamond = new Polygon();
            diamond.addPoint(mx + TILE_SIZE / 2, my + 4);
            diamond.addPoint(mx + TILE_SIZE - 4, my + TILE_SIZE / 2);
            diamond.addPoint(mx + TILE_SIZE / 2, my + TILE_SIZE - 4);
            diamond.addPoint(mx + 4, my + TILE_SIZE / 2);

            g2.setColor(new Color(150, 0, 180));
            g2.fill(diamond);

            Stroke stroke = g2.getStroke();
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(Color.BLACK);
            g2.draw(diamond);


            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            String label = "M";
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getAscent();
            int tx = mx + (TILE_SIZE / 2) - textWidth / 2;
            int ty = my + (TILE_SIZE / 2) + textHeight / 3;
            g2.drawString(label, tx, ty);

            g2.setStroke(stroke);
        }
    }

    public void close() {
        dispose();
    }
}
