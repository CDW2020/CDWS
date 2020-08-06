package com.example.CDWSecurity.controller;



import com.example.CDWSecurity.model.User;
import com.example.CDWSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        Object pricipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (pricipal instanceof UserDetails) {
            return "redirect:/";
        }
        return "login";
    }


    @GetMapping("/login/error")
    public String loginErr(Model model) {
        String message = "Tài khoản không hợp lệ";
        model.addAttribute("message", message);
        return "login";
    }


    @GetMapping("/register")
    public String register(Model model) {
        User u = new User();
        model.addAttribute("user", u);
        return "register";
    }

//    @PostMapping("/add-user")
//    public String addUser(@ModelAttribute("user") User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        userRepository.save(user);
//        long id = userRepository.findByUsername(user.getUsername()).getId();
//
//        UserRole userRole = new UserRole();
//        userRole.setRoleId(2);
//        userRole.setUserId((id));
//        userRoleRepository.save(userRole);
//
//        return "redirect:/login";
//
//    }
//
//    @GetMapping("/delete-user")
//    public String deleteUser(@PathVariable("id") int id) {
//        try {
//            userRepository.deleteById(id);
//            return "ok";
//        } catch (Exception e) {
//            return "err";
//        }
//    }
//
//    @PostMapping("/update-user")
//    public User updateUser(@ModelAttribute("userupdate") User user) {
//        return userRepository.save(user);
//    }
//
//    @RequestMapping(value = "/about")
//    public String about(Model model) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            String email = ((UserDetails) principal).getUsername();
//            User userModel = userRepository.findUserByEmail(email);
//            model.addAttribute("userAbout", userModel);
//        }
//        return "home/about";
//    }
//
//    // thay doi thong tin tai khoan
//    @RequestMapping(value = "/saveChange")
//    public String saveChange(@RequestParam(value = "address") String address, @RequestParam(value = "name")
//            String name, @RequestParam(value = "phone") String phone,
//                             String dob, Model model) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            String email = ((UserDetails) principal).getUsername();
//            User userModel = userRepository.findUserByEmail(email);
//            userModel.setAddress(address);
//            userModel.setName(name);
//            userModel.setPhone(phone);
//            userRepository.save(userModel);
//            model.addAttribute("userAbout", userModel);
//            return "home/about";
//        }
//        return "home/about";
//    }

    // doi password
    @RequestMapping(value = "/passwordChange")
    public String passwordChange(@RequestParam(value = "newPassword") String newPassword, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String pass = passwordEncoder.encode(newPassword);
            String email = ((UserDetails) principal).getUsername();
            User userModel = userRepository.findByUsername(email);
            userModel.setPassword(pass);
            model.addAttribute("userAbout", userModel);
            return "home/about";
        }
        return "home/about";
    }

    //ajaxCompeletePassword
    @RequestMapping(value = "/ajaxCompletePassword", method = RequestMethod.POST)
    public @ResponseBody
    String ajaxCompletePassword(@RequestParam(value = "passwordOld") String passwordOld) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            User userModel = userRepository.findByUsername(email);
            //kiem tra mat khau cu va mat khau moi
            boolean check = passwordEncoder.matches(passwordOld, ((UserDetails) principal).getPassword());
            if(check) return "success";
        }
        return "error";
    }

}
