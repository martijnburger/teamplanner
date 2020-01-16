import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TeamplannerRoutingModule } from './teamplanner-routing.module';
import { TeamplannerComponent } from './teampanner.component';
import { EventComponent } from './event/event.component';
import { EventsOverviewComponent } from './events-overview/events-overview.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatGridListModule, MatCardModule, MatProgressSpinnerModule,
  MatButtonModule, MatTooltipModule, MatDialogModule
} from '@angular/material';
import { registerLocaleData } from '@angular/common';
import localeNl from '@angular/common/locales/nl';
import { MemberEventComponent, CommentDialogComponent } from './member-event/member-event.component';
import { CarouselComponent } from './carousel/carousel-component';
import { CarouselItemDirective } from './carousel/carousel-item-directive';
import { CarouselItemElementDirective } from './carousel/carousel-item-element-directive';

registerLocaleData(localeNl);

@NgModule({
  declarations: [
    TeamplannerComponent,
    EventsOverviewComponent,
    EventComponent,
    MemberEventComponent,
    CommentDialogComponent,
    CarouselComponent,
    CarouselItemDirective,
    CarouselItemElementDirective
  ],
  entryComponents: [
    CommentDialogComponent
  ],
  imports: [
    BrowserModule,
    FlexLayoutModule,
    TeamplannerRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatGridListModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatTooltipModule,
    MatDialogModule
  ],
  bootstrap: [TeamplannerComponent],
  providers: [
    { provide: LOCALE_ID, useValue: 'nl-NL' }
  ]
})
export class TeamplannerModule { }
