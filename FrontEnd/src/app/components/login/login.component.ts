import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Citizen } from 'src/app/models/citizen';
import { CitizenService } from 'src/app/service/citizen.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  validMessage:string = "";
  citizen:Citizen;
  flag:boolean;

  loginForm = new FormGroup({
    email: new FormControl("", [Validators.required, Validators.pattern("^[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]),
    password: new FormControl("", [Validators.required, Validators.minLength(4)])
  });

  constructor(private service: CitizenService, private router:Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.loginForm.valid)
    {
      this.service.login(this.loginForm.get("email").value, this.loginForm.get("password").value).subscribe(
        (data) => {
          this.citizen = data;
          if (this.citizen != null)
          {
            sessionStorage.setItem('email', this.loginForm.get("email").value)
            this.loginForm.reset();
            this.router.navigateByUrl('/dashboard', {state: this.citizen})
          }
          else{
            this.validMessage = "Invalid data !"
          }
        }, (err) => {
          console.log(err)
        }
      )
    }
    else{
      this.validMessage = "Please fill the form before submitting !"
    }
  }

  forgotPassword(): void {
    console.log("TEST");
    if (this.loginForm.get("email").valid)
    {
      this.service.getForgotPassword(this.loginForm.get("email").value).subscribe(
        (data) => {
          if (data)
          {
            this.validMessage = "Your password has been sent to your email"
          }
          else{
            this.validMessage = "No account exist with this email !"
          }
        }, (err) => {
          console.log(err)
        }
      )
    }
    else{
      this.validMessage = "Please give a valid email !"
    }
  }

  get formLogin()
  {
    return this.loginForm.controls;
  }

}
