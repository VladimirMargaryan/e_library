package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.ReceiptRepository;
import com.app.e_library.service.dto.ReceiptDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public List<ReceiptDto> getAll() {
        return ReceiptDto.mapToDtoList(receiptRepository.findAll());
    }

    public ReceiptDto getById(Long id) throws NotFoundException {
        return receiptRepository.findById(id).map(ReceiptDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public ReceiptDto save(ReceiptDto receiptDto) {
        return ReceiptDto
                .mapToDto(receiptRepository
                        .save(ReceiptDto.mapToEntity(receiptDto)));
    }

    @Transactional
    public ReceiptDto update(ReceiptDto receiptDto) {
        ReceiptDto byId = getById(receiptDto.getId());
        byId.setBook(receiptDto.getBook());
        byId.setExpirationDate(receiptDto.getExpirationDate());
        byId.setOrderDate(receiptDto.getOrderDate());
        byId.setUser(receiptDto.getUser());
        return ReceiptDto
                .mapToDto(receiptRepository
                        .save(ReceiptDto.mapToEntity(receiptDto)));
    }

    @Transactional
    public ReceiptDto deleteById(Long id) {
        ReceiptDto byId = getById(id);
        receiptRepository.deleteById(byId.getId());
        return byId;
    }
}
