package edu.bookingtour.repo;

import edu.bookingtour.entity.NguoiDung;
import edu.bookingtour.entity.YeuThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<YeuThich,  Integer>
{
    List<YeuThich> findAll();
    List<YeuThich> findByIdNguoiDung(NguoiDung nguoiDung);

    boolean existsByIdNguoiDungAndIdChuyenDi(NguoiDung user, edu.bookingtour.entity.ChuyenDi tour);
}
