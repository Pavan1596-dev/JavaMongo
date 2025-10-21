package com.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.model.Employee;
import com.mongodb.vo.EmployeeProjection;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
	
	/*✅ Projection
Return only required fields — faster + secure*/
	@Query(value = "{}", fields = "{ 'name' : 1, 'email' : 1 }")
	List<Employee> findAllNameAndEmailOnly();
	
    @Query(value = "{ 'department': ?0 }", fields = "{ 'name': 1, 'email': 1, '_id': 0 }")
    List<EmployeeProjection> findByDepartmentLight(String department);
    
    @Query(value = "{ 'department': ?0 }", fields = "{ 'name': 1, '_id': 0 }")
    List<Employee> findEmpNameByDepartment(String department);
}
