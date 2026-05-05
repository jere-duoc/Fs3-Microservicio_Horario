package DuocQuin.Horarios.controller;

import DuocQuin.Horarios.model.Horario;
import DuocQuin.Horarios.service.HorarioService;
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
}
