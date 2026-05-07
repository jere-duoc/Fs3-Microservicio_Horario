package DuocQuin.Horarios.repository;

import DuocQuin.Horarios.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    
    List<Horario> findByIdUsuario(Long idUsuario);
    
    List<Horario> findByTurnoIdTurno(Long idTurno);
    
    List<Horario> findByIdSala(Long idSala);
    
    List<Horario> findByFecha(LocalDate fecha);
    
    @Query("SELECT h FROM Horario h WHERE h.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Horario> findByFechaBetween(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT h FROM Horario h WHERE h.idUsuario = :idUsuario AND h.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Horario> findByIdUsuarioAndFechaBetween(@Param("idUsuario") Long idUsuario, 
                                                @Param("fechaInicio") LocalDate fechaInicio, 
                                                @Param("fechaFin") LocalDate fechaFin);
}

