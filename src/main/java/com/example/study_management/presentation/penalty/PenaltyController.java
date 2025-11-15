package com.example.study_management.presentation.penalty;

import com.example.study_management.presentation.penalty.dto.PenaltyResDto;
import com.example.study_management.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyService penaltyService;

    /** 시연용 지난 주 코테 벌금 계산 */
    @PostMapping("/penalties")
    public void createPenalty(){
        penaltyService.applyWeeklyPenalties();
    }

    @GetMapping("/study-groups/{id}/penalties")
    public List<PenaltyResDto> getPenalties(@PathVariable("id") Long studyGroupId){
        return penaltyService.getPenalties(studyGroupId);
    }
}
