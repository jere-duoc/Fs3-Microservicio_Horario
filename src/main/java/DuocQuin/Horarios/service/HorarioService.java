package DuocQuin.Horarios.service;

import DuocQuin.Horarios.model.Horario;
import DuocQuin.Horarios.repository.HorarioRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import DuocQuin.Horarios.dto.EventoHorarioDTO;

import DuocQuin.Horarios.dto.BulkRandomRequest;
import DuocQuin.Horarios.model.Turno;
import DuocQuin.Horarios.repository.TurnoRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
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

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final Random random = new Random();
    
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

        Horario horarioGuardado =
            horarioRepository.save(horario);

        
        //Aca empieza se cumple rabbit
        EventoHorarioDTO evento =
            new EventoHorarioDTO(
                horarioGuardado.getIdHorario(),
                horarioGuardado.getIdUsuario(),
                "Horario creado correctamente",
                null
            );

        rabbitTemplate.convertAndSend(
            "duocquin.exchange",
            "horario.creado",
            evento
        );

        return horarioGuardado;
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
        return horarioRepository.findByIdUsuario(idUsuario);
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
        return horarioRepository.findByIdSala(idSala);
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

    @Transactional
    public List<Horario> generarHorariosAleatorios(BulkRandomRequest request) {
        logger.info("Iniciando generación masiva aleatoria para {} usuarios", request.getIdUsuarios().size());
        List<Horario> nuevosHorarios = new ArrayList<>();
        List<Turno> turnosDisponibles = turnoRepository.findAll();
        
        if (turnosDisponibles.isEmpty()) {
            throw new RuntimeException("No hay turnos configurados en la base de datos");
        }

        for (Long idUsuario : request.getIdUsuarios()) {
            // Asignamos un turno fijo para este usuario en este rango para que sea coherente
            Turno turnoAsignado = turnosDisponibles.get(random.nextInt(turnosDisponibles.size()));
            Long idSalaAsignada = request.getIdSalas().get(random.nextInt(request.getIdSalas().size()));

            // Definimos horas según el turno
            LocalTime entrada, salida;
            switch (turnoAsignado.getNombreTurno()) {
                case "Diurno":
                    entrada = LocalTime.of(8, 0);
                    salida = LocalTime.of(16, 0);
                    break;
                case "Vespertino":
                    entrada = LocalTime.of(16, 0);
                    salida = LocalTime.of(0, 0);
                    break;
                case "Nocturno":
                default:
                    entrada = LocalTime.of(0, 0);
                    salida = LocalTime.of(8, 0);
                    break;
            }

            LocalDate actual = request.getFechaInicio();
            while (!actual.isAfter(request.getFechaFin())) {
                boolean esFinde = (actual.getDayOfWeek().getValue() >= 6);
                
                if (!request.isExcluirFinesSemana() || !esFinde) {
                    Horario h = new Horario();
                    h.setFecha(actual);
                    h.setHoraEntrada(entrada);
                    h.setHoraSalida(salida);
                    h.setTurno(turnoAsignado);
                    h.setIdUsuario(idUsuario);
                    h.setIdSala(idSalaAsignada);
                    h.setHorasExtra(0);
                    nuevosHorarios.add(h);
                }
                actual = actual.plusDays(1);
            }
        }

        return horarioRepository.saveAll(nuevosHorarios);
    }
    
    @Transactional
    public List<Horario> generarHorariosPorTurno(Long idTurno, List<Long> idUsuarios, List<Long> idSalas, 
                                                  LocalDate fechaInicio, LocalDate fechaFin, boolean excluirFinesSemana) {
        logger.info("Generando horarios para turno {} entre {} y {}", idTurno, fechaInicio, fechaFin);
        
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + idTurno));
        
        List<Horario> nuevosHorarios = new ArrayList<>();
        
        for (Long idUsuario : idUsuarios) {
            Long idSalaAsignada = idSalas.get(random.nextInt(idSalas.size()));
            
            LocalDate actual = fechaInicio;
            while (!actual.isAfter(fechaFin)) {
                boolean esFinde = (actual.getDayOfWeek().getValue() >= 6);
                
                if (!excluirFinesSemana || !esFinde) {
                    Horario horario = new Horario();
                    horario.setFecha(actual);
                    horario.setHoraEntrada(turno.getHoraInicioDefault());
                    horario.setHoraSalida(turno.getHoraFinDefault());
                    horario.setTurno(turno);
                    horario.setIdUsuario(idUsuario);
                    horario.setIdSala(idSalaAsignada);
                    horario.setHorasExtra(0);
                    nuevosHorarios.add(horario);
                }
                actual = actual.plusDays(1);
            }
        }
        
        return horarioRepository.saveAll(nuevosHorarios);
    }
    
    @Transactional
    public List<Horario> generarHorarioSemanal(Long idUsuario, Long idTurno, Long idSala, LocalDate fechaInicio) {
        logger.info("Generando horario semanal para usuario {} desde {}", idUsuario, fechaInicio);
        
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + idTurno));
        
        List<Horario> horariosSemana = new ArrayList<>();
        LocalDate finSemana = fechaInicio.plusDays(6); // Lunes a Domingo
        
        LocalDate actual = fechaInicio;
        while (!actual.isAfter(finSemana)) {
            boolean esFinde = (actual.getDayOfWeek().getValue() >= 6);
            
            if (!esFinde) { // Solo días de semana
                Horario horario = new Horario();
                horario.setFecha(actual);
                horario.setHoraEntrada(turno.getHoraInicioDefault());
                horario.setHoraSalida(turno.getHoraFinDefault());
                horario.setTurno(turno);
                horario.setIdUsuario(idUsuario);
                horario.setIdSala(idSala);
                horario.setHorasExtra(0);
                horariosSemana.add(horario);
            }
            actual = actual.plusDays(1);
        }
        
        return horarioRepository.saveAll(horariosSemana);
    }
    
    public List<Horario> getHorariosPorSemana(Long idUsuario, LocalDate fecha) {
        // Obtener el lunes de la semana de la fecha proporcionada
        LocalDate lunes = fecha.minusDays(fecha.getDayOfWeek().getValue() - 1);
        LocalDate domingo = lunes.plusDays(6);
        
        logger.info("Obteniendo horarios para usuario {} en la semana del {} al {}", idUsuario, lunes, domingo);
        return horarioRepository.findByIdUsuarioAndFechaBetween(idUsuario, lunes, domingo);
    }
    
    public List<Horario> getHorariosPorMes(Long idUsuario, int anio, int mes) {
        LocalDate primerDia = LocalDate.of(anio, mes, 1);
        LocalDate ultimoDia = primerDia.withDayOfMonth(primerDia.lengthOfMonth());
        
        logger.info("Obteniendo horarios para usuario {} en {}/{}", idUsuario, mes, anio);
        return horarioRepository.findByIdUsuarioAndFechaBetween(idUsuario, primerDia, ultimoDia);
    }
}
