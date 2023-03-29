package com.dbserver.desafiovotacao.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Voto {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Associado associado;
    @CreationTimestamp
    private LocalDateTime dataHoraVoto;
    private String voto;
    @ManyToOne
    private Pauta pauta;

    public Voto(Associado associado, String voto, Pauta pauta) {
        this.associado = associado;
        this.voto = voto;
        this.pauta = pauta;
    }

    public boolean getVotoSim() {
        return Objects.equals(voto, "Sim");
    }

    public boolean getVotoNao() {
        return Objects.equals(voto, "Não");
    }
}
