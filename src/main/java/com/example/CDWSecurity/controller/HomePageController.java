package com.example.CDWSecurity.controller;

import com.example.CDWSecurity.model.User;
import com.example.CDWSecurity.repository.UserRepository;
import com.example.CDWSecurity.service.DanhMucService;
import com.example.CDWSecurity.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomePageController {

    @Autowired
    DanhMucService danhMucService;
    @Autowired
    SanPhamService sanPhamService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String homeIndex(Model model, HttpSession session){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = userRepository.findByUsername(((UserDetails) principal).getUsername());
            session.setAttribute("user",user);
        }
        session.setAttribute("listDanhMuc",danhMucService.findAllDanhMuc());
        session.setAttribute("listSanPham",sanPhamService.findAll());
        return "index";

    }
    @GetMapping("/Home/spById/{id}")
    public String homeDetail(Model model , @PathVariable("id") long id,HttpSession session){
        session.setAttribute("spById",sanPhamService.findById(id));
        session.setAttribute("listDanhMuc",danhMucService.findAllDanhMuc());
        session.setAttribute("listSanPham",sanPhamService.findAll());
        return "Home/detail-product";
    }
}
