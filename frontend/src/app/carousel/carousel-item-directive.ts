import { Directive, TemplateRef } from '@angular/core';

@Directive({
    selector: '[tpCarouselItem]'
})
export class CarouselItemDirective {

    constructor(public tpl: TemplateRef<any>) {
    }

}