package org.aptech.t2311e.service;

import org.aptech.t2311e.entity.Department;
import org.aptech.t2311e.entity.Employee;
import org.aptech.t2311e.repository.DepartmentRepository;
import org.aptech.t2311e.repository.EmployeeRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public Employee createEmployee(Employee employee, Long departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Phòng ban không tồn tại"));
        employee.setDepartment(dept);
        dept.setEmployeeCount(dept.getEmployeeCount() + 1);
        departmentRepository.save(dept);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee newData) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        emp.setName(newData.getName());
        emp.setDob(newData.getDob());
        emp.setAddress(newData.getAddress());
        emp.setJoinDate(newData.getJoinDate());
        return employeeRepository.save(emp);
    }

    public Page<Employee> searchEmployees(String name, String address, Long deptId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("joinDate").ascending());

        return employeeRepository.findAll((root, query, cb) -> {
            var predicates = cb.conjunction();
            if (name != null && !name.isEmpty())
                predicates = cb.and(predicates, cb.like(root.get("name"), "%" + name + "%"));
            if (address != null && !address.isEmpty())
                predicates = cb.and(predicates, cb.like(root.get("address"), "%" + address + "%"));
            if (deptId != null)
                predicates = cb.and(predicates, cb.equal(root.get("department").get("id"), deptId));
            return predicates;
        }, pageable);
    }

    public void inactiveEmployee(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));
        emp.setActive(false);
        employeeRepository.save(emp);
    }
}
