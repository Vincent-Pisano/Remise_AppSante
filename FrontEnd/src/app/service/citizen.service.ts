import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Citizen } from '../models/citizen';
import { GenericService } from './genericService';
import { Observable } from 'rxjs';
import { Permit } from '../models/permit';

@Injectable({
  providedIn: 'root',
})
export class CitizenService extends GenericService<Citizen, Number> {
  constructor(http: HttpClient) {
    super(http, 'http://localhost:9090/appSante');
  }

  checkCitizenValidity(citizen: Citizen): Observable<Citizen> {
    return this.http.post<Citizen>(this.url + '/isCitizenValid', citizen);
  }

  createAccount(citizen: Citizen): Observable<string> {
    return this.http.post<string>(this.url + '/createAccount', citizen, {
      responseType: 'text' as 'json',
    });
  }

  createAccountMinor(citizenParent: Citizen, citizenEnfant: Citizen): Observable<string> {
    let citizens:Citizen[] = [citizenParent, citizenEnfant]
    console.log(citizens)
    return this.http.post<string>(this.url + '/createAccountMinor', citizens, {
      responseType: 'text' as 'json',
    });
  }

  getPermit(nas: string): Observable<Permit> {
    return this.http.get<Permit>(this.url + '/getInfosPermit/' + nas);
  }

  getForgotPassword(email: string): Observable<boolean> {
    return this.http.get<boolean>(this.url + '/getForgotPassword/' + email);
  }

  renewPermit(citizen: Citizen): Observable<string> {
    return this.http.post<string>(this.url + '/renewPermit', citizen, {
      responseType: 'text' as 'json',
    });
  }

  login(email: string, password: string): Observable<Citizen> {
    return this.http.get<Citizen>(this.url + '/login/' + email + '/' + password);
  }

  userIsLogIn(): boolean {
    let email = sessionStorage.getItem('email');
    return email != null;
  }

  public logout(): void {
    sessionStorage.clear();
  }
}
