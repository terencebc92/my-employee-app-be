package com.coursera.employeemanagement.service;

import com.coursera.employeemanagement.dto.EmployeeDto;
import com.coursera.employeemanagement.model.Employee;
import com.coursera.employeemanagement.repository.EmployeeRepository;
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
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        Employee employeeDb = employeeRepository.save(employee);
        log.info("Employee created: {}", employeeDb.getId());
        return employeeDb.getId();
    }

    public Long updateEmployee(Long id, EmployeeDto dto) {
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
