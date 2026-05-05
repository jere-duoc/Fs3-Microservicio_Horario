package DuocQuin.Horarios.controller;

import DuocQuin.Horarios.model.Turno;
import DuocQuin.Horarios.service.TurnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/turnos")
@CrossOrigin(origins = "*")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    @GetMapping
    public ResponseEntity<List<Turno>> getAllTurnos() {
        List<Turno> turnos = turnoService.findAll();
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turno> getTurnoById(@PathVariable Long id) {
        Optional<Turno> turno = turnoService.findById(id);
        return turno.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Turno> createTurno(@Valid @RequestBody Turno turno) {
        try {
            Turno nuevoTurno = turnoService.save(turno);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTurno);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Turno> updateTurno(@PathVariable Long id, @Valid @RequestBody Turno turnoDetails) {
        try {
            Turno turnoActualizado = turnoService.update(id, turnoDetails);
            return ResponseEntity.ok(turnoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurno(@PathVariable Long id) {
        try {
            turnoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombreTurno}")
    public ResponseEntity<Turno> getTurnoByNombre(@PathVariable String nombreTurno) {
        Optional<Turno> turno = turnoService.findByNombreTurno(nombreTurno);
        return turno.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
