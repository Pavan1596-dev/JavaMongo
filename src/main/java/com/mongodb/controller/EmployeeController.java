package com.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.model.Employee;
import com.mongodb.repository.EmployeeRepository;
import com.mongodb.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/getByAll")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
    @GetMapping("/getByDepartment/{department}")
    public List<Employee> getEmployeeByDep(@PathVariable("department") String department) {
        return employeeService.getNameAndEmailbyDepartment(department);
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
    	  try {
              Employee savedEmployee = employeeRepository.save(employee);
              return ResponseEntity.ok(savedEmployee); // success response
          } catch (DuplicateKeyException e) {
              return ResponseEntity.status(HttpStatus.CONFLICT)
                      .body("Employee with name '" + employee.getName() + "' already exists.");
          }
    }

    @PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") String id,
			@RequestBody Employee updatedEmployee) {
		return employeeRepository.findById(id).map(employee -> {
			if (StringUtils.hasText(updatedEmployee.getName())) {
				employee.setName(updatedEmployee.getName());
			}
			if (StringUtils.hasText(updatedEmployee.getEmail())) {
				employee.setEmail(updatedEmployee.getEmail());
			}
			if (StringUtils.hasText(updatedEmployee.getDepartment())) {
				employee.setDepartment(updatedEmployee.getDepartment());
			}
			return ResponseEntity.ok(employeeRepository.save(employee));
		}).orElse(ResponseEntity.notFound().build());
	}

    //Delete API
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") String id) {
        return employeeRepository.findById(id).map(employee -> {
            employeeRepository.delete(employee);
            return ResponseEntity.ok("Employee deleted successfully");
        }).orElse(ResponseEntity.status(404).body("Employee not found"));
    }
}
