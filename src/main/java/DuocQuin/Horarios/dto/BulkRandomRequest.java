package DuocQuin.Horarios.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class BulkRandomRequest {
    private List<Long> idUsuarios;
    private List<Long> idSalas;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean excluirFinesSemana;
}
