import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { 
    let t = localStorage.getItem("token");
    if(t){
      this.token = t;
    }
  }

  token: any = null;

  login(user: any){
    return this.http.post<any>("http://localhost:3000/login", user).pipe(
      tap(x => {
        this.token = x["token"];
        localStorage.setItem("token", this.token);
        }
      ));
  }

  getUser(){
    if(this.token){
      let payload = this.token.split(".")[1];
      return JSON.parse(atob(payload));
    }
  }

  getRoles(){
    let user = this.getUser();
    if(user){
      return user.roles;
    }

  }

  validateRoles(requiredRoles: string[]): boolean {
    if (!requiredRoles || requiredRoles.length === 0) return true; // Ako nije tražena nijedna rola, pusti
  
    const user = this.getUser();
    if (!user || !user.roles) return false;
  
    return requiredRoles.some(role => user.roles.includes(role));
  }
  
  
}
