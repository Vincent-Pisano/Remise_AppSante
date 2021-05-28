import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Citizen } from 'src/app/models/citizen';
import { CitizenService } from 'src/app/service/citizen.service';

@Component({
  selector: 'app-confirm-subscribe-minor',
  templateUrl: './confirm-subscribe-minor.component.html',
  styleUrls: ['./confirm-subscribe-minor.component.css']
})
export class ConfirmSubscribeMinorComponent implements OnInit {

  validMessage:string = "";
  citizenParent:Citizen = new Citizen();
  citizenMinor:Citizen = new Citizen();

  confirmSubscribeMinorForm:FormGroup;

  constructor(private service: CitizenService, private router:Router) { }

  ngOnInit(): void {
    var citizens: Array<Citizen> = history.state;
    this.citizenParent = citizens[0];
    this.citizenMinor = citizens[1];

    this.setFormGroup()
  }

  onSubmit(): void {
    if (this.confirmSubscribeMinorForm.valid)
    {
      console.log(this.citizenParent);
      console.log(this.citizenMinor);
      this.service.createAccountMinor(this.citizenParent, this.citizenMinor).subscribe(
        (data) => {
          console.log(data)
          if (data == "SUCCESS")
          {
            sessionStorage.setItem('email', this.citizenMinor.email)
            this.confirmSubscribeMinorForm.reset();
            this.router.navigateByUrl('/dashboard', {state: this.citizenMinor});
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

  get formConfirmSubscribeMinor()
  {
    return this.confirmSubscribeMinorForm.controls;
  }

  setFormGroup()
  {
    this.confirmSubscribeMinorForm = new FormGroup({
      nasParent: new FormControl(this.citizenParent.nas, [Validators.required, Validators.minLength(7)]),
      passwordParent: new FormControl(this.citizenParent.password, [Validators.required, Validators.minLength(4)]),
      lastNameParent: new FormControl(this.citizenParent.lastName, Validators.required),
      firstNameParent: new FormControl(this.citizenParent.firstName, Validators.required),
      birthDateParent: new FormControl(this.citizenParent.birthDate, Validators.required),
      emailParent: new FormControl(this.citizenParent.email, [Validators.required, Validators.pattern("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,4}$")]),
      sexParent: new FormControl(this.citizenParent.sex, Validators.required),
      cityParent: new FormControl(this.citizenParent.city, Validators.required),
      addressParent: new FormControl(this.citizenParent.address, Validators.required),
      postalCodeParent: new FormControl(this.citizenParent.postalCode, Validators.required),
      phoneNbrParent: new FormControl(this.citizenParent.phoneNbr, Validators.required),
      nas: new FormControl(this.citizenMinor.nas, [Validators.required, Validators.minLength(7)]),
      password: new FormControl(this.citizenMinor.password, [Validators.required, Validators.minLength(4)]),
      lastName: new FormControl(this.citizenMinor.lastName, Validators.required),
      firstName: new FormControl(this.citizenMinor.firstName, Validators.required),
      birthDate: new FormControl(this.citizenMinor.lastName, Validators.required),
      email: new FormControl(this.citizenMinor.email, [Validators.required, Validators.pattern("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,4}$")]),
      sex: new FormControl(this.citizenMinor.sex, Validators.required),
      city: new FormControl(this.citizenMinor.city, Validators.required),
      address: new FormControl(this.citizenMinor.address, Validators.required),
      postalCode: new FormControl(this.citizenMinor.postalCode, Validators.required),
      phoneNbr: new FormControl(this.citizenMinor.phoneNbr, Validators.required)
    });
  }

}
