import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '../../environments/environment';

import { IEvent } from '../model/event';
import { IItem } from '../model/item';

@Component({
  selector: 'teamplanner-events-overview',
  templateUrl: 'events-overview.component.html'
})
export class EventsOverviewComponent implements OnInit {

  event$: Observable<IEvent[]>;

  @ViewChild('menu', { static: false }) menuView: ElementRef;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.event$ = this.http.get<IItem<IEvent>>(environment.baseUrl + 'events').pipe(
      map(response => response.items)
    );
  }

  onLeft() {
    console.log('Go left...');
    this.menuView.nativeElement.animate( { scrollLeft: 100}, 1000);
  }

  onRight() {
    console.log('Go right...');
  }

}
