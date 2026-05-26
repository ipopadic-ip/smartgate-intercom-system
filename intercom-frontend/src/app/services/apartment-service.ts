import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Apartment } from '../models/apartment';

@Injectable({
  providedIn: 'root',
})
export class ApartmentService {

  constructor(private http: HttpClient) { }

  getApartments() {
    return this.http.get<Apartment[]>('http://localhost:8080/api/user/apartments');
  }
}
