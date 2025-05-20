package com.uniquindio.redsocial.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversacionDTO {
    private Long id;
    private List<UsuarioDTO> participantes;
}

