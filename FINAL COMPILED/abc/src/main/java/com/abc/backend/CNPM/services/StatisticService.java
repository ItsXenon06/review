package com.abc.backend.CNPM.service;

import com.abc.backend.CNPM.repository.StatisticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticService {

    private final StatisticRepository statisticRepository;

    public StatisticService(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    public Double getTotalRevenue() {
        Double revenue = statisticRepository.getTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    public Long getTotalBookings() {
        Long count = statisticRepository.getTotalBookings();
        return count != null ? count : 0L;
    }

    public List<Map<String, Object>> getVehicleStatusStats() {
        return statisticRepository.getVehicleStatusStats();
    }

    public List<Map<String, Object>> getMonthlyRevenueStats() {
        return statisticRepository.getMonthlyRevenueStats();
    }
}