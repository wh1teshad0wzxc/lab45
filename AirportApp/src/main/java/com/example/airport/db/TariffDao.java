package com.example.airport.db;

import com.example.airport.model.Tariff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class
TariffDao {

    public static List<Tariff> findAll() throws Exception {
        List<Tariff> list = new ArrayList<>();
        try (Connection c = Db.connect();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id, direction, price, cargo_type, max_weight FROM tariffs ORDER BY id");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Tariff(
                        rs.getInt("id"),
                        rs.getString("direction"),
                        rs.getDouble("price"),
                        rs.getString("cargo_type"),
                        rs.getInt("max_weight")
                ));
            }
        }
        return list;
    }
    public static void update(Tariff t) throws Exception {
        try (Connection c = Db.connect();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE tariffs SET direction=?, price=?, cargo_type=?, max_weight=? WHERE id=?")) {

            ps.setString(1, t.direction);
            ps.setDouble(2, t.price);
            ps.setString(3, t.cargoType);
            ps.setInt(4, t.maxWeight);
            ps.setInt(5, t.id);

            ps.executeUpdate();
        }
    }

    public static void insert(Tariff t) throws Exception {
        try (Connection c = Db.connect();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO tariffs VALUES (?, ?, ?, ?, ?)")) {

            ps.setInt(1, t.id);
            ps.setString(2, t.direction);
            ps.setDouble(3, t.price);
            ps.setString(4, t.cargoType);
            ps.setInt(5, t.maxWeight);
            ps.executeUpdate();
        }
    }



    public static void delete(int id) throws Exception {
        try (Connection c = Db.connect();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM tariffs WHERE id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
