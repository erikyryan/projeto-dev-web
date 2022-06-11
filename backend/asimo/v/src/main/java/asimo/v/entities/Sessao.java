package asimo.v.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sessao")
public class Sessao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "dtsessao")
    private Date dataDaSessao;

    @OneToOne
    @JoinColumn(name="idlocal", referencedColumnName = "id")
    private Localizacao Local;

    @OneToOne
    @JoinColumn(name="idevento", referencedColumnName = "id")
    private Evento evento;

    @Column(name = "valorinteira")
    private Long valorInteira;

    @OneToMany(mappedBy="sessao", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    private List<Ingresso> ingressos;
}