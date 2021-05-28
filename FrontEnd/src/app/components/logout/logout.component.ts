import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CitizenService } from 'src/app/service/citizen.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private serice: CitizenService, private route: Router){
    
  }

  ngOnInit(): void {
    this.serice.logout();
    this.route.navigate(['/home'])
  }

}
