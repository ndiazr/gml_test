import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Client } from '../models/client.model';

const baseUrl = 'http://localhost:8080/clients';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(baseUrl);
  }

  get(id: number): Observable<Client> {
    return this.http.get<Client>(`${baseUrl}/${id}`);
  }

  create(data: Client): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  update(data: Client): Observable<any> {
    return this.http.put(`${baseUrl}/${data.id}`, data);
  }

  delete(id?: number): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(baseUrl);
  }

  findBySharedKey(sharedKey: string): Observable<Client[]> {
    return this.http.get<Client[]>(`${baseUrl}/${sharedKey}/sharedKey`);
  }
}
