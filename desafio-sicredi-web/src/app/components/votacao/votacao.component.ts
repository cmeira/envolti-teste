import { Component, OnInit } from '@angular/core';
import { Associado } from '../../models/associado';
import { Voto } from '../../models/voto';
import { AssociadoService } from '../../services/associado.service';
import { VotacaoService } from '../../services/votacao.service';

@Component({
  selector: 'app-votacao',
  templateUrl: './votacao.component.html',
})
export class VotacaoComponent implements OnInit {
  pautaId: number = 0; // Atualize com a lógica para associar uma pauta
  associados: Associado[] = [];
  associadoSelecionado: number = 0;
  voto: 'Sim' | 'Não' = 'Sim';

  constructor(
    private votacaoService: VotacaoService,
    private associadoService: AssociadoService
  ) {}

  ngOnInit(): void {
    this.carregarAssociados();
  }

  carregarAssociados() {
    this.associadoService.listarAssociados().subscribe((data) => {
      this.associados = data;
    });
  }

  registrarVoto() {
    const voto: Voto = {
      associadoId: this.associadoSelecionado,
      voto: this.voto,
    };

    this.votacaoService.registrarVoto(this.pautaId, voto).subscribe(() => {
      alert('Voto registrado com sucesso!');
    });
  }
}
