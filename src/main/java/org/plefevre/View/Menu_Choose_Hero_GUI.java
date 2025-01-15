package org.plefevre.View;

import org.plefevre.Model.Hero;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Menu_Choose_Hero_GUI extends JFrame {

    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel chooseHeroPanel;
    private JPanel createHeroPanel;
    private CardLayout cardLayout;

    private JList<String> heroList;
    private DefaultListModel<String> heroListModel;

    private JTextField heroNameField;
    private JComboBox<String> classComboBox;
    private JSpinner strengthSpinner;
    private JSpinner defenseSpinner;
    private JSpinner hitPointSpinner;
    private JLabel pointsLeftLabel;
    private int pointsLeft = 5;

    JButton chooseHeroButton = new JButton("Choose Hero");
    JButton createHeroButton = new JButton("Create Hero");

    JButton generateButton = new JButton("Create Hero");
    JButton selectButton = new JButton("Select");


    public Menu_Choose_Hero_GUI() {
        super("Hero Menu GUI");

        customizeUI();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        CustomBackgroundPanel backgroundPanel = new CustomBackgroundPanel("./resources/bg_image_title.png");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout) {
            @Override
            public boolean isOpaque() {

                return false;
            }
        };
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        menuPanel = buildMenuPanel();
        chooseHeroPanel = buildChooseHeroPanel();
        createHeroPanel = buildCreateHeroPanel();

        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(chooseHeroPanel, "CHOOSE_HERO");
        mainPanel.add(createHeroPanel, "CREATE_HERO");

        cardLayout.show(mainPanel, "MENU");
        setVisible(true);
    }

    private void customizeUI() {

        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 12));


    }

    private JPanel buildMenuPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setOpaque(false);
        outerPanel.setLayout(new GridBagLayout());

        TransparentPanel innerPanel = new TransparentPanel(new Color(255, 255, 255, 200));
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addHoverEffect(chooseHeroButton);
        addHoverEffect(createHeroButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        innerPanel.add(chooseHeroButton, gbc);

        gbc.gridy = 1;
        innerPanel.add(createHeroButton, gbc);


        outerPanel.add(innerPanel);

        return outerPanel;
    }

    public void addCreateButtonListener(ActionListener listener) {
        createHeroButton.addActionListener(listener);
    }

    public void addChooseButtonListener(ActionListener listener) {
        chooseHeroButton.addActionListener(listener);
    }

    public void addGenerateButtonListener(ActionListener listener) {
        generateButton.addActionListener(listener);
    }

    public void addSelectButtonListener(ActionListener listener) {
        selectButton.addActionListener(listener);
    }

    private JPanel buildChooseHeroPanel() {
        TransparentPanel panel = new TransparentPanel(new Color(255, 255, 255, 200));
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Choose your Hero");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        heroListModel = new DefaultListModel<>();
        heroList = new JList<>(heroListModel);
        heroList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton backButton = new JButton("Back to Menu");


        addHoverEffect(backButton);
        addHoverEffect(selectButton);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));


        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.add(titleLabel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);
        bottomPanel.add(selectButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(heroList), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void updateHeroList(ArrayList<Hero> heroes) {
        heroListModel.clear();

        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            heroListModel.addElement(String.format(
                    "[%d] %s (lvl %d) %s",
                    hero.getId(), hero.getClassName(), hero.getLvl(), hero.getName()
            ));
        }
    }

    private JPanel buildCreateHeroPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setOpaque(false);
        outerPanel.setLayout(new GridBagLayout());


        TransparentPanel innerPanel = new TransparentPanel(new Color(255, 255, 255, 200));
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel nameLabel = new JLabel("Hero Name:");
        heroNameField = new JTextField(15);

        JLabel classLabel = new JLabel("Class:");
        String[] classes = {"Warrior", "Mage", "Archey"};
        classComboBox = new JComboBox<>(classes);

        JLabel strengthLabel = new JLabel("Strength:");
        strengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        JLabel defenseLabel = new JLabel("Defense:");
        defenseSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        JLabel intelLabel = new JLabel("Intelligence:");
        hitPointSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        pointsLeftLabel = new JLabel("Points left: " + pointsLeft);

        JButton cancelButton = new JButton("Cancel");


        addHoverEffect(generateButton);
        addHoverEffect(cancelButton);

        ChangeListener spinnerChangeListener = e -> updatePointsLeft();
        strengthSpinner.addChangeListener(spinnerChangeListener);
        defenseSpinner.addChangeListener(spinnerChangeListener);
        hitPointSpinner.addChangeListener(spinnerChangeListener);

        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        innerPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        innerPanel.add(heroNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        innerPanel.add(classLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        innerPanel.add(classComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        innerPanel.add(strengthLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        innerPanel.add(strengthSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        innerPanel.add(defenseLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        innerPanel.add(defenseSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        innerPanel.add(intelLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        innerPanel.add(hitPointSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        innerPanel.add(pointsLeftLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        innerPanel.add(cancelButton, gbc);
        gbc.gridx = 1;
        innerPanel.add(generateButton, gbc);
        outerPanel.add(innerPanel);

        return outerPanel;
    }

    private void resetCreateForm() {
        heroNameField.setText("");
        classComboBox.setSelectedIndex(0);
        strengthSpinner.setValue(0);
        defenseSpinner.setValue(0);
        hitPointSpinner.setValue(0);
        pointsLeft = 5;
        pointsLeftLabel.setText("Points left: " + pointsLeft);
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

    private void updatePointsLeft() {
        int str = (Integer) strengthSpinner.getValue();
        int def = (Integer) defenseSpinner.getValue();
        int intl = (Integer) hitPointSpinner.getValue();

        int total = str + def + intl;
        pointsLeft = 5 - total;
        pointsLeftLabel.setText("Points left: " + pointsLeft);
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


    private void animateBackground(final JButton button,
                                   final Color startColor,
                                   final Color endColor,
                                   final int durationMs) {
        final long startTime = System.nanoTime();
        final Timer timer = new Timer(15, null);

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float elapsed = (System.nanoTime() - startTime) / 1_000_000f;
                float progress = Math.min(elapsed / durationMs, 1.0f);


                int r = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * progress);
                int g = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * progress);
                int b = (int) (startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * progress);
                int a = (int) (startColor.getAlpha() + (endColor.getAlpha() - startColor.getAlpha()) * progress);

                button.setBackground(new Color(r, g, b, a));

                if (progress >= 1.0f) {
                    timer.stop();
                }
            }
        });

        timer.start();
    }

    public void setView(String view) {
        cardLayout.show(mainPanel, view);
    }

    public String getCreateHeroName() {
        if (heroNameField == null) return null;
        return heroNameField.getText().trim();
    }

    public String getCreateHeroClass() {
        if (classComboBox == null) return null;
        return (String) classComboBox.getSelectedItem();
    }

    public int getCreateHeroAttack() {
        if (strengthSpinner == null) return 0;
        return (Integer) strengthSpinner.getValue();
    }

    public int getCreateHeroDefense() {
        if (defenseSpinner == null) return 0;
        return (Integer) defenseSpinner.getValue();
    }

    public int getCreateHeroHitPoints() {
        if (hitPointSpinner == null) return 0;
        return (Integer) hitPointSpinner.getValue();
    }

    public int getHeroIdSelected() {
        int index = heroList.getSelectedIndex();
        String selectedValue = heroListModel.getElementAt(index);

        if (index < 0) return -1;

        return Integer.parseInt(selectedValue.substring(
                selectedValue.indexOf("[") + 1,
                selectedValue.indexOf("]")
        ));
    }

    public void close() {
        dispose();
    }
}
