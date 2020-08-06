package com.example.CDWSecurity.controller;

import com.example.CDWSecurity.model.DanhMuc;
import com.example.CDWSecurity.model.Images;
import com.example.CDWSecurity.model.SanPham;
import com.example.CDWSecurity.service.DanhMucService;
import com.example.CDWSecurity.service.ImagesService;
import com.example.CDWSecurity.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

@Controller
public class SanPhamController {
    @Autowired
    SanPhamService sanPhamService;
    @Autowired
    DanhMucService danhMucService;
    @Autowired
    ImagesService imagesService;

    @GetMapping("/Admin/quanlysp")
    public String quanlySP(Model model, HttpServletRequest request
            , RedirectAttributes redirect) {
        request.getSession().setAttribute("listsp", null);
        return "redirect:/Admin/qlsp/page/1";
    }

    //    phan trang
    @GetMapping("Admin/qlsp/page/{pageNumber}")
    public String showEmployeePage(HttpServletRequest request,
                                   @PathVariable int pageNumber, Model model, HttpSession session) {
        PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("listsp");
        int pagesize = 5;
        List<SanPham> list =(List<SanPham>) sanPhamService.findAll();
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
        request.getSession().setAttribute("listsp", pages);
        int current = pages.getPage() + 1;
        int begin = Math.max(1, current - list.size());
        int end = Math.min(begin + 5, pages.getPageCount());
        int totalPageCount = pages.getPageCount();
        String baseUrl = "/Admin/qlsp/page/";

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("product", pages);
        model.addAttribute("listdanhmuc",danhMucService.findAllDanhMuc());

        return "Admin/admin-sanpham";
    }

    @PostMapping(value = "/InsertSanPham")
    public String insertSanPham(@RequestParam("tensanpham")String tensanpham, @RequestParam("giasanpham")String giasanpham,
                                @RequestParam("giamgia")String giamgia, @RequestParam("motasanpham")String motasanpham,
                                @RequestParam("soluong")String soluong, @RequestParam("id_danhmuc")Long id_danhmuc,
                                @RequestParam("hinhsanpham") MultipartFile[] files , Model model) {
        Calendar calendar = Calendar.getInstance();
        DanhMuc danhMuc = danhMucService.findById(id_danhmuc);
        SanPham sp = new SanPham(tensanpham,Float.parseFloat(giasanpham),Float.parseFloat(giamgia),
                motasanpham,calendar.getTime(),Float.parseFloat(soluong),danhMuc);
        sanPhamService.insertSp(sp);
        Long idSpMax = sanPhamService.maxId();

        try {
            for (MultipartFile file : files) {
                byte[] bytes = file.getBytes();
                String rootPath = "D:\\CDW\\CDWEB\\CDWEB\\src\\main\\resources\\static";
                File dir = new File(rootPath + File.separator + "images");
                String name = file.getOriginalFilename();
                if (!dir.exists())
                    dir.mkdirs();
                File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                SanPham s = sanPhamService.findById(idSpMax);
                Images images = new Images(name,idSpMax);
                imagesService.insertImages(images);


            }

        }catch (Exception e ){
            e.printStackTrace();
        }
        return "redirect:/Admin/quanlysp";
    }

    @GetMapping("/Admin/qlsp/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        SanPham emp = sanPhamService.findById(id);
        sanPhamService.delete(emp);

        return "redirect:/Admin/quanlysp";
    }

    @GetMapping("/Admin/edit")
    public String edit(@PathVariable long id, Model model) {
        model.addAttribute("sanpham", sanPhamService.findById(id));
        return "redirect:/Admin/quanlysp";
    }
    @RequestMapping("/Admin/listSpByDmuc/{id}")
    public String listSpByDmuc(@PathVariable("id")long id,Model model){
        DanhMuc dmuc = danhMucService.findById(id);
        List<SanPham> list = dmuc.getSanPhams();
        model.addAttribute("listspbydanhmuc",list);
        model.addAttribute("listdanhmuc",danhMucService.findAllDanhMuc());
        return "Admin/admin-sanphambydanhmuc";
//        return "redirect:/Admin/quanlysp";
    }

    @RequestMapping("/Admin/getImages")
    public @ResponseBody String showImagesbyId(){
        Images images = imagesService.findById(5);
        return images.getName_img();
    }


}
