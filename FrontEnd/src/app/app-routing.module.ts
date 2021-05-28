import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ConfirmSubscribeMinorComponent } from './components/confirm-subscribe-minor/confirm-subscribe-minor.component';
import { ConfirmSubscribeComponent } from './components/confirm-subscribe/confirm-subscribe.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { Error404Component } from './components/error404/error404.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { LogoutComponent } from './components/logout/logout.component';
import { SubscribeMinorComponent } from './components/subscribe-minor/subscribe-minor.component';
import { SubscribeComponent } from './components/subscribe/subscribe.component';
import { AuthGuard } from './service/auth.guard';


const routes: Routes = [

  {path:'dashboard', component:DashboardComponent, canActivate:[AuthGuard]},
  {path:'logout', component:LogoutComponent, canActivate:[AuthGuard]},

  {path:'subscribe', component:SubscribeComponent},
  {path:'subscribe-mineur', component:SubscribeMinorComponent},
  {path:'confirm-subscribe', component:ConfirmSubscribeComponent},
  {path:'confirm-subscribe-minor', component:ConfirmSubscribeMinorComponent},
  {path:'login', component:LoginComponent},

  {path:'home', component:HomeComponent},
  {path:'', redirectTo:"/home", pathMatch:'full'},
  {path: '**', component: Error404Component}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
