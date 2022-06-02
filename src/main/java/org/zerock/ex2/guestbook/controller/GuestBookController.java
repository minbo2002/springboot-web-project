package org.zerock.ex2.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.ex2.guestbook.dto.GuestBookDTO;
import org.zerock.ex2.guestbook.dto.PageRequestDTO;
import org.zerock.ex2.guestbook.dto.PageResultDTO;
import org.zerock.ex2.guestbook.service.GuestBookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;

    @GetMapping("/")
    public String index() {

        return "redirect:/guestbook/list";
    }

    // 목록
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list......" + pageRequestDTO);

        model.addAttribute("result", guestBookService.getList(pageRequestDTO));
    }

    // 등록화면
    @GetMapping("/register")
    public void registerGet() {
        log.info("register GET");
    }

    // 등록처리
    @PostMapping("/register")
    public String registerPost(GuestBookDTO guestBookDTO, RedirectAttributes redirectAttributes) {
        log.info("register POST");

        Long gno = guestBookService.register(guestBookDTO);

        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    @GetMapping("/read")
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {
        log.info("gno : " + gno);

        GuestBookDTO dto = guestBookService.read(gno);

        model.addAttribute("dto", dto);
    }
}
