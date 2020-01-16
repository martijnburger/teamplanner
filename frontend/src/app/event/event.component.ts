import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { IMember } from '../model/member';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { IEvent } from '../model/event';

@Component({
  selector: 'teamplanner-event',
  templateUrl: 'event.component.html',
  styleUrls: [ 'event.component.scss' ]
})
export class EventComponent implements OnInit {

  public member$: Observable<IMember[]>;

  @Input()
  event: IEvent;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.member$ = this.http.get<IMember[]>(environment.baseUrl + 'events/' + this.event.id + '/members');
  }

}
