package com.mongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.model.Employee;
import com.mongodb.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	public List<Employee> getNameAndEmailbyDepartment(String department){
		return employeeRepository.findByDepartmentLight(department);
	}
}
