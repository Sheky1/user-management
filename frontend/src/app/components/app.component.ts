import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'domaci3-front';

  constructor(private authService: AuthService) {}

  isUserLogged() {
    return this.authService.isLogged;
  }

  logout() {
    this.authService.logout();
  }

  hasPermission(permission: string) {
    return this.authService.hasPermission(permission);
  }
}
