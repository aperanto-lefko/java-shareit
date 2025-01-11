package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@Controller
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;
    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                                @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Создание запроса пользователем {}, с описанием {}", userId, itemRequestDto.getDescription());
        return itemRequestClient.createRequest(userId, itemRequestDto);
    }

}
