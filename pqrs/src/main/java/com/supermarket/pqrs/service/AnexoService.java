package com.supermarket.pqrs.service;

import com.supermarket.pqrs.model.Anexo;
import com.supermarket.pqrs.repository.AnexoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnexoService {

    private final AnexoRepository anexoRepository;

    public List<Anexo> findAll() {
        return anexoRepository.findAll();
    }

    public Optional<Anexo> findById(Long id) {
        return anexoRepository.findById(id);
    }

    public Anexo save(Anexo anexo) {
        return anexoRepository.save(anexo);
    }

    public void delete(Long id) {
        anexoRepository.deleteById(id);
    }
}
