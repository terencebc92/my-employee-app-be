package com.coursera.employeemanagement.service;

import com.coursera.employeemanagement.dto.EmployeeDto;
import com.coursera.employeemanagement.model.Employee;
import com.coursera.employeemanagement.repository.EmployeeRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found, give a valid id");
        }
        return employeeOpt.get();
    }

    public Long createEmployee(EmployeeDto dto) {

        validateEmployeeRequest(dto);


        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        Employee employeeDb = employeeRepository.save(employee);
        log.info("Employee created: {}", employeeDb.getId());
        return employeeDb.getId();
    }

    private static void validateEmployeeRequest(EmployeeDto dto) {
        String firstName = dto.getFirstName();
        String lastName = dto.getLastName();
        String email = dto.getEmail();
        Long salary = dto.getSalary();

        if (StringUtils.isEmpty(firstName)) {
            throw new RuntimeException("First name cannot be empty");
        }

        if (StringUtils.isEmpty(lastName)) {
            throw new RuntimeException("Last name cannot be empty");
        }

        if (StringUtils.isEmpty(email)) {
            throw new RuntimeException("Email cannot be empty");
        }

        if (salary == null || salary == 0) {
            throw new RuntimeException("Salary cannot be 0");
        }

        if (!email.contains("@")) {
            throw new RuntimeException("Email should have an @");
        }

        if (!email.endsWith(".com")) {
            throw new RuntimeException("Only .com domain allowed for email");
        }

        if (firstName.length() > 10) {
            throw new RuntimeException("First name cannot exceed 10 characters: " + firstName + ". Length: " + firstName.length());
        }
        if (lastName.length() > 10) {
            throw new RuntimeException("Last name cannot exceed 10 characters: " + lastName + ". Length: " + lastName.length());
        }
        if (email.length() > 20) {
            throw new RuntimeException("Email cannot exceed 20 characters: " + email + ". Length: " + email.length());
        }
        if (salary < 0) {
            throw new RuntimeException("Salary cannot be negative: " + salary);
        }
    }

    public Long updateEmployee(Long id, EmployeeDto dto) {
        validateEmployeeRequest(dto);

        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found, give a valid id");
        }
        Employee employee = employeeOpt.get();
        BeanUtils.copyProperties(dto, employee, "id");
        Employee employeeDb = employeeRepository.save(employee);
        log.info("Employee updated: {}", employeeDb.getId());
        return employeeDb.getId();
    }

    public void deleteEmployee(Long id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found, give a valid id");
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted: {}", id);
    }
}
