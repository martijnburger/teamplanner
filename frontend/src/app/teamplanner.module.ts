import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { TeamplannerRoutingModule } from './teamplanner-routing.module';
import { TeamplannerComponent } from './teampanner.component';
import { EventComponent } from './event/event.component';
import { EventsOverviewComponent } from './events-overview/events-overview.component';

@NgModule({
  declarations: [
    TeamplannerComponent,
    EventsOverviewComponent,
    EventComponent
  ],
  imports: [
    BrowserModule,
    TeamplannerRoutingModule,
    HttpClientModule
  ],
  bootstrap: [TeamplannerComponent]
})
export class TeamplannerModule { }
