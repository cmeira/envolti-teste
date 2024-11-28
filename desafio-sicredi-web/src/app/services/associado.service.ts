import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Associado } from '../models/associado';

@Injectable({
  providedIn: 'root',
})
export class AssociadoService {
  private apiUrl = 'http://localhost:8080/api/associados'; // Atualize com a URL da API

  constructor(private http: HttpClient) {}

  listarAssociados(): Observable<Associado[]> {
    return this.http.get<Associado[]>(this.apiUrl);
  }

  cadastrarAssociado(associado: Associado): Observable<Associado> {
    return this.http.post<Associado>(this.apiUrl, associado);
  }
}
