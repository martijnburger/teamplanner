import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '../environments/environment';

import { IEvent } from './event';
import { IItem } from './item';
import { IMember } from './member';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html'
})
export class AppComponent implements OnInit {

  event$: Observable<IEvent[]>;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.event$ = this.http.get<IItem<IEvent>>(environment.baseUrl + 'events').pipe(
      map(response => response.items)
    );
  }

}
