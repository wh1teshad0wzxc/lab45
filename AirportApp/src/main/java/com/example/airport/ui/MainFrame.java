package com.example.airport.ui;

import com.example.airport.db.TariffDao;
import com.example.airport.model.Tariff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Направление", "Стоимость", "Тип груза", "Макс. вес"}, 0);
    private JTable table = new JTable(model);

    public MainFrame() {
        setTitle("Аэропорт — тарифы");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(toolbar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private JPanel toolbar() {
        JButton load = new JButton("Загрузить из БД");
        JButton add = new JButton("Добавить");
        JButton del = new JButton("Удалить");

        load.addActionListener(e -> reload());

        add.addActionListener(e -> {
            TariffDialog d = new TariffDialog(this);
            d.setVisible(true);
            Tariff t = d.getResult();
            if (t == null) return;
            try {
                TariffDao.insert(t);
                reload();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) return;
            int id = Integer.parseInt(model.getValueAt(r, 0).toString());
            try {
                TariffDao.delete(id);
                reload();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        JPanel p = new JPanel();
        p.add(load);
        p.add(add);
        p.add(del);
        return p;
    }

    private void reload() {
        try {
            List<Tariff> list = TariffDao.findAll();
            model.setRowCount(0);
            for (Tariff t : list) {
                model.addRow(new Object[]{
                        t.id, t.direction, t.price, t.cargoType, t.maxWeight
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage(),
                "Ошибка БД", JOptionPane.ERROR_MESSAGE);
    }
}
