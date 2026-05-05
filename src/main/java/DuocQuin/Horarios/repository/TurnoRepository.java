package DuocQuin.Horarios.repository;

import DuocQuin.Horarios.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    Optional<Turno> findByNombreTurno(String nombreTurno);

    boolean existsByNombreTurno(String nombreTurno);
}
