import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Client } from '../models/client.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  readonly baseUrl = 'http://localhost:8080/clients';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(this.baseUrl);
  }

  get(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.baseUrl}/${id}`);
  }

  create(data: Client): Observable<any> {
    return this.http.post(this.baseUrl, data);
  }

  update(data: Client): Observable<any> {
    return this.http.put(`${this.baseUrl}/${data.id}`, data);
  }

  delete(id?: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  findBySharedKey(sharedKey: string): Observable<Client[]> {
    return this.http.get<Client[]>(`${this.baseUrl}/${sharedKey}/sharedKey`);
  }

  downloadCsv() {
    return this.http.get<any>(`${this.baseUrl}/csv`)
  }
}
