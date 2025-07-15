package com.ems.repositoy;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ems.model.Employee;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {

	private final JdbcTemplate jdbcTemplate;

	public List<Employee> findAll() {
		return jdbcTemplate.query("SELECT * FROM employees", new EmployeeRowMapper());
	}

	public Optional<Employee> findById(int id) {
		String sql = "SELECT * FROM employees WHERE id = ?";
		List<Employee> employees = jdbcTemplate.query(sql, new EmployeeRowMapper(), id);
		return employees.stream().findFirst();
	}

	public int save(Employee employee) {
	    String sql = "INSERT INTO employees(name, department, position, salary, email) VALUES (?, ?, ?, ?, ?)";
	    return jdbcTemplate.update(sql,
	            employee.getName(),
	            employee.getDepartment(),
	            employee.getPosition(),
	            employee.getSalary(),
	            employee.getEmail());
	}


	public int update(Employee employee) {
	    String sql = "UPDATE employees SET name = ?, department = ?, position = ?, salary = ?, email = ? WHERE id = ?";
	    return jdbcTemplate.update(sql,
	            employee.getName(),
	            employee.getDepartment(),
	            employee.getPosition(),
	            employee.getSalary(),
	            employee.getEmail(),
	            employee.getId());
	}

	public int deleteById(int id) {
		return jdbcTemplate.update("DELETE FROM employees WHERE id = ?", id);
	}
}