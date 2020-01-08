import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { TeamplannerRoutingModule } from './teamplanner-routing.module';
import { TeamplannerComponent } from './teampanner.component';
import { EventComponent } from './event/event.component';
import { EventsOverviewComponent } from './events-overview/events-overview.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatGridListModule, MatCardModule, MatProgressSpinnerModule } from '@angular/material'

import { registerLocaleData } from '@angular/common';
import localeNl from '@angular/common/locales/nl';

registerLocaleData(localeNl);

@NgModule({
  declarations: [
    TeamplannerComponent,
    EventsOverviewComponent,
    EventComponent
  ],
  imports: [
    BrowserModule,
    TeamplannerRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatGridListModule,
    MatCardModule,
    MatProgressSpinnerModule
  ],
  bootstrap: [TeamplannerComponent],
  providers: [
    { provide: LOCALE_ID, useValue: "nl-NL" }
  ]
})
export class TeamplannerModule { }
