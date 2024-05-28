package com.kushnir.pastebin.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasteRepository extends JpaRepository<Paste, Integer> {
}
