package io.github.daviaarrudaofc.libaryAPI.service;

import io.github.daviaarrudaofc.libaryAPI.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository  livroRepository;
    
}
