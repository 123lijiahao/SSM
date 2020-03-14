package entrance.service;

import entrance.bean.Employee;
import entrance.bean.EmployeeExample;
import entrance.bean.Msg;
import entrance.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    //查询所有员工
    public List<Employee> getAll() {
        //查询条件
        return employeeMapper.selectByExampleWithDept(null);
    }

    //员工保存方法
    public void saveEmp(Employee employee) {
        employeeMapper.insertSelective(employee);
    }

    //校验用户名是否可用
    public boolean checkUser(String empName) {
        //将查询条件放在example中
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpNameEqualTo(empName);
        long count = employeeMapper.countByExample(example);
        //count == 0 说明员工名可用
        return count == 0;
    }

    public Employee getEmp(Integer id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        return employee;
    }

    //更新员工数据
    public void upadateEmp(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public void deleteEmp(Integer id) {
        employeeMapper.deleteByPrimaryKey(id);
    }

    //批处理删除
    public void deleteBatch(List<Integer> ids) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpIdIn(ids);
        employeeMapper.deleteByExample(example);
    }
}
