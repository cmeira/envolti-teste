import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Voto } from '../models/voto';

@Injectable({
  providedIn: 'root',
})
export class VotacaoService {
  private apiUrl = 'http://localhost:8080/api/votos'; // Atualize para a URL da sua API

  constructor(private http: HttpClient) {}

  registrarVoto(pautaId: number, voto: Voto): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${pautaId}`, voto);
  }
}
