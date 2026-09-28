import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { Component, input } from '@angular/core';

import { EventCard } from '../event-card/event-card';
import { TeamEvent } from '../model/team-event';
import { EventsOverview } from './events-overview';

@Component({ selector: 'teamplanner-event-card', template: '{{ event().name }}' })
class EventCardStub {
  readonly event = input.required<TeamEvent>();
}

describe('EventsOverview', () => {
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    // The real event card loads its own members, which is covered by its own test
    TestBed.overrideComponent(EventsOverview, {
      remove: { imports: [EventCard] },
      add: { imports: [EventCardStub] },
    });
    http = TestBed.inject(HttpTestingController);
  });

  it('loads the events and sorts them by date', async () => {
    const fixture = TestBed.createComponent(EventsOverview);
    TestBed.tick();

    http.expectOne('/api/v1.0/events').flush({
      items: [
        { id: 5, name: 'Wedstrijd', date: '2020-01-19', planned: false },
        { id: 1, name: 'Training', date: '2020-01-05', planned: true },
      ],
      count: 2,
      pageSize: 20,
      pageCount: 1,
    });
    await fixture.whenStable();

    expect(fixture.componentInstance.events().map((e) => e.id)).toEqual([1, 5]);
    expect(fixture.nativeElement.querySelectorAll('teamplanner-event-card').length).toBe(2);
  });

  it('shows an error when the events cannot be loaded', async () => {
    const fixture = TestBed.createComponent(EventsOverview);
    TestBed.tick();

    http.expectOne('/api/v1.0/events').flush('', { status: 503, statusText: 'Unavailable' });
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('konden niet worden geladen');
  });
});
