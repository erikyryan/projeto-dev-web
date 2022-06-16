package asimo.v.entities;

import asimo.v.entities.enums.TipoIngresso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ingresso")
public class Ingresso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String assento;

    private TipoIngresso tipo;

    private Long valor;

    @ManyToOne
    @JoinColumn(name="idsessao",referencedColumnName = "id")
    private Sessao sessao;

    @OneToOne
    @JoinColumn(name="idvenda", referencedColumnName = "id")
    private Venda venda;
}
