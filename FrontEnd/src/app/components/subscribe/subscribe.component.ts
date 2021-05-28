import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Citizen } from 'src/app/models/citizen';
import { CitizenService } from 'src/app/service/citizen.service';

@Component({
  selector: 'app-subscribe',
  templateUrl: './subscribe.component.html',
  styleUrls: ['./subscribe.component.css']
})
export class SubscribeComponent implements OnInit {

  validMessage:string = "";
  citizen:Citizen = new Citizen();

  subscribeForm = new FormGroup({
    nas: new FormControl("", [Validators.required, Validators.minLength(7)]),
    password: new FormControl("", [Validators.required, Validators.minLength(4)]),
    lastName: new FormControl("", Validators.required),
    firstName: new FormControl("", Validators.required),
    birthDate: new FormControl("", Validators.required)
  });

  constructor(private service: CitizenService, private router:Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.subscribeForm.valid)
    {
      this.citizen = this.subscribeForm.value;
      this.service.checkCitizenValidity(this.citizen).subscribe(
        (data) => {
          if (data != null)
          {
            this.subscribeForm.reset();
            this.router.navigateByUrl('/confirm-subscribe', {state: data});
          }
          else {
            this.validMessage = "Données invalides !";
            
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

  get formSubscribe()
  {
    return this.subscribeForm.controls;
  }

}
