package org.bayat.transactionmanualy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
        @Autowired
        private DataSource dataSource;

        public void createUser(User user) throws SQLException {
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            Connection conn = null;
            try {
                conn = dataSource.getConnection();
                conn.setAutoCommit(false); // Start transaction
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getEmail());
                    pstmt.executeUpdate();
                    conn.commit(); // Commit transaction
                } catch (SQLException e) {
                    conn.rollback(); // Rollback on error
                    throw e;
                }
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            }
        }

        public User getUserById(int id) throws SQLException {
            String sql = "SELECT * FROM users WHERE id = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
                }
            }
            return null;
        }

        public List<User> getAllUsers() throws SQLException {
            List<User> users = new ArrayList<>();
            String sql = "SELECT * FROM users";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
                }
            }
            return users;
        }

        public void updateUser(User user) throws SQLException {
            String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
            Connection conn = null;
            try {
                conn = dataSource.getConnection();
                conn.setAutoCommit(false); // Start transaction
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getEmail());
                    pstmt.setInt(3, user.getId());
                    pstmt.executeUpdate();
                    conn.commit(); // Commit transaction
                } catch (SQLException e) {
                    conn.rollback(); // Rollback on error
                    throw e;
                }
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            }
        }

        public void deleteUser(int id) throws SQLException {
            String sql = "DELETE FROM users WHERE id = ?";
            Connection conn = null;
            try {
                conn = dataSource.getConnection();
                conn.setAutoCommit(false); // Start transaction
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    conn.commit(); // Commit transaction
                } catch (SQLException e) {
                    conn.rollback(); // Rollback on error
                    throw e;
                }
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            }
        }
    }

