package com.mongodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	 // Fetch employees with optional department filter, pagination & sorting
    public Page<EmployeeProjection> getEmployees(String department, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (department == null || department.isEmpty()) {
            return employeeRepository.findAllBy(pageable);
        } else {
            return employeeRepository.findByDepartment(department, pageable);
        }
    }
}
