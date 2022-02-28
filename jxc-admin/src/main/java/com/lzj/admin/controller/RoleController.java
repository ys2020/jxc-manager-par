package com.lzj.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/role")
public class RoleController {

    @RequestMapping("index")
    public String index(){
        return "role/role";
    }
}
