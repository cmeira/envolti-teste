import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pauta } from '../models/pauta';

@Injectable({
  providedIn: 'root',
})
export class PautaService {
  private apiUrl = 'http://localhost:8080/api/pautas'; // Atualize com a URL da API

  constructor(private http: HttpClient) {}

  listarPautas(): Observable<Pauta[]> {
    return this.http.get<Pauta[]>(this.apiUrl);
  }

  cadastrarPauta(pauta: Pauta): Observable<Pauta> {
    return this.http.post<Pauta>(this.apiUrl, pauta);
  }
}
