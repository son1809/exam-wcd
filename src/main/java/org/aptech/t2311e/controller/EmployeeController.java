package org.aptech.t2311e.controller;

import org.aptech.t2311e.entity.Employee;
import org.aptech.t2311e.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/{deptId}")
    public Employee createEmployee(@RequestBody Employee employee, @PathVariable Long deptId) {
        return employeeService.createEmployee(employee, deptId);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @GetMapping("/search")
    public Page<Employee> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Long deptId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return employeeService.searchEmployees(name, address, deptId, page, size);
    }

    @PutMapping("/{id}/inactive")
    public void inactive(@PathVariable Long id) {
        employeeService.inactiveEmployee(id);
    }
}
