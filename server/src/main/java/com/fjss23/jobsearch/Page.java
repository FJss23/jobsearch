package com.fjss23.jobsearch;

import java.util.List;

public record Page<T>(List<T> content, Long prev, Long next, int total, boolean first) {}
