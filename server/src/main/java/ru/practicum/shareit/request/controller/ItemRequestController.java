package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validate.UserIdValid;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(@UserIdValid @RequestHeader("X-Sharer-User-Id") Long userId,
                                        @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return requestService.createRequest(itemRequestDto, userId);
    }
    @GetMapping
    public List<ItemRequestDto> getRequests(@UserIdValid @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getRequests(userId);
    }
    @GetMapping("/{id}")
    public ItemRequestDto getRequestById(@PathVariable Long id) {
        return requestService.getRequest(id);
    }
    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests() {
        return requestService.getAllRequests();
    }

}
