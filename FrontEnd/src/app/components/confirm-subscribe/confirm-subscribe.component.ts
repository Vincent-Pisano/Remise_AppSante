import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Citizen } from 'src/app/models/citizen';
import { CitizenService } from 'src/app/service/citizen.service';

@Component({
  selector: 'app-confirm-subscribe',
  templateUrl: './confirm-subscribe.component.html',
  styleUrls: ['./confirm-subscribe.component.css']
})
export class ConfirmSubscribeComponent implements OnInit {

  validMessage:string = "";
  citizen:Citizen;
  flag:boolean;

  confirmSubscribeForm;

  constructor(private service: CitizenService, private router:Router) { }

  ngOnInit(): void {
    this.citizen = history.state;

    this.confirmSubscribeForm = new FormGroup({
      nas: new FormControl(this.citizen.nas, [Validators.required, Validators.minLength(7)]),
      password: new FormControl(this.citizen.password, [Validators.required, Validators.minLength(4)]),
      lastName: new FormControl(this.citizen.lastName, Validators.required),
      firstName: new FormControl(this.citizen.firstName, Validators.required),
      birthDate: new FormControl(this.citizen.birthDate, Validators.required),
      email: new FormControl(this.citizen.email, [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]),
      sex: new FormControl(this.citizen.sex, Validators.required),
      city: new FormControl(this.citizen.city, Validators.required),
      address: new FormControl(this.citizen.address, Validators.required),
      postalCode: new FormControl(this.citizen.postalCode, Validators.required),
      phoneNbr: new FormControl(this.citizen.phoneNbr, Validators.required),
    });
  }

  onSubmit(): void {
    if (this.confirmSubscribeForm.valid)
    {
      this.citizen = this.confirmSubscribeForm.value;
      this.service.createAccount(this.citizen).subscribe(
        (data) => {
          console.log(data)
          if (data == "SUCCESS")
          {
            sessionStorage.setItem('email', this.citizen.email)
            this.confirmSubscribeForm.reset();
            this.router.navigateByUrl('/dashboard', {state: this.citizen});
          }
          else {
            this.validMessage = data;
            
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

  get formConfirmeSubscribe()
  {
    return this.confirmSubscribeForm.controls;
  }

}
