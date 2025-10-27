package com.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.mongodb.UserProto;
import com.mongodb.model.Employee;
import com.mongodb.repository.EmployeeRepository;
import com.mongodb.service.EmployeeService;
import com.mongodb.vo.EmployeeProjection;
import com.mongodb.vo.User;
//import com.mongodb.UserProto;

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
    
    
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") int id) {
        return new User(id, "Pavan", true);
    }
    
    
    
    @GetMapping(value = "/user/proto/{id}", produces = "application/x-protobuf")
    public String getUserProto(@PathVariable("id") int id) throws InvalidProtocolBufferException {
//        return UserProto.User.newBuilder()
//                .setId(id)
//                .setName("Pavan")
//                .setActive(true)
//                .build();
    	UserProto.User user = UserProto.User.newBuilder()
                .setId(id)
                .setName("Pavan")
                .setActive(true)
                .build();
        return JsonFormat.printer().print(user);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
    @GetMapping("/getByDepartment/{department}")
    public List<EmployeeProjection> getEmployeeByDep(@PathVariable("department") String department) {
        return employeeService.getNameAndEmailbyDepartment(department);
    }
    
    @GetMapping("/getEmpNameByDep/{department}")
    public List<Employee> getEmployeeNameByDep(@PathVariable("department") String department) {
        return employeeRepository.findEmpNameByDepartment(department);
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
			if (updatedEmployee.getSalary() != null) {
				employee.setSalary(updatedEmployee.getSalary());
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
    
    @GetMapping
    public ResponseEntity<Page<EmployeeProjection>> getEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<EmployeeProjection> result = employeeService.getEmployees(department, page, size, sortBy, sortDir);
        return result.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }
}
