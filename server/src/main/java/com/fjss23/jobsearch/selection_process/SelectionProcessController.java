package com.fjss23.jobsearch.selection_process;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/selection-process")
@PreAuthorize("hasRole('ROLE_CANDIDATE')")
public class SelectionProcessController {

    @GetMapping
    public List<SelectionProcessResponseDto> getSelectionProcessesForUser() {
        var processes = Arrays.asList(
            new SelectionProcessResponseDto("1", "2", false, OffsetDateTime.now()));
        return processes;
    }
}
