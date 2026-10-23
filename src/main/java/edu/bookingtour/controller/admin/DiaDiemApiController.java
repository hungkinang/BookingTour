package edu.bookingtour.controller.admin;

import edu.bookingtour.entity.DiemDen;
import edu.bookingtour.entity.NoiLuuTru;
import edu.bookingtour.repo.DiemDenRepository;
import edu.bookingtour.repo.NoiLuuTruRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dia-diem")
public class DiaDiemApiController {

    @Autowired
    private DiemDenRepository diemDenRepository;

    @Autowired
    private NoiLuuTruRepository noiLuuTruRepository;

    @GetMapping("/quoc-gia")
    public List<String> getQuocGia(@RequestParam String chauLuc) {
        return diemDenRepository.findDistinctQuocGiaByChauLuc(chauLuc);
    }

    @GetMapping("/thanh-pho")
    public List<DiemDen> getThanhPho(@RequestParam String quocGia) {
        return diemDenRepository.findByQuocGia(quocGia);
    }

    @GetMapping("/noi-luu-tru")
    public List<NoiLuuTru> getNoiLuuTru(@RequestParam Integer diemDenId) {
        return noiLuuTruRepository.findByDiemDenId(diemDenId);
    }
}

