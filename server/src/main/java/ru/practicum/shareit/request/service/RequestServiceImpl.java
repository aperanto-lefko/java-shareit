package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.RequestIdNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService { //добавить интерфейс
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userService.getUserById(userId);
        ItemRequest request = RequestMapper.toRequest(itemRequestDto, user);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    public ItemRequest getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RequestIdNotFoundException("Запрос с id " + id + " не найден"));
    }

    public List<ItemRequestDto> getRequests(Long id) {
        return toListRequestDto(requestRepository.findByRequestor(id)).stream()
                .peek(this::setItems) //добавление подходящих по запросу вещей
                .toList();

    }

    public List<ItemRequestDto> getAllRequests() {
        return toListRequestDto(requestRepository.findAll()).stream()
                .peek(this::setItems) //добавление подходящих по запросу вещей
                .toList();

    }

    private ItemRequestDto setItems(ItemRequestDto request) {
        request.setItems(itemService.searchItemByRequest(request.getId()));
        return request;
    }

    public ItemRequestDto getRequest(Long requestId) {
        return setItems(RequestMapper.toRequestDto(getRequestById(requestId)));
    }

    private List<ItemRequestDto> toListRequestDto(List<ItemRequest> list) {
        return list.stream()
                .map(RequestMapper::toRequestDto)
                .toList();
    }
}
