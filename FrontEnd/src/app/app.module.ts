import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http'
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { Error404Component } from './components/error404/error404.component';
import { LoginComponent } from './components/login/login.component';
import { SubscribeComponent } from './components/subscribe/subscribe.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { ConfirmSubscribeComponent } from './components/confirm-subscribe/confirm-subscribe.component';
import { LogoutComponent } from './components/logout/logout.component';
import { SubscribeMinorComponent } from './components/subscribe-minor/subscribe-minor.component';
import { ConfirmSubscribeMinorComponent } from './components/confirm-subscribe-minor/confirm-subscribe-minor.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    Error404Component,
    LoginComponent,
    SubscribeComponent,
    DashboardComponent,
    HeaderComponent,
    FooterComponent,
    ConfirmSubscribeComponent,
    LogoutComponent,
    SubscribeMinorComponent,
    ConfirmSubscribeMinorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
