import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Citizen } from 'src/app/models/citizen';
import { Permit } from 'src/app/models/permit';
import { CitizenService } from 'src/app/service/citizen.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  citizen:Citizen;
  permit:Permit = new Permit();
  
  flagHasPermit:boolean;
  renewMessage:string;
  srcImage:string;

  constructor(private service: CitizenService, private router:Router) { }

  ngOnInit(): void {

    this.citizen = history.state;
    this.flagHasPermit = false;

    this.setPermit();
  }

  renewPermit(): void {
    this.service.renewPermit(this.citizen).subscribe(
      (data) => {
        if (data == "SUCCESS")
        {
          this.setPermit();
          this.renewMessage = "Operation sucessful";
        }
        else {
          this.renewMessage = data
        }
      }, (err) => {
        console.log(err)
      }
    )
  }

  private setPermit()
  {
    this.service.getPermit(this.citizen.nas).subscribe(
      (data) => {
        if (data != null)
        {
          this.flagHasPermit = true;
          this.permit = data;
          this.srcImage = 'http://localhost:9090/appSante/qrCode/' + this.permit.idPermit;
        }
        else {
          this.flagHasPermit = false;
          this.permit = null;
          this.srcImage = '';
        }
      }, (err) => {
        console.log(err)
      }
    )
  }

  clearRenwMessage()
  {
    this.renewMessage = "Voulez vous renouveller votre permis ?";
  }
}
