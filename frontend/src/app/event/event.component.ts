import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { IMember } from '../member';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html'
})
export class EventComponent implements OnInit {

  member$: Observable<IMember[]>;

  @Input()
  eventId: string;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.member$ = this.http.get<IMember[]>(environment.baseUrl + 'events/' + this.eventId + '/members');
  }

}
