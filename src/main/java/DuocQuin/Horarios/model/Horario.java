package DuocQuin.Horarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "horarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Horario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long idHorario;
    
    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    
    @NotNull(message = "La hora de entrada es obligatoria")
    @Column(name = "hora_entrada", nullable = false)
    private LocalTime horaEntrada;
    
    @NotNull(message = "La hora de salida es obligatoria")
    @Column(name = "hora_salida", nullable = false)
    private LocalTime horaSalida;
    
    @Column(name = "horas_extra")
    private Integer horasExtra;
    
    @NotNull(message = "El turno es obligatorio")
    @ManyToOne
    @JoinColumn(name = "id_turno", nullable = false)
    private Turno turno;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;
    
    @NotNull(message = "El ID de la sala es obligatorio")
    @Column(name = "id_sala", nullable = false)
    private Long idSala;
}
