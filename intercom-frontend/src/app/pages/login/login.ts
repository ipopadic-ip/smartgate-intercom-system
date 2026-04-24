import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private router: Router) {}

  login(): void {
    localStorage.setItem('loggedIn', 'true');
    this.router.navigate(['/dashboard']);
  }
}