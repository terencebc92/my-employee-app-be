package com.coursera.employeemanagement;

import com.coursera.employeemanagement.model.Employee;
import com.coursera.employeemanagement.repository.EmployeeRepository;
import com.coursera.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;


    @Test
    void testGetEmployees() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
        List<Employee> employeeList = employeeService.getAllEmployees();
        Assertions.assertNotNull(employeeList);
//        Assertions.assertNull(employeeList);
    }
}
