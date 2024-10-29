package com.fyp.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyp.pojo.*;
import com.fyp.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Controller
public class AccountController extends SidebarController{
    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ShopWorkerService shopworkerService;

    @Autowired
    private ShopOwnerService shopownerService;

    @Autowired
    private DeliverymanService deliverymanService;

    @Autowired
    private OrderService orderService;

    @Value("${location}")
    private String location;

    //跳转到登录页面
    @GetMapping("/toLogin")
    public String toLogin(){
        return "index";
    }
    //跳转到注册页面
    @GetMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/logintoadmin")
    public String logintoadmin(){
        return "loginpage_admin";
    }

    @RequestMapping("/logintocustomer")
    public String logintocustomer(){
        return "loginpage_customer";
    }

    @RequestMapping("/logintoshopowner")
    public String logintoshopowner(){
        return "loginpage_shopowner";
    }

    @RequestMapping("/logintoshopworker")
    public String logintoshopworker(){
        return "loginpage_shopworker";
    }

    @RequestMapping("/logintodeliveryman")
    public String logintodeliveryman(){
        return "loginpage_deliveryman";
    }

    //登录
    @RequestMapping("/login_admin")
    public String login_admin(String userName, String userPwd, Model model, HttpSession session) {

        if (userName!=null && userName.equals("admin")){
            boolean login = accountService.login(userName, userPwd);
            if (login) {
                QueryWrapper<User>qw=new QueryWrapper<>();
                User one = accountService.getOne(qw);
                session.setAttribute("currentUser", userName);
                session.setAttribute("password",userPwd);
                session.setAttribute("email",one.getEmail());
                session.setAttribute("image",one.getImage());
                session.setAttribute("type",one.getClass());

                List<Customer> list = customerService.list(null);
                Integer count=list.size();
                model.addAttribute("customercount",count);

                List<Store> list2 = storeService.list(null);
                Integer count2=list2.size();
                model.addAttribute("storecount",count2);

                return "mainpage_admin";
            } else {
                model.addAttribute("msg", "用户名或密码错误！");
                return "loginpage_admin";
            }
        }else {
            model.addAttribute("msg", "admin用户名错误！");
            return "index";
        }
    }

    @RequestMapping("/login_customer")
    public String login_customer(String userName, String userPwd, Model model, HttpSession session, Item item) {
        boolean login = customerService.login(userName, userPwd);
        if (login) {
            QueryWrapper<Customer> qw = new QueryWrapper<>();
            qw.eq("customerloginid", userName);
            Customer one = customerService.getOne(qw);
            session.setAttribute("currentUser", userName);
            session.setAttribute("userId", one.getId());
            session.setAttribute("password", userPwd);
            session.setAttribute("image", one.getCimage());
            session.setAttribute("email", one.getEmail());
            session.setAttribute("type",one.getClass());

            String itemName = item.getItemName();
            PageInfo<Order> pageInfo=orderService.listMyOrders(one.getId(),itemName);
            model.addAttribute("pageInfo",pageInfo);

            return "myOrders-list";
        }else {
            model.addAttribute("msg", "用户名或密码错误！");
            return "loginpage_customer";
        }
    }

    @RequestMapping("/login_shopworker")
    public String login_shopworker(String userName, String userPwd, Model model, HttpSession session) {
        boolean login = shopworkerService.login(userName, userPwd);
        if (login) {
            QueryWrapper<ShopWorker> qw = new QueryWrapper<>();
            qw.eq("shopworkerloginid", userName);
            ShopWorker one = shopworkerService.getOne(qw);
            session.setAttribute("currentUser", userName);
            session.setAttribute("workerId", one.getId());
            session.setAttribute("shopId",one.getShopId());
            session.setAttribute("password", userPwd);
            session.setAttribute("image", one.getCimage());
            session.setAttribute("email", one.getEmail());
            session.setAttribute("type",one.getClass());
            return "redirect:/order/listOrder";
        }else {
            model.addAttribute("msg", "用户名或密码错误！");
            return "loginpage_shopworker";
        }
    }

