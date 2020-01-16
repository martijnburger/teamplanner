import {
    AfterViewInit, Component, ContentChildren, ElementRef, Input,
    QueryList, ViewChild, ViewChildren } from '@angular/core';
import { CarouselItemDirective } from './carousel-item-directive';
import { animate, AnimationBuilder, AnimationFactory, AnimationPlayer, style } from '@angular/animations';
import { CarouselItemElementDirective } from './carousel-item-element-directive';

// https://netbasal.com/building-a-simple-carousel-component-with-angular-3a94092b7080
@Component({
    selector: 'teamplanner-carousel',
    templateUrl: 'carousel-component.html',
    styleUrls: [ 'carousel-component.scss' ]
})
export class CarouselComponent implements AfterViewInit {
    @ContentChildren(CarouselItemDirective) items: QueryList<CarouselItemDirective>;
    @ViewChildren(CarouselItemElementDirective, { read: ElementRef }) private itemsElements: QueryList<ElementRef>;
    @ViewChild('carousel', { static: true }) private carousel: ElementRef;
    private timing = '250ms ease-in';
    private showControls = true;
    private player: AnimationPlayer;
    private itemWidth: number;
    private currentSlide = 0;
    carouselWrapperStyle = {}

    next() {
        if (this.currentSlide + 1 === this.items.length) { return; }
        this.currentSlide = (this.currentSlide + 1) % this.items.length;
        const offset = this.currentSlide * this.itemWidth;
        const myAnimation: AnimationFactory = this.buildAnimation(offset);
        this.player = myAnimation.create(this.carousel.nativeElement);
        this.player.play();
    }

    isNext() {
        if (this.currentSlide + 1 === this.items.length) { return true; } else { return false; }

    }

    private buildAnimation(offset) {
        return this.builder.build([
            animate(this.timing, style({ transform: `translateX(-${offset}px)` }))
        ]);
    }

    prev() {
        if (this.currentSlide === 0) { return; }

        this.currentSlide = ((this.currentSlide - 1) + this.items.length) % this.items.length;
        const offset = this.currentSlide * this.itemWidth;

        const myAnimation: AnimationFactory = this.buildAnimation(offset);
        this.player = myAnimation.create(this.carousel.nativeElement);
        this.player.play();
    }

    isPrev() {
        if (this.currentSlide === 0)  {return true; } else { return false; }
    }

    constructor(private builder: AnimationBuilder) {
    }

    ngAfterViewInit() {
        // For some reason only here I need to add setTimeout, in my local env it's working without this.
        setTimeout(() => {
            this.itemWidth = this.itemsElements.first.nativeElement.getBoundingClientRect().width;
            this.carouselWrapperStyle = {
                width: `${this.itemWidth}px`
            }
        });

    }

}
