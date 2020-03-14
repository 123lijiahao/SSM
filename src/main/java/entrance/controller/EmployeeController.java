package entrance.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import entrance.bean.Employee;
import entrance.bean.Msg;
import entrance.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//处理员工CRUD请求
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    //单个，批量二合一
    @ResponseBody
    @DeleteMapping("/emp/{ids}")
    public Msg deleteEmp(@PathVariable("ids") String ids){
        if(ids.contains("-")){
            List<Integer> list = new ArrayList<>();
            //批量删除
            String[] str_ids = ids.split("-");
            //组装id集合
            for (String id : str_ids) {
                list.add(Integer.parseInt(id));
            }

            employeeService.deleteBatch(list);
        }else {
            //单个删除
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }

        return Msg.success();
    }

    @PutMapping("/emp/{empId}")
    @ResponseBody
    public Msg saveEmp(Employee employee){
        System.out.println(employee);
        employeeService.upadateEmp(employee);
        return Msg.success();
    }

    //根据id保存对象
    @GetMapping("/emp/{id}")
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }

    //校验用户名是否可用：去重
    @PostMapping("/checkuser")
    @ResponseBody
    public Msg checkuser(String empName){
        //判断用户名是否是合法的表达式
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if(!empName.matches(regx)){
            return Msg.fail().add("va_msg","用户名规则：2~5位中文、6~16位英文、数字的纯文本或者组合");
        }
        boolean bool = employeeService.checkUser(empName);
        if(bool){
            return Msg.success();
        }
        return Msg.fail().add("va_msg","用户名重复~(-_-||)");
    }

    //保存员工信息
    @PostMapping("/emp")
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        if(result.hasErrors()){
            //后台校验失败
            Map<String,Object> map = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                System.out.println("错误的字段名："+fieldError.getField());
                System.out.println("错误的信息："+fieldError.getDefaultMessage());
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorfields",map);
        }else{
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        //原本不是一个分页查询
        //需要引入一个PageHelper分页插件
        //在查询之前只需要调用
        PageHelper.startPage(pn,5);//查第几页，每页几个数据
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果，这样可以有一些列的分页方法可以调用，所以将pageInfo交给页面
        PageInfo pageInfo = new PageInfo(emps,5);//连续显示五页
        return Msg.success().add("pageInfo",pageInfo);
    }


    //查询员工数据：分页查询
    //@RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model){
        //原本不是一个分页查询
        //需要引入一个PageHelper分页插件
        //在查询之前只需要调用
        PageHelper.startPage(pn,5);//查第几页，每页几个数据
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果，这样可以有一些列的分页方法可以调用，所以将pageInfo交给页面
        PageInfo pageInfo = new PageInfo(emps,5);//连续显示五页
        //交给request域
        model.addAttribute("pageInfo",pageInfo);
        return "list";
    }
}
