package com.kushnir.pastebin.controller;

import com.kushnir.pastebin.model.Paste;
import com.kushnir.pastebin.model.PasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class PasteController {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private PasteRepository pasteRepository;

    @GetMapping("/")
    public String home(Model model, @RequestParam(defaultValue = "1") int page) {
        int totalPasteCount = (int) pasteRepository.count();
        int totalPages = (int) Math.ceil((double) totalPasteCount / PAGE_SIZE);

        if (totalPages == 0) {
            totalPages = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC,"id"));
        Page<Paste> pastePage = pasteRepository.findAll(pageable);
        List<Paste> pastes = pastePage.getContent();

        model.addAttribute("pastes", pastes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("paste", new Paste());
        return "create";
    }

    @PostMapping("/create")
    public String createPaste(@ModelAttribute Paste paste, RedirectAttributes redirectAttributes) {
        paste.setCreatedAt(new Date());
        Paste savedPaste = pasteRepository.save(paste);

        redirectAttributes.addFlashAttribute("success", "Пасту успішно створено.");
        return "redirect:/paste/" + savedPaste.getId();
    }

    @GetMapping("/paste/{id}")
    public String viewPaste(@PathVariable int id, Model model) {
        Paste paste = pasteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Paste not found."));

        model.addAttribute("paste", paste);
        return "view";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
