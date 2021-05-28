import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Citizen } from 'src/app/models/citizen';
import { CitizenService } from 'src/app/service/citizen.service';

@Component({
  selector: 'app-subscribe-minor',
  templateUrl: './subscribe-minor.component.html',
  styleUrls: ['./subscribe-minor.component.css']
})
export class SubscribeMinorComponent implements OnInit {

  validMessage:string = "";
  citizenParent:Citizen = new Citizen();
  citizenMinor:Citizen = new Citizen();

  subscribeMinorForm = new FormGroup({
    nasParent: new FormControl("", [Validators.required, Validators.minLength(7)]),
    passwordParent: new FormControl("", [Validators.required, Validators.minLength(4)]),
    lastNameParent: new FormControl("", Validators.required),
    firstNameParent: new FormControl("", Validators.required),
    birthDateParent: new FormControl("", Validators.required),
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
    if (this.subscribeMinorForm.valid)
    {
      this.setCitizen();
      console.log(this.citizenParent);
      console.log(this.citizenMinor);
      this.service.checkCitizenValidity(this.citizenParent).subscribe(
        (dataParent) => {
          if (dataParent != null)
          {
            this.service.checkCitizenValidity(this.citizenMinor).subscribe(
              (dataMinor) => {
                if (dataMinor != null)
                {
                  this.subscribeMinorForm.reset();
                  this.router.navigateByUrl('/confirm-subscribe-minor', {state: [dataParent, dataMinor]});
                }
                else {
                  this.validMessage = "Données du Parent invalides !";
                }
              }, (err) => {
                console.log(err)
              }
            )
          }
          else {
            this.validMessage = "Données du Parent invalides !";
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

  setCitizen():void {
    this.citizenParent.nas = this.subscribeMinorForm.get("nasParent").value;
    this.citizenParent.password = this.subscribeMinorForm.get("passwordParent").value;
    this.citizenParent.birthDate = this.subscribeMinorForm.get("birthDateParent").value;
    this.citizenParent.lastName = this.subscribeMinorForm.get("lastNameParent").value;
    this.citizenParent.firstName = this.subscribeMinorForm.get("firstNameParent").value;
    this.citizenMinor.nas = this.subscribeMinorForm.get("nas").value;
    this.citizenMinor.password = this.subscribeMinorForm.get("password").value;
    this.citizenMinor.birthDate = this.subscribeMinorForm.get("birthDate").value;
    this.citizenMinor.lastName = this.subscribeMinorForm.get("lastName").value;
    this.citizenMinor.firstName = this.subscribeMinorForm.get("firstName").value;
  }

  get formSubscribeMinor()
  {
    return this.subscribeMinorForm.controls;
  }

}
