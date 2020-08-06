package com.example.CDWSecurity.controller;

import com.example.CDWSecurity.model.DanhMuc;
import com.example.CDWSecurity.model.SanPham;
import com.example.CDWSecurity.service.DanhMucService;
import com.example.CDWSecurity.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class DanhMucController {
    SanPham sanPham;
    @Autowired
    DanhMucService danhMucService;
    @Autowired
    SanPhamService sanPhamService;
    @GetMapping("/Admin/quanlydm")
    public String showDanhMuc(Model model, HttpServletRequest request){
        request.getSession().setAttribute("listdm", null);
        return "redirect:/Admin/qldm/page/1";
    }

    //    phan trang
    @GetMapping("Admin/qldm/page/{pageNumber}")
    public String showEmployeePage(HttpServletRequest request,
                                   @PathVariable int pageNumber, Model model) {
        PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("listdm");
        int pagesize = 3;
        List<DanhMuc> list =(List<DanhMuc>) danhMucService.findAllDanhMuc();
        System.out.println(list.size());
        if (pages == null) {
            pages = new PagedListHolder<>(list);
            pages.setPageSize(pagesize);
        } else {
            final int goToPage = pageNumber - 1;
            if (goToPage <= pages.getPageCount() && goToPage >= 0) {
                pages.setPage(goToPage);
            }
        }
        request.getSession().setAttribute("listdm", pages);
        int current = pages.getPage() + 1;
        int begin = Math.max(1, current - list.size());
        int end = Math.min(begin + 5, pages.getPageCount());
        int totalPageCount = pages.getPageCount();
        String baseUrl = "/Admin/qldm/page/";

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("categorys", pages);
        model.addAttribute("listdanhmuc",danhMucService.findAllDanhMuc());
        return "Admin/admin-danhmuc";
    }
    //    search
    @GetMapping("Admin/qldm/search/{pageNumber}")
    public String search(@RequestParam("search") String search, Model model, HttpServletRequest request,
                         @PathVariable int pageNumber) {
        if (search.equals("")) {
            return "redirect:/Admin/quanlydm";
        }
        List<DanhMuc> list = danhMucService.search(search);
        if (list == null) {
            return "redirect:/Admin/quanlydm";
        }
        PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("listdm");
        int pagesize = 3;
        pages = new PagedListHolder<>(list);
        pages.setPageSize(pagesize);

        final int goToPage = pageNumber - 1;
        if (goToPage <= pages.getPageCount() && goToPage >= 0) {
            pages.setPage(goToPage);
        }
        request.getSession().setAttribute("employeelist", pages);
        int current = pages.getPage() + 1;
        int begin = Math.max(1, current - list.size());
        int end = Math.min(begin + 5, pages.getPageCount());
        int totalPageCount = pages.getPageCount();
        String baseUrl = "/Admin/qldm/page/";
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("categorys", pages);
        return "Admin/admin-danhmuc";
    }
    @GetMapping("/Admin/qldm/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        DanhMuc danhMuc = danhMucService.findById(id);
        danhMucService.deleteDanhMuc(danhMuc);
        return "redirect:/Admin/quanlydm";
    }
    @PostMapping(value = "/InsertDanhMuc")
    public String insertDanhMuc(@ModelAttribute("insertDanhMuc") DanhMuc danhMuc) {

        danhMucService.insertDanhMuc(danhMuc);

        return "redirect:/Admin/quanlydm";
    }

}
