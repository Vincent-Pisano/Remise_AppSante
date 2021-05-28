import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, CanDeactivate } from '@angular/router';
import { Observable } from 'rxjs';
import { CitizenService } from './citizen.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private service: CitizenService, private router: Router){}

  canActivate(
    route: ActivatedRouteSnapshot, state: RouterStateSnapshot){
      if (this.service.userIsLogIn()) 
      return true;
      this.router.navigate(['/home'])
    return false;
  }
  
}
