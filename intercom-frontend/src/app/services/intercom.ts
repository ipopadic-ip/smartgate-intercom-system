import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IntercomEvent } from '../models/intercom-event'

@Injectable({
  providedIn: 'root'
})
export class IntercomService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  openGate() {
    return this.http.post(`${this.apiUrl}/open`, {}, { responseType: 'text' });
  }

  getLatestEvent() {
    return this.http.get<IntercomEvent>(`${this.apiUrl}/intercom/latest`);
  }
}