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
        JButton loadFromFile = new JButton("Загрузить из файла");
        JButton load = new JButton("Загрузить из БД");
        JButton add = new JButton("Добавить");
        JButton edit = new JButton("Редактировать");
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

        loadFromFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res != JFileChooser.APPROVE_OPTION) return;

            java.io.File file = chooser.getSelectedFile();

            try {
                loadTariffsFromFile(file);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Ошибка чтения файла",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Выберите строку");
                return;
            }

            Tariff t = new Tariff(
                    Integer.parseInt(model.getValueAt(row, 0).toString()),
                    model.getValueAt(row, 1).toString(),
                    Double.parseDouble(model.getValueAt(row, 2).toString()),
                    model.getValueAt(row, 3).toString(),
                    Integer.parseInt(model.getValueAt(row, 4).toString())
            );

            TariffDialog dialog = new TariffDialog(this, t);
            dialog.setVisible(true);

            Tariff updated = dialog.getResult();
            if (updated == null) return;

            try {
                TariffDao.update(updated);
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
        p.add(loadFromFile);
        p.add(edit);
        p.add(del);
        return p;
    }

    private void loadTariffsFromFile(java.io.File file) throws Exception {
        model.setRowCount(0); // очищаем таблицу

        try (java.io.BufferedReader br =
                     new java.io.BufferedReader(new java.io.FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(";");
                if (p.length != 5) continue;

                int id = Integer.parseInt(p[0].trim());
                String direction = p[1].trim();
                double price = Double.parseDouble(p[2].trim());
                String cargoType = p[3].trim();
                int maxWeight = Integer.parseInt(p[4].trim());

                model.addRow(new Object[]{
                        id,
                        direction,
                        price,
                        cargoType,
                        maxWeight
                });
            }
        }
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
