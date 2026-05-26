import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActionLog } from '../models/action-log';

@Injectable({
  providedIn: 'root',
})
export class ActionLogService {

  constructor(private http: HttpClient) { }

  findAll() {
    return this.http.get<ActionLog[]>('http://localhost:8080/api/user/admin');
  }
}
