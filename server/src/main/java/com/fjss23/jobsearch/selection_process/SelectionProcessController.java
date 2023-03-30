package com.fjss23.jobsearch.selection_process;

import com.fjss23.jobsearch.ApiV1PrefixController;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@ApiV1PrefixController
@PreAuthorize("hasRole('ROLE_CANDIDATE')")
public class SelectionProcessController {

    @GetMapping("/selection-process")
    public List<SelectionProcessResponse> getSelectionProcessesForUser() {
        var processes = Arrays.asList(new SelectionProcessResponse("1", "2", false, OffsetDateTime.now()));
        return processes;
    }
}