    @RequestMapping("/login_shopowner")
    public String login_shopowner(String userName, String userPwd, Model model, HttpSession session) {
        boolean login = shopownerService.login(userName, userPwd);
        if (login) {
            QueryWrapper<ShopOwner> qw = new QueryWrapper<>();
            qw.eq("shopownerloginid", userName);
            ShopOwner one = shopownerService.getOne(qw);
            session.setAttribute("currentUser", userName);
            session.setAttribute("ownerId", one.getId());
            session.setAttribute("shopId",one.getShopId());
            session.setAttribute("password", userPwd);
            session.setAttribute("image", one.getCimage());
            session.setAttribute("email", one.getEmail());
            session.setAttribute("type",one.getClass());
            return "chart_total";
        }else {
            model.addAttribute("msg", "用户名或密码错误！");
            return "loginpage_shopowner";
        }
    }

    @RequestMapping("/login_deliveryman")
    public String login_deliveryman(String userName, String userPwd, Model model, HttpSession session) {
        boolean login = deliverymanService.login(userName, userPwd);
        if (login) {
            QueryWrapper<Deliveryman> qw = new QueryWrapper<>();
            qw.eq("deliverymanloginid", userName);
            Deliveryman one = deliverymanService.getOne(qw);
            session.setAttribute("currentUser", userName);
            session.setAttribute("deliverymanId", one.getId());
            session.setAttribute("shopId",one.getShopId());
            session.setAttribute("password", userPwd);
            session.setAttribute("image", one.getCimage());
            session.setAttribute("email", one.getEmail());
            session.setAttribute("type",one.getClass());
            return "redirect:/deliveringorder/listDeliveringOrder";
        }else {
            model.addAttribute("msg", "用户名或密码错误！");
            return "loginpage_deliveryman";
        }
    }

    //注册
    @RequestMapping("/register")
    public String register(String userName, String userPwd,String confirmPwd, Model model, String email, String phone, String address) {
        QueryWrapper<Customer>qw=new QueryWrapper<>();
        qw.eq("customerloginid",userName);
        List<Customer> customer_sameid =customerService.list(qw);
        boolean customer_notexist=customer_sameid.isEmpty();
        if (!userPwd.equals(confirmPwd)){
            model.addAttribute("msg", "输入密码不一致");
            return "register";
        }
        if(customer_notexist==false){
            model.addAttribute("msg", "用户已存在，请使用其他的用户名来注册。若已有账号，请去往登录页面。");
            return "register";
        }
        else{
            Customer customer = new Customer();
            customer.setCustomerloginid(userName);
            String s = DigestUtil.md5Hex(userPwd);
            customer.setPassword(s);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setAddress(address);
            customerService.save(customer);
            model.addAttribute("msg","注册成功，请登录");
            return "index";
        }
    }

    //跳转到修改密码的页面
    @RequestMapping("pwd")
    public String preUpdate() {
        return "modify";
    }

