import { Directive, TemplateRef, inject } from '@angular/core';

/** Marks a template as one slide of a `teamplanner-carousel`. */
@Directive({
  selector: '[teamplannerCarouselItem]',
})
export class CarouselItem {
  readonly template = inject(TemplateRef);
}
