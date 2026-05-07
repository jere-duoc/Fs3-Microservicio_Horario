package DuocQuin.Horarios.controller;

import DuocQuin.Horarios.model.Horario;
import DuocQuin.Horarios.service.HorarioService;
import DuocQuin.Horarios.dto.BulkRandomRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*")
public class HorarioController {
    
    @Autowired
    private HorarioService horarioService;
    
    @GetMapping
    public ResponseEntity<List<Horario>> getAllHorarios() {
        List<Horario> horarios = horarioService.findAll();
        return ResponseEntity.ok(horarios);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Horario> getHorarioById(@PathVariable Long id) {
        Optional<Horario> horario = horarioService.findById(id);
        return horario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Horario> createHorario(@Valid @RequestBody Horario horario) {
        try {
            Horario nuevoHorario = horarioService.save(horario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHorario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Horario> updateHorario(@PathVariable Long id, @Valid @RequestBody Horario horarioDetails) {
        try {
            Horario horarioActualizado = horarioService.update(id, horarioDetails);
            return ResponseEntity.ok(horarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHorario(@PathVariable Long id) {
        try {
            horarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Horario>> getHorariosByIdUsuario(@PathVariable Long idUsuario) {
        List<Horario> horarios = horarioService.findByUsuarioId(idUsuario);
        return ResponseEntity.ok(horarios);
    }
    
    @GetMapping("/turno/{idTurno}")
    public ResponseEntity<List<Horario>> getHorariosByIdTurno(@PathVariable Long idTurno) {
        List<Horario> horarios = horarioService.findByTurnoId(idTurno);
        return ResponseEntity.ok(horarios);
    }
    
    @GetMapping("/sala/{idSala}")
    public ResponseEntity<List<Horario>> getHorariosByIdSala(@PathVariable Long idSala) {
        List<Horario> horarios = horarioService.findBySalaId(idSala);
        return ResponseEntity.ok(horarios);
    }
    
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Horario>> getHorariosByFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Horario> horarios = horarioService.findByFecha(fecha);
        return ResponseEntity.ok(horarios);
    }
    
    @GetMapping("/fecha-rango")
    public ResponseEntity<List<Horario>> getHorariosByFechaBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Horario> horarios = horarioService.findByFechaBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(horarios);
    }
    
    // Endpoints para administración de horarios
    
    @PostMapping("/generar-aleatorios")
    public ResponseEntity<?> generarHorariosAleatorios(@RequestBody BulkRandomRequest request) {
        try {
            List<Horario> horarios = horarioService.generarHorariosAleatorios(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(horarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PostMapping("/generar-por-turno")
    public ResponseEntity<?> generarHorariosPorTurno(@RequestBody GenerarHorariosRequest request) {
        try {
            List<Horario> horarios = horarioService.generarHorariosPorTurno(
                request.getIdTurno(),
                request.getIdUsuarios(),
                request.getIdSalas(),
                request.getFechaInicio(),
                request.getFechaFin(),
                request.isExcluirFinesSemana()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(horarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PostMapping("/generar-semanal")
    public ResponseEntity<?> generarHorarioSemanal(@RequestBody GenerarHorarioSemanalRequest request) {
        try {
            List<Horario> horarios = horarioService.generarHorarioSemanal(
                request.getIdUsuario(),
                request.getIdTurno(),
                request.getIdSala(),
                request.getFechaInicio()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(horarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Endpoints para visualización por usuario
    
    @GetMapping("/usuario/{idUsuario}/semana")
    public ResponseEntity<List<Horario>> getHorariosPorSemana(
            @PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Horario> horarios = horarioService.getHorariosPorSemana(idUsuario, fecha);
        return ResponseEntity.ok(horarios);
    }
    
    @GetMapping("/usuario/{idUsuario}/mes")
    public ResponseEntity<List<Horario>> getHorariosPorMes(
            @PathVariable Long idUsuario,
            @RequestParam int anio,
            @RequestParam int mes) {
        List<Horario> horarios = horarioService.getHorariosPorMes(idUsuario, anio, mes);
        return ResponseEntity.ok(horarios);
    }
    
    // DTOs para las peticiones
    public static class GenerarHorariosRequest {
        private Long idTurno;
        private List<Long> idUsuarios;
        private List<Long> idSalas;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private boolean excluirFinesSemana = true;
        
        // Getters y Setters
        public Long getIdTurno() { return idTurno; }
        public void setIdTurno(Long idTurno) { this.idTurno = idTurno; }
        public List<Long> getIdUsuarios() { return idUsuarios; }
        public void setIdUsuarios(List<Long> idUsuarios) { this.idUsuarios = idUsuarios; }
        public List<Long> getIdSalas() { return idSalas; }
        public void setIdSalas(List<Long> idSalas) { this.idSalas = idSalas; }
        public LocalDate getFechaInicio() { return fechaInicio; }
        public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
        public LocalDate getFechaFin() { return fechaFin; }
        public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
        public boolean isExcluirFinesSemana() { return excluirFinesSemana; }
        public void setExcluirFinesSemana(boolean excluirFinesSemana) { this.excluirFinesSemana = excluirFinesSemana; }
    }
    
    public static class GenerarHorarioSemanalRequest {
        private Long idUsuario;
        private Long idTurno;
        private Long idSala;
        private LocalDate fechaInicio;
        
        // Getters y Setters
        public Long getIdUsuario() { return idUsuario; }
        public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
        public Long getIdTurno() { return idTurno; }
        public void setIdTurno(Long idTurno) { this.idTurno = idTurno; }
        public Long getIdSala() { return idSala; }
        public void setIdSala(Long idSala) { this.idSala = idSala; }
        public LocalDate getFechaInicio() { return fechaInicio; }
        public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    }
}
