package entrance.test;

import entrance.bean.Department;
import entrance.bean.Employee;
import entrance.dao.DepartmentMapper;
import entrance.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/*测试dao层：可以直接使用原生的代码
但是推荐使用spring的单元测试，可以自动注入我们需要的组件
@ContextConfiguration：指定spring文件的位置
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    SqlSession sqlSession;

    @Test
    public void testCRUD(){
        //1、有选择的插入，可以漏掉一些属性
//        departmentMapper.insertSelective(new Department(null,"开发部"));
//        departmentMapper.insertSelective(new Department(null,"测试部 "));

        //2、生成员工数据，进行员工插入
        //employeeMapper.insertSelective(new Employee(null,"张三","M","1@qq.com",1));
        //3、批量插入员工，例子用uuid实现：批处理可以使用SqlSession
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for(int i = 0;i < 100; i++ ){
            String uuid = UUID.randomUUID().toString().substring(0,5)+"_"+i;
            mapper.insertSelective(new Employee(null,uuid,"M","@qq.com",1));
        }
        System.out.println("批处理成功");
    }

}
