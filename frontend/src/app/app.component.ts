import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IEvent } from './event';
import { IItem } from './item';
import { IMember } from './member';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  event$: Observable<IEvent[]>;

  constructor(private http: HttpClient) {
    this.event$ = this.http.get<IItem<IEvent>>('http://localhost:8080/events').pipe(
      map(response => response.items)
    )
  }

  members(id: number): Observable<IMember[]> {
    return this.http.get<IMember[]>('http://localhost:8080/events/' + id + '/members');
  }

}
