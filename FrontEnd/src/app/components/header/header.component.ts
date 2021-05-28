import { Component, OnInit } from '@angular/core';
import { CitizenService } from 'src/app/service/citizen.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public service: CitizenService) { }

  ngOnInit(): void {
  }

}
