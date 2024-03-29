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
import org.zerock.ex2.guestbook.service.GuestBookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;

    /* 생성자 주입방식
    @Autowired
    public GuestBookController(GuestBookService guestBookService) {
        this.guestBookService = guestBookService;
    }
    */

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

    // 읽기, 수정
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {
        log.info("gno : " + gno);

        GuestBookDTO dto = guestBookService.read(gno);

        model.addAttribute("dto", dto);
    }

    // 수정
    @PostMapping("/modify")
    public String modify(GuestBookDTO guestBookDTO,
                         @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes) {

        log.info("post modify........................");
        log.info("guestBookDTO : " + guestBookDTO);

        guestBookService.modify(guestBookDTO);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", guestBookDTO.getGno());

        return "redirect:/guestbook/read";
    }

    // 삭제
    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes) {
        log.info("gno : " + gno);

        guestBookService.remove(gno);

        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }
}
