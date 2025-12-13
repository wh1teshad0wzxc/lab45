package com.example.airport.ui;

import com.example.airport.model.Tariff;

import javax.swing.*;
import java.awt.*;

public class TariffDialog extends JDialog {

    private JTextField id = new JTextField();
    private JTextField direction = new JTextField();
    private JSpinner price =
            new JSpinner(new SpinnerNumberModel(100.0, 1.0, 1_000_000.0, 10.0));
    private JTextField cargoType = new JTextField();
    private JSpinner maxWeight =
            new JSpinner(new SpinnerNumberModel(1, 1, 10_000, 1));

    private Tariff result;

    public TariffDialog(Frame owner) {
        super(owner, "Добавить тариф", true);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.add(new JLabel("ID")); form.add(id);
        form.add(new JLabel("Направление")); form.add(direction);
        form.add(new JLabel("Стоимость")); form.add(price);
        form.add(new JLabel("Тип груза")); form.add(cargoType);
        form.add(new JLabel("Макс. вес (кг)")); form.add(maxWeight);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Отмена");

        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(ok);
        buttons.add(cancel);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private void onOk() {
        try {
            int i = Integer.parseInt(id.getText().trim());
            if (i <= 0) throw new Exception();

            String d = direction.getText().trim();
            String c = cargoType.getText().trim();
            double p = (double) price.getValue();
            int w = (int) maxWeight.getValue();

            if (d.isEmpty() || c.isEmpty())
                throw new Exception();

            result = new Tariff(i, d, p, c, w);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Некорректные данные");
        }
    }

    public Tariff getResult() {
        return result;
    }
}
