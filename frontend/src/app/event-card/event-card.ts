import { Component, input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { httpResource } from '@angular/common/http';
import { MatMiniFabButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

import { API_URL } from '../api';
import { MemberEvent } from '../model/member-event';
import { TeamEvent } from '../model/team-event';
import { MemberEventRow } from '../member-event/member-event-row';

@Component({
  selector: 'teamplanner-event-card',
  imports: [DatePipe, MatMiniFabButton, MatIcon, MatProgressSpinner, MemberEventRow],
  templateUrl: './event-card.html',
  styleUrl: './event-card.scss',
})
export class EventCard {
  readonly event = input.required<TeamEvent>();

  readonly members = httpResource<MemberEvent[]>(
    () => `${API_URL}/events/${this.event().id}/members`,
  );
}
