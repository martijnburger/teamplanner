import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { EventCard } from './event-card';

describe('EventCard', () => {
  it('loads and shows the members of the event', async () => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    const http = TestBed.inject(HttpTestingController);
    const fixture = TestBed.createComponent(EventCard);
    fixture.componentRef.setInput('event', {
      id: 3,
      name: 'Training',
      date: '2020-01-12',
      planned: true,
    });
    TestBed.tick();

    http.expectOne('/api/v1.0/events/3/members').flush([
      { id: 1, name: 'Sander Kroon', available: 'WAITING', planned: 'WAITING', comment: '' },
      { id: 2, name: 'Hein Hopmans', available: 'AVAILABLE', planned: 'ACCEPTED', comment: '' },
    ]);
    await fixture.whenStable();

    const text: string = fixture.nativeElement.textContent;
    expect(text).toContain('Training');
    expect(text).toContain('Sander Kroon');
    expect(fixture.nativeElement.querySelectorAll('teamplanner-member-event-row').length).toBe(2);
  });
});
