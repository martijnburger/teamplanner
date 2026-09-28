import { Component, computed } from '@angular/core';
import { httpResource } from '@angular/common/http';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

import { API_URL } from '../api';
import { Carousel } from '../carousel/carousel';
import { CarouselItem } from '../carousel/carousel-item';
import { EventCard } from '../event-card/event-card';
import { Page } from '../model/page';
import { TeamEvent } from '../model/team-event';

@Component({
  selector: 'teamplanner-events-overview',
  imports: [Carousel, CarouselItem, EventCard, MatProgressSpinner],
  templateUrl: './events-overview.html',
  styles: `
    mat-spinner {
      margin: 0 auto;
    }
  `,
})
export class EventsOverview {
  readonly page = httpResource<Page<TeamEvent>>(() => `${API_URL}/events`);

  /** Events in date order; the search API does not sort them. */
  readonly events = computed(() =>
    [...(this.page.value()?.items ?? [])].sort((a, b) => a.date.localeCompare(b.date)),
  );
}
