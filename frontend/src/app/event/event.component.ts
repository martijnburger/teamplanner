import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { IMember } from '../model/member';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'teamplanner-event',
  templateUrl: './event.component.html'
})
export class EventComponent implements OnInit {

  public member$: Observable<IMember[]>;

  @Input()
  eventId: string;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.member$ = this.http.get<IMember[]>(environment.baseUrl + 'events/' + this.eventId + '/members');
  }

}
