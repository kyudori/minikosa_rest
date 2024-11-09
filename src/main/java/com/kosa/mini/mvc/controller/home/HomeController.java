package com.kosa.mini.mvc.controller.home;

import com.kosa.mini.mvc.domain.home.StoreDTO;
import com.kosa.mini.mvc.service.home.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private HomeService service;

    @GetMapping({"/", "/home"})
    public String goHome(Model model){
        List<StoreDTO> list = service.viewStoreHome();
        model.addAttribute("store", list);

        return "home";
    }
}
