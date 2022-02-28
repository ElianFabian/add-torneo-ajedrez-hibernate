package entidades;

import javax.persistence.*;
import java.time.Instant;

@Entity @Table(name = "partidas")
public class Partida {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "BLANCAS")
    private Jugador blancas;

    @ManyToOne
    @JoinColumn(name = "NEGRAS")
    private Jugador negras;

    @Lob @Column(name = "GANAN") private String ganan;

    @Column(name = "INICIO") private Instant inicio;

    @Column(name = "FIN") private Instant fin;

    public Instant getFin() {
        return fin;
    }
    public void setFin(Instant fin) {
        this.fin = fin;
    }

    public Instant getInicio() {
        return inicio;
    }
    public void setInicio(Instant inicio) {
        this.inicio = inicio;
    }

    public String getGanan() {
        return ganan;
    }
    public void setGanan(String ganan) {
        this.ganan = ganan;
    }

    public Jugador getNegras() {
        return negras;
    }
    public void setNegras(Jugador negras) {
        this.negras = negras;
    }

    public Jugador getBlancas() {
        return blancas;
    }
    public void setBlancas(Jugador blancas) {
        this.blancas = blancas;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override public String toString() {
        return "Partida{" +
                "id=" + id +
                ", blancas=" + blancas +
                ", negras=" + negras +
                ", ganan='" + ganan + '\'' +
                ", inicio=" + inicio +
                ", fin=" + fin +
                '}';
    }
}