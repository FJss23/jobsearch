package com.fjss23.jobsearch.selection_process;

import com.fjss23.jobsearch.ApiV1PrefixController;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@PreAuthorize("hasRole('ROLE_CANDIDATE')")
@ApiV1PrefixController("selection-process")
public class SelectionProcessController {

    @GetMapping
    public List<SelectionProcessResponseDto> getSelectionProcessesForUser() {
        var processes = Arrays.asList(new SelectionProcessResponseDto("1", "2", false, OffsetDateTime.now()));
        return processes;
    }
}
