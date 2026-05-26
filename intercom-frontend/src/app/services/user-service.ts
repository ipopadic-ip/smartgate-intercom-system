import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  constructor( private http: HttpClient) { }

  findAll() {
    return this.http.get<User[]>('http://localhost:8080/api/user/everyone');
  }

  create(user: User){
    return this.http.post<User>('http://localhost:8080/api/user/register', user);
  }

  update(user: User){
    return this.http.put<User>(`http://localhost:8080/api/user/update/${user.id}` , user);
  }

  activateUser(id: number){
    return this.http.put<User>(`http://localhost:8080/api/user/activate/${id}`, {});
  }
  deactivateUser(id: number){
    return this.http.put<User>(`http://localhost:8080/api/user/deactivate/${id}`, {});
  }
}
