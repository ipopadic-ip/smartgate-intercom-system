import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { User } from '../../models/user';
import { UserService } from '../../services/user-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent implements OnInit {
  user: User = {
    id: 0,
    username: '',
    password: '',
    active: true ,
    apartment: null
  };

  update: boolean = false;

  constructor(private router: Router, private authService: AuthService, private route: ActivatedRoute, private userService: UserService) {}

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {

      this.update = params['update'] === 'true';

      if (this.update) {
        this.user.username = this.getUsernameFromToken() || '';
      }

    });

  }

  login(): void {
    this.authService.login(this.user).subscribe(data =>{
      this.router.navigate(['/dashboard']);
    });
  }

  updateUser(): void{
    this.user.id = this.getUserId() || 0;
    this.user.username = this.getUsernameFromToken() || '';

    this.userService.update(this.user).subscribe(data =>{
          this.router.navigate(['/dashboard']);
    });
  }

  getUserId(): number | null {
    const token = localStorage.getItem('access_token');

    if (!token) {
      return null;
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));

      return payload.id || null;

    } catch (error) {
      console.error('Neispravan token', error);
      return null;
    }
  }

  getUsernameFromToken(): string | null {

  const token = localStorage.getItem('access_token');

  if (!token) {
    return null;
  }

  try {

    const payload = JSON.parse(atob(token.split('.')[1]));

    return payload.sub || null;

  } catch(error) {

    return null;
  }
}
}