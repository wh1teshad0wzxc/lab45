package com.example.airport.ui;

import com.example.airport.model.Tariff;

import javax.swing.*;
import java.awt.*;

public class TariffDialog extends JDialog {

    private JTextField idField = new JTextField(10);
    private JTextField directionField = new JTextField(20);
    private JSpinner priceField = new JSpinner(new SpinnerNumberModel(0.0, 0, 100000, 1));
    private JTextField cargoTypeField = new JTextField(15);
    private JSpinner maxWeightField = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));

    private Tariff result = null;

    // === КОНСТРУКТОР: ДОБАВЛЕНИЕ ===
    public TariffDialog(Frame owner) {
        super(owner, "Добавить тариф", true);
        buildUI();
    }

    // === КОНСТРУКТОР: РЕДАКТИРОВАНИЕ ===
    public TariffDialog(Frame owner, Tariff t) {
        super(owner, "Редактировать тариф", true);
        buildUI();

        idField.setText(String.valueOf(t.id));
        idField.setEnabled(false);

        directionField.setText(t.direction);
        priceField.setValue(t.price);
        cargoTypeField.setText(t.cargoType);
        maxWeightField.setValue(t.maxWeight);
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));

        form.add(new JLabel("ID:"));
        form.add(idField);

        form.add(new JLabel("Направление:"));
        form.add(directionField);

        form.add(new JLabel("Стоимость:"));
        form.add(priceField);

        form.add(new JLabel("Тип груза:"));
        form.add(cargoTypeField);

        form.add(new JLabel("Макс. вес:"));
        form.add(maxWeightField);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Отмена");

        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        JPanel buttons = new JPanel();
        buttons.add(ok);
        buttons.add(cancel);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    private void onOk() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String direction = directionField.getText().trim();
            double price = ((Number) priceField.getValue()).doubleValue();
            String cargoType = cargoTypeField.getText().trim();
            int maxWeight = ((Number) maxWeightField.getValue()).intValue();

            if (direction.isEmpty() || cargoType.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Заполните все поля");
                return;
            }

            result = new Tariff(id, direction, price, cargoType, maxWeight);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода данных");
        }
    }

    public Tariff getResult() {
        return result;
    }
}
