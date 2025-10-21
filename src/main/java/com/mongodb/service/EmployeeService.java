package com.mongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.repository.EmployeeRepository;
import com.mongodb.vo.EmployeeProjection;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	public List<EmployeeProjection> getNameAndEmailbyDepartment(String department){
		return employeeRepository.findByDepartmentLight(department);
	}
}
