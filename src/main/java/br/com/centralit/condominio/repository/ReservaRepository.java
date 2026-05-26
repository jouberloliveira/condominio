package br.com.centralit.condominio.repository;

import br.com.centralit.condominio.entity.Reserva;
import br.com.centralit.condominio.enums.AreaComum;
import br.com.centralit.condominio.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.area = :area AND r.status = :status " +
           "AND ((r.dataHoraInicio < :fim AND r.dataHoraFim > :inicio))")
    List<Reserva> findConflitos(@Param("area") AreaComum area,
                                @Param("status") StatusReserva status,
                                @Param("inicio") LocalDateTime inicio,
                                @Param("fim") LocalDateTime fim);

}