    //修改密码
    @PostMapping("pwdUser")
    public String updatePwd(String userPwd, String newPwd, Model model, HttpSession session, HttpServletRequest request) {
        String currentUser = (String) session.getAttribute("currentUser");
        Object currentUserType = request.getSession().getAttribute("type");
        boolean login;
        if (currentUserType.toString().equals("class com.fyp.pojo.ShopOwner")){
            login = shopownerService.login(currentUser, userPwd);
            if (login) {
                ShopOwner shopowner= new ShopOwner();
                shopowner.setShopownerloginid(currentUser);
                String newPassword = DigestUtil.md5Hex(newPwd);
                shopowner.setPassword(newPassword);
                QueryWrapper<ShopOwner>qw=new QueryWrapper<>();
                qw.eq("shopownerloginid",shopowner.getShopownerloginid());
                boolean b = shopownerService.update(shopowner,qw);
                if (b) {
                    model.addAttribute("msg","更改成功，请重新登录");
                    return "index";
                } else {
                    model.addAttribute("loginFail", "修改密码失败");
                }
            } else {
                model.addAttribute("loginFail", "用户验证失败");
            }
        }
        else if (currentUserType.toString().equals("class com.fyp.pojo.ShopWorker")){
            login = shopworkerService.login(currentUser, userPwd);
            if (login) {
                ShopWorker shopworker= new ShopWorker();
                shopworker.setShopworkerloginid(currentUser);
                String newPassword = DigestUtil.md5Hex(newPwd);
                shopworker.setPassword(newPassword);
                QueryWrapper<ShopWorker>qw=new QueryWrapper<>();
                qw.eq("shopworkerloginid",shopworker.getShopworkerloginid());
                boolean b = shopworkerService.update(shopworker,qw);
                if (b) {
                    model.addAttribute("msg","更改成功，请重新登录");
                    return "index";
                } else {
                    model.addAttribute("loginFail", "修改密码失败");
                }
            } else {
                model.addAttribute("loginFail", "用户验证失败");
            }
        }
        else if (currentUserType.toString().equals("class com.fyp.pojo.Deliveryman")){
            login = deliverymanService.login(currentUser, userPwd);
            if (login) {
                Deliveryman deliveryman= new Deliveryman();
                deliveryman.setDeliverymanloginid(currentUser);
                String newPassword = DigestUtil.md5Hex(newPwd);
                deliveryman.setPassword(newPassword);
                QueryWrapper<Deliveryman>qw=new QueryWrapper<>();
                qw.eq("deliverymanloginid",deliveryman.getDeliverymanloginid());
                boolean b = deliverymanService.update(deliveryman,qw);
                if (b) {
                    model.addAttribute("msg","更改成功，请重新登录");
                    return "index";
                } else {
                    model.addAttribute("loginFail", "修改密码失败");
                }
            } else {
                model.addAttribute("loginFail", "用户验证失败");
            }
        }
        else if (currentUserType.toString().equals("class com.fyp.pojo.Customer")){
            login = customerService.login(currentUser, userPwd);
            if (login) {
                Customer customer= new Customer();
                customer.setCustomerloginid(currentUser);
                String newPassword = DigestUtil.md5Hex(newPwd);
                customer.setPassword(newPassword);
                QueryWrapper<Customer>qw=new QueryWrapper<>();
                qw.eq("customerloginid",customer.getCustomerloginid());
                boolean b = customerService.update(customer,qw);
                if (b) {
                    model.addAttribute("msg","更改成功，请重新登录");
                    return "index";
                } else {
                    model.addAttribute("loginFail", "修改密码失败");
                }
            } else {
                model.addAttribute("loginFail", "用户验证失败");
            }
        }
        else{
            login = accountService.login(currentUser, userPwd);
            if (login) {
                User user = new User();
                user.setUsername(currentUser);
                String newPassword = DigestUtil.md5Hex(newPwd);
                user.setPassword(newPassword);
                QueryWrapper<User>qw=new QueryWrapper<>();
                qw.eq("username",user.getUsername());
                boolean b = accountService.update(user,qw);
                if (b) {
                    model.addAttribute("msg","更改成功，请重新登录");
                    return "index";
                } else {
                    model.addAttribute("loginFail", "修改密码失败");
                }
            } else {
                model.addAttribute("loginFail", "用户验证失败");
            }
        }

        return "modify";
    }

