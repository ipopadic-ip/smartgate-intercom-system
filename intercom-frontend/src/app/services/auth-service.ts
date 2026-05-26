import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

interface AuthResponse {
  token: string;
}

interface JwtPayload {
  sub: string;
  roles: string[];
  exp: number;
  stan?: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';

  private currentUserSubject = new BehaviorSubject<JwtPayload | null>(this.decodeToken());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem('access_token', response.token);
        this.currentUserSubject.next(this.decodeToken());
      })
    );
  }

  logout(): void {
    localStorage.removeItem('access_token');
    this.currentUserSubject.next(null);
  }
  getToken(): string | null {
    return typeof window !== 'undefined'
      ? localStorage.getItem('access_token')
      : null;
  }
  
  getStan() {
    const payload = this.decodeToken();
    return payload?.stan;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getUser(): JwtPayload | null {
    return this.currentUserSubject.value;
  }

  hasRole(role: string): boolean {
    const user = this.getUser();
    return !!user?.roles?.includes(role);
  }

  private decodeToken(): JwtPayload | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch {
      return null;
    }
  }
}