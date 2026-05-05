package DuocQuin.Horarios.service;

import DuocQuin.Horarios.model.Turno;
import DuocQuin.Horarios.repository.TurnoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TurnoService {

    private static final Logger logger = LoggerFactory.getLogger(TurnoService.class);
    private static final String CIRCUIT_BREAKER_NAME = "turnoService";

    @Autowired
    private TurnoRepository turnoRepository;

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findAllFallback")
    public List<Turno> findAll() {
        logger.info("Obteniendo todos los turnos");
        return turnoRepository.findAll();
    }

    public List<Turno> findAllFallback(Exception e) {
        logger.error("Circuit breaker activado para findAll: {}", e.getMessage());
        return List.of();
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findByIdFallback")
    public Optional<Turno> findById(Long id) {
        logger.info("Obteniendo turno con ID: {}", id);
        return turnoRepository.findById(id);
    }

    public Optional<Turno> findByIdFallback(Long id, Exception e) {
        logger.error("Circuit breaker activado para findById: {}", e.getMessage());
        return Optional.empty();
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "saveFallback")
    public Turno save(Turno turno) {
        if (turnoRepository.existsByNombreTurno(turno.getNombreTurno())) {
            throw new RuntimeException("Ya existe un turno con el nombre: " + turno.getNombreTurno());
        }
        logger.info("Guardando nuevo turno: {}", turno.getNombreTurno());
        return turnoRepository.save(turno);
    }

    public Turno saveFallback(Turno turno, Exception e) {
        logger.error("Circuit breaker activado para save: {}", e.getMessage());
        throw new RuntimeException("Servicio no disponible en este momento", e);
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "updateFallback")
    public Turno update(Long id, Turno turnoDetails) {
        logger.info("Actualizando turno con ID: {}", id);
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + id));

        if (!turno.getNombreTurno().equals(turnoDetails.getNombreTurno()) &&
                turnoRepository.existsByNombreTurno(turnoDetails.getNombreTurno())) {
            throw new RuntimeException("Ya existe un turno con el nombre: " + turnoDetails.getNombreTurno());
        }

        turno.setNombreTurno(turnoDetails.getNombreTurno());
        return turnoRepository.save(turno);
    }

    public Turno updateFallback(Long id, Turno turnoDetails, Exception e) {
        logger.error("Circuit breaker activado para update: {}", e.getMessage());
        throw new RuntimeException("Servicio no disponible en este momento", e);
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "deleteByIdFallback")
    public void deleteById(Long id) {
        logger.info("Eliminando turno con ID: {}", id);
        if (!turnoRepository.existsById(id)) {
            throw new RuntimeException("Turno no encontrado con ID: " + id);
        }
        turnoRepository.deleteById(id);
    }

    public void deleteByIdFallback(Long id, Exception e) {
        logger.error("Circuit breaker activado para deleteById: {}", e.getMessage());
        throw new RuntimeException("Servicio no disponible en este momento", e);
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findByNombreTurnoFallback")
    public Optional<Turno> findByNombreTurno(String nombreTurno) {
        logger.info("Obteniendo turno con nombre: {}", nombreTurno);
        return turnoRepository.findByNombreTurno(nombreTurno);
    }

    public Optional<Turno> findByNombreTurnoFallback(String nombreTurno, Exception e) {
        logger.error("Circuit breaker activado para findByNombreTurno: {}", e.getMessage());
        return Optional.empty();
    }
}
