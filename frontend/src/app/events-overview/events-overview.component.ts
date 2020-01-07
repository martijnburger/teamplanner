import { Component, OnInit } from '@angular/core';
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

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.event$ = this.http.get<IItem<IEvent>>(environment.baseUrl + 'events').pipe(
      map(response => response.items)
    );
  }

}