    //登出
    @RequestMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }


    //跳转到统计销售量的页面
    @RequestMapping("count")
    public String count(){
        return "chart_count";
    }
    //跳转到统计销售额的页面
    @RequestMapping("total")
    public String count1(){
        return "chart_total";
    }

    @RequestMapping("toprofile")
    public String to_profile(HttpServletRequest request){
        String currentUser = (String) request.getSession().getAttribute("currentUser");
        String password =(String) request.getSession().getAttribute("password");

        if (currentUser.equals("admin")){
            return "redirect:/profile/admin";
        }

        QueryWrapper<Customer>qw=new QueryWrapper<>();
        qw.eq("customerloginid",currentUser);
        Customer cust = customerService.getOne(qw);
        if (cust!=null){
            return "redirect:/profile/customer";
        }

        QueryWrapper<ShopOwner>qw2=new QueryWrapper<>();
        qw2.eq("shopownerloginid",currentUser);
        ShopOwner owner = shopownerService.getOne(qw2);
        if (owner!=null){
            return "redirect:/profile/shopowner";
        }

        QueryWrapper<ShopWorker>qw3=new QueryWrapper<>();
        qw3.eq("shopworkerloginid",currentUser);
        ShopWorker worker = shopworkerService.getOne(qw3);
        if (worker!=null){
            return "redirect:/profile/shopworker";
        }

        QueryWrapper<Deliveryman>qw4=new QueryWrapper<>();
        qw4.eq("deliverymanloginid",currentUser);
        Deliveryman deliveryman = deliverymanService.getOne(qw4);
        if (deliveryman!=null){
            return "redirect:/profile/deliveryman";
        }
        else {return "error";}
    }

    //跳转到修改个人信息的页面
    @RequestMapping("profile/admin")
    public String profile_admin(HttpServletRequest request,Model model){
        String currentUser = (String) request.getSession().getAttribute("currentUser");
        String password =(String) request.getSession().getAttribute("password");

        if (currentUser.equals("admin")){
            QueryWrapper<User>qw=new QueryWrapper<>();
            qw.eq("username",currentUser);
            User one = accountService.getOne(qw);
            one.setPassword(password);//为了在个人简介页面显示的是没加密的密码
            model.addAttribute("user",one);
            return "profile-admin";
        }else {return "error";}
    }


    @RequestMapping("profile/customer")
    public String profile_customer(HttpServletRequest request,Model model){
        String currentUser = (String) request.getSession().getAttribute("currentUser");
        String password =(String) request.getSession().getAttribute("password");
        QueryWrapper<Customer>qw=new QueryWrapper<>();
        qw.eq("customerloginid",currentUser);
        Customer cust = customerService.getOne(qw);
        if (cust!=null){
            cust.setPassword(password);//为了在个人简介页面显示的是没加密的密码
            model.addAttribute("user",cust);
            return "profile-customer";
        }else {return "error";}
    }

    @RequestMapping("profile/shopowner")
    public String profile_shopowner(HttpServletRequest request,Model model){
        String currentUser = (String) request.getSession().getAttribute("currentUser");
        String password =(String) request.getSession().getAttribute("password");
        QueryWrapper<ShopOwner>qw=new QueryWrapper<>();
        qw.eq("shopownerloginid",currentUser);
        ShopOwner owner = shopownerService.getOne(qw);
        if (owner!=null){
            owner.setPassword(password);//为了在个人简介页面显示的是没加密的密码
            model.addAttribute("user",owner);
            return "profile-shopowner";
        }else {return "error";}
    }

    @RequestMapping("profile/shopworker")
    public String profile_shopworker(HttpServletRequest request,Model model){
        String currentUser = (String) request.getSession().getAttribute("currentUser");
        String password =(String) request.getSession().getAttribute("password");
        QueryWrapper<ShopWorker>qw=new QueryWrapper<>();
        qw.eq("shopworkerloginid",currentUser);
        ShopWorker worker = shopworkerService.getOne(qw);
        if (worker!=null){
            worker.setPassword(password);//为了在个人简介页面显示的是没加密的密码
            model.addAttribute("user",worker);
            return "profile-shopworker";
        }else {return "error";}
    }

    @RequestMapping("profile/deliveryman")
    public String profile_deliveryman(HttpServletRequest request,Model model,MultipartFile file){
        String currentUser = (String) request.getSession().getAttribute("currentUser");
        String password =(String) request.getSession().getAttribute("password");
        QueryWrapper<Deliveryman>qw=new QueryWrapper<>();
        qw.eq("deliverymanloginid",currentUser);
        Deliveryman deliveryman = deliverymanService.getOne(qw);
        if (deliveryman!=null){
            deliveryman.setPassword(password);//为了在个人简介页面显示的是没加密的密码
            model.addAttribute("user",deliveryman);
            return "profile-deliveryman";
        }else {return "error";}
    }

    //修改管理员个人信息
    @RequestMapping("updateAdminProfile")
    public String updateProfile(User user,HttpServletRequest request,MultipartFile file){
        //修改数据库
        String s = DigestUtil.md5Hex((String)request.getSession().getAttribute("password"));
        transFileAdmin(user,file,request);
        user.setPassword(s);
        boolean b = accountService.updateById(user);
        request.setAttribute("msg","资料修改成功，请重新登录");
        return "index";
    }

    @RequestMapping("updateCustomerProfile")
    public String updateReaderProfile(Customer customer,HttpServletRequest request, MultipartFile file){
        String s = DigestUtil.md5Hex((String)request.getSession().getAttribute("password"));
        transFileCustomer(customer,file,request);
        customer.setPassword(s);
        boolean b = customerService.updateById(customer);
        request.setAttribute("msg","资料修改成功，请重新登录");
        return "index";
    }

    @RequestMapping("updateShopWorkerProfile")
    public String updateProfile(ShopWorker shopworker,HttpServletRequest request,MultipartFile file){
        //修改数据库
        String s = DigestUtil.md5Hex((String)request.getSession().getAttribute("password"));
        transFileShopWorker(shopworker,file,request);
        shopworker.setPassword(s);
        boolean b = shopworkerService.updateById(shopworker);
        request.setAttribute("msg","资料修改成功，请重新登录");
        return "index";
    }

    @RequestMapping("updateShopOwnerProfile")
    public String updateProfile(ShopOwner shopowner,HttpServletRequest request, MultipartFile file){
        //修改数据库
        String s = DigestUtil.md5Hex((String)request.getSession().getAttribute("password"));
        transFileShopOwner(shopowner,file,request);
        shopowner.setPassword(s);
        boolean b = shopownerService.updateById(shopowner);
        request.setAttribute("msg","资料修改成功，请重新登录");
        return "index";
    }

    @RequestMapping("updateDeliverymanProfile")
    public String updateProfile(Deliveryman deliveryman,HttpServletRequest request,MultipartFile file){
        //修改数据库
        String s = DigestUtil.md5Hex((String)request.getSession().getAttribute("password"));
        transFileDeliveryman(deliveryman,file,request);
        deliveryman.setPassword(s);
        boolean b = deliverymanService.updateById(deliveryman);
        request.setAttribute("msg","资料修改成功，请重新登录");
        return "index";
    }

    //文件上传
    private void transFileCustomer(Customer customer, MultipartFile file,HttpServletRequest request) {
        if(file.isEmpty()){
            String path=(String) request.getSession().getAttribute("image");
            customer.setCimage(path);
            return;
        }
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String prefix =System.nanoTime()+"";
        String path=prefix+suffix;
        File file1 = new File(location);
        if (!file1.exists()){
            file1.mkdirs();
        }
        File file2 = new File(file1,path);
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        customer.setCimage(path);
    }

    private void transFileAdmin(User user, MultipartFile file,HttpServletRequest request) {
        if(file.isEmpty()){
            String path=(String) request.getSession().getAttribute("image");
            user.setImage(path);
            return;
        }
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String prefix =System.nanoTime()+"";
        String path=prefix+suffix;
        File file1 = new File(location);
        if (!file1.exists()){
            file1.mkdirs();
        }
        File file2 = new File(file1,path);
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setImage(path);
    }

    private void transFileShopWorker(ShopWorker shopworker, MultipartFile file, HttpServletRequest request) {
        if(file.isEmpty()){
            String path=(String) request.getSession().getAttribute("image");
            shopworker.setCimage(path);
            return;
        }
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String prefix =System.nanoTime()+"";
        String path=prefix+suffix;
        File file1 = new File(location);
        if (!file1.exists()){
            file1.mkdirs();
        }
        File file2 = new File(file1,path);
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        shopworker.setCimage(path);
    }

    private void transFileShopOwner(ShopOwner shopowner, MultipartFile file,HttpServletRequest request) {
        if(file.isEmpty()){
            String path=(String) request.getSession().getAttribute("image");
            shopowner.setCimage(path);
            return;
        }
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String prefix =System.nanoTime()+"";
        String path=prefix+suffix;
        File file1 = new File(location);
        if (!file1.exists()){
            file1.mkdirs();
        }
        File file2 = new File(file1,path);
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        shopowner.setCimage(path);
    }

    private void transFileDeliveryman(Deliveryman deliveryman, MultipartFile file,HttpServletRequest request) {
        if(file.isEmpty()){
            String path=(String) request.getSession().getAttribute("image");
            deliveryman.setCimage(path);
            return;
        }
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String prefix =System.nanoTime()+"";
        String path=prefix+suffix;
        File file1 = new File(location);
        if (!file1.exists()){
            file1.mkdirs();
        }
        File file2 = new File(file1,path);
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        deliveryman.setCimage(path);
    }

    @ModelAttribute("allstorelist")
    public List<Store> allStoreList(Model model, Store store){
        QueryWrapper<Store> qw=new QueryWrapper<>();
        if (store.getStoreName()!=null){
            qw.like("store_name",store.getStoreName());
        }

        List<Store> allstoreList = storeService.list(qw);
        return allstoreList;
    }



}
