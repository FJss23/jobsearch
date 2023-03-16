package com.fjss23.jobsearch.selection_process;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("selection-process")
@PreAuthorize("hasRole('ROLE_CANDIDATE')")
public class SelectionProcessController {

    @GetMapping
    public List<SelectionProcessResponseDto> getSelectionProcessesForUser() {
        var processes = Arrays.asList(new SelectionProcessResponseDto("1", "2", false, OffsetDateTime.now()));
        return processes;
    }
}
