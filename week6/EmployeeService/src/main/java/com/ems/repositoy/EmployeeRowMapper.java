package com.ems.repositoy;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ems.model.Employee;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("department"),
            rs.getString("position"),
            rs.getDouble("salary"),
            rs.getString("email")
        );
    }
}