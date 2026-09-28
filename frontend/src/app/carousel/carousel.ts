import { Component, computed, contentChildren, signal } from '@angular/core';
import { NgTemplateOutlet } from '@angular/common';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

import { CarouselItem } from './carousel-item';

/** Shows its `*teamplannerCarouselItem` slides one at a time, with previous and next buttons. */
@Component({
  selector: 'teamplanner-carousel',
  imports: [NgTemplateOutlet, MatIconButton, MatIcon],
  templateUrl: './carousel.html',
  styleUrl: './carousel.scss',
})
export class Carousel {
  readonly items = contentChildren(CarouselItem);
  readonly current = signal(0);
  readonly isFirst = computed(() => this.current() === 0);
  readonly isLast = computed(() => this.current() >= this.items().length - 1);
  readonly offset = computed(() => `translateX(-${this.current() * 100}%)`);

  previous() {
    if (!this.isFirst()) {
      this.current.update((i) => i - 1);
    }
  }

  next() {
    if (!this.isLast()) {
      this.current.update((i) => i + 1);
    }
  }
}
