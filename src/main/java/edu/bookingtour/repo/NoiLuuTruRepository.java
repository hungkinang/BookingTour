package edu.bookingtour.repo;

import edu.bookingtour.entity.NoiLuuTru;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoiLuuTruRepository extends JpaRepository<NoiLuuTru, Integer> {
    @Query("SELECT n FROM NoiLuuTru n WHERE n.idDiemDen.id = :diemDenId")
    List<NoiLuuTru> findByDiemDenId(@Param("diemDenId") Integer diemDenId);
}
