import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { Apartment } from '../../models/apartment';
import { ApartmentService } from '../../services/apartment-service';
import { UserService } from '../../services/user-service';
import { MatCardModule ,MatCard } from "@angular/material/card";
import { ReactiveFormsModule } from '@angular/forms';
import {MatDividerModule} from '@angular/material/divider';
import {MatListModule} from '@angular/material/list';
import { User } from '../../models/user';


@Component({
  selector: 'app-user-list-component',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCardModule,
    MatDividerModule,
    MatListModule,
    MatCard
  ],
  templateUrl: './user-list-component.html',
  styleUrl: './user-list-component.css',
})
export class UserListComponent implements OnInit {

  users: User[] = [];

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.findAll().subscribe(data => {
      this.users = data;
    });
  }

  activateUser(id: number) {
    this.userService.activateUser(id).subscribe(() => {
      this.users = this.users.map(user => {
        if (user.id === id) {
          return { ...user, active: true };
        }
        return user;
      });
    });
  }

  deactivateUser(id: number) {
    this.userService.deactivateUser(id).subscribe(() => {
      this.users = this.users.map(user => {
        if (user.id === id) {
          return { ...user, active: false };
        }
        return user;
      });
    });
  }

  loadUsers() {
  this.userService.findAll().subscribe(data => {
    this.users = data;
  });
}
}
