package DuocQuin.Horarios.service;

import DuocQuin.Horarios.model.Horario;
import DuocQuin.Horarios.repository.HorarioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HorarioService {
    
    private static final Logger logger = LoggerFactory.getLogger(HorarioService.class);
    private static final String CIRCUIT_BREAKER_NAME = "horarioService";
    
    @Autowired
    private HorarioRepository horarioRepository;
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findAllFallback")
    public List<Horario> findAll() {
        logger.info("Obteniendo todos los horarios");
        return horarioRepository.findAll();
    }
    
    public List<Horario> findAllFallback(Exception e) {
        logger.error("Circuit breaker activado para findAll: {}", e.getMessage());
        return List.of();
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findByIdFallback")
    public Optional<Horario> findById(Long id) {
        logger.info("Obteniendo horario con ID: {}", id);
        return horarioRepository.findById(id);
    }
    
    public Optional<Horario> findByIdFallback(Long id, Exception e) {
        logger.error("Circuit breaker activado para findById: {}", e.getMessage());
        return Optional.empty();
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "saveFallback")
    public Horario save(Horario horario) {
        logger.info("Guardando nuevo horario");
        return horarioRepository.save(horario);
    }
    
    public Horario saveFallback(Horario horario, Exception e) {
        logger.error("Circuit breaker activado para save: {}", e.getMessage());
        throw new RuntimeException("Servicio no disponible en este momento", e);
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "updateFallback")
    public Horario update(Long id, Horario horarioDetails) {
        logger.info("Actualizando horario con ID: {}", id);
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));
        
        horario.setFecha(horarioDetails.getFecha());
        horario.setHoraEntrada(horarioDetails.getHoraEntrada());
        horario.setHoraSalida(horarioDetails.getHoraSalida());
        horario.setHorasExtra(horarioDetails.getHorasExtra());
        horario.setTurno(horarioDetails.getTurno());
        horario.setIdUsuario(horarioDetails.getIdUsuario());
        horario.setIdSala(horarioDetails.getIdSala());
        
        return horarioRepository.save(horario);
    }
    
    public Horario updateFallback(Long id, Horario horarioDetails, Exception e) {
        logger.error("Circuit breaker activado para update: {}", e.getMessage());
        throw new RuntimeException("Servicio no disponible en este momento", e);
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "deleteByIdFallback")
    public void deleteById(Long id) {
        logger.info("Eliminando horario con ID: {}", id);
        if (!horarioRepository.existsById(id)) {
            throw new RuntimeException("Horario no encontrado con ID: " + id);
        }
        horarioRepository.deleteById(id);
    }
    
    public void deleteByIdFallback(Long id, Exception e) {
        logger.error("Circuit breaker activado para deleteById: {}", e.getMessage());
        throw new RuntimeException("Servicio no disponible en este momento", e);
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findByUsuarioIdFallback")
    public List<Horario> findByUsuarioId(Long idUsuario) {
        logger.info("Obteniendo horarios para el usuario: {}", idUsuario);
        return horarioRepository.findByUsuarioIdUsuario(idUsuario);
    }
    
    public List<Horario> findByUsuarioIdFallback(Long idUsuario, Exception e) {
        logger.error("Circuit breaker activado para findByUsuarioId: {}", e.getMessage());
        return List.of();
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findByTurnoIdFallback")
    public List<Horario> findByTurnoId(Long idTurno) {
        logger.info("Obteniendo horarios para el turno: {}", idTurno);
        return horarioRepository.findByTurnoIdTurno(idTurno);
    }
    
    public List<Horario> findByTurnoIdFallback(Long idTurno, Exception e) {
        logger.error("Circuit breaker activado para findByTurnoId: {}", e.getMessage());
        return List.of();
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findBySalaIdFallback")
    public List<Horario> findBySalaId(Long idSala) {
        logger.info("Obteniendo horarios para la sala: {}", idSala);
        return horarioRepository.findBySalaIdSala(idSala);
    }
    
    public List<Horario> findBySalaIdFallback(Long idSala, Exception e) {
        logger.error("Circuit breaker activado para findBySalaId: {}", e.getMessage());
        return List.of();
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findByFechaFallback")
    public List<Horario> findByFecha(LocalDate fecha) {
        logger.info("Obteniendo horarios para la fecha: {}", fecha);
        return horarioRepository.findByFecha(fecha);
    }
    
    public List<Horario> findByFechaFallback(LocalDate fecha, Exception e) {
        logger.error("Circuit breaker activado para findByFecha: {}", e.getMessage());
        return List.of();
    }
    
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findByFechaBetweenFallback")
    public List<Horario> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        logger.info("Obteniendo horarios entre fechas: {} y {}", fechaInicio, fechaFin);
        return horarioRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
    
    public List<Horario> findByFechaBetweenFallback(LocalDate fechaInicio, LocalDate fechaFin, Exception e) {
        logger.error("Circuit breaker activado para findByFechaBetween: {}", e.getMessage());
        return List.of();
    }
}
