import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { EventsOverview } from './events-overview/events-overview';

@Component({
  selector: 'teamplanner-root',
  imports: [RouterOutlet, EventsOverview],
  template: `
    <teamplanner-events-overview />
    <router-outlet />
  `,
})
export class App {}
