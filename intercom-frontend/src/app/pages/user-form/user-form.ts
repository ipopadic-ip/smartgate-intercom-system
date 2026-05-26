import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { Apartment } from '../../models/apartment';
import { ApartmentService } from '../../services/apartment-service';
import { UserService } from '../../services/user-service';
import { MatCardModule ,MatCard } from "@angular/material/card";
import { Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-user-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCard,
    MatCardModule
],
  templateUrl: './user-form.html',
  styleUrl: './user-form.css',
})
export class UserForm implements OnInit {

  form!: FormGroup;

  apartments: Apartment[] = [];


  constructor(private fb: FormBuilder, private apartmentService: ApartmentService, private userService: UserService) {}
  ngOnInit(): void {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      apartment: this.fb.group({
        id: ['', Validators.required]
      })    
    });
    this.apartmentService.getApartments().subscribe(data => {
      this.apartments = data;
    });
  }

  submit() {
      if (this.form.valid) {

        console.log(this.form.value);
        this.userService.create(this.form.value).subscribe(data => {
          console.log(data);
          this.form.reset();
        });
      } 
    

  }

  

}
