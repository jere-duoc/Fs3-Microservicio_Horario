package DuocQuin.Horarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Table(name = "turnos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long idTurno;

    @NotBlank(message = "El nombre del turno es obligatorio")
    @Column(name = "nombre_turno", nullable = false, length = 50)
    private String nombreTurno;
    
    @Column(name = "hora_inicio_default")
    private LocalTime horaInicioDefault;
    
    @Column(name = "hora_fin_default")
    private LocalTime horaFinDefault;
    
    // Constructor para crear turnos con horas predefinidas
    public Turno(String nombreTurno, LocalTime horaInicioDefault, LocalTime horaFinDefault) {
        this.nombreTurno = nombreTurno;
        this.horaInicioDefault = horaInicioDefault;
        this.horaFinDefault = horaFinDefault;
    }
}
