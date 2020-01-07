import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { TeamplannerRoutingModule } from './teamplanner-routing.module';
import { TeamplannerComponent } from './teampanner.component';
import { EventComponent } from './event/event.component';
import { EventsOverviewComponent } from './events-overview/events-overview.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

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
    BrowserAnimationsModule
  ],
  bootstrap: [TeamplannerComponent]
})
export class TeamplannerModule { }
