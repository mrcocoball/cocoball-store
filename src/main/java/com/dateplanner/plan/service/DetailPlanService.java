package com.dateplanner.plan.service;

import com.dateplanner.plan.repository.DetailPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class DetailPlanService {

    private final DetailPlanRepository detailPlanRepository;

}
