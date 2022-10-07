import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css'],
})
export class CreateUserComponent implements OnInit {
  createUserForm: FormGroup;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) {
    this.authService.checkPermissions('can_create_users');
    this.createUserForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      can_read_users: [],
      can_create_users: [],
      can_update_users: [],
      can_delete_users: [],
    });
  }

  ngOnInit(): void {}

  createUser() {
    let permissions = [];
    if (this.createUserForm.get('can_read_users')?.value == true)
      permissions.push('can_read_users');
    if (this.createUserForm.get('can_create_users')?.value == true)
      permissions.push('can_create_users');
    if (this.createUserForm.get('can_update_users')?.value == true)
      permissions.push('can_update_users');
    if (this.createUserForm.get('can_delete_users')?.value == true)
      permissions.push('can_delete_users');
    let user = {
      firstName: this.createUserForm.get('firstName')?.value,
      lastName: this.createUserForm.get('lastName')?.value,
      email: this.createUserForm.get('email')?.value,
      password: this.createUserForm.get('password')?.value,
      permissions,
    };
    this.userService.createUser(user);
    this.createUserForm.reset();
  }
}
