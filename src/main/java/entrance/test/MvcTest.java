package entrance.test;

import com.github.pagehelper.PageInfo;
import entrance.bean.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

//使用Spring测试模块提供的测试请求功能，测试CRUD请求的正确性
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "file:/WEB-INF/springmvc.xml"})
public class MvcTest {
    //传入SpringMvc的ioc
    @Autowired//但是只能注入ioc容器里有的，这里需要注入ioc容器本身，所以要添加@WebAppConfiguration
    WebApplicationContext context;

    //虚拟的mvc：发送模拟请求
    MockMvc mockMvc;

    @Before//初始化一次
    public void initMockMvc(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testPage() throws Exception {
        //模拟发送请求,拿到返回值
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "5")).andReturn();
        //请求成功后，请求域中会有pageInfo,可以进行验证
        MockHttpServletRequest request = result.getRequest();
        PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
        System.out.println("当前页码："+pageInfo.getPageNum());
        System.out.println("总页码数："+pageInfo.getPages());
        System.out.println("总记录数："+pageInfo.getTotal());
        System.out.println("在页面上需要连续显示的页码");
        int[] navigatepageNums = pageInfo.getNavigatepageNums();
        for(int i : navigatepageNums ){
            System.out.print(" "+i);
        }

        //获取员工数据
        List<Employee> list = pageInfo.getList();
        for (Employee employee : list) {
            System.out.println(employee);
        }
    }
}
