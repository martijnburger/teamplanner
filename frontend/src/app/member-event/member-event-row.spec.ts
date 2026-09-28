import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberEvent } from '../model/member-event';
import { MemberEventRow } from './member-event-row';

describe('MemberEventRow', () => {
  let fixture: ComponentFixture<MemberEventRow>;

  function render(member: Partial<MemberEvent>) {
    fixture = TestBed.createComponent(MemberEventRow);
    fixture.componentRef.setInput('member', {
      id: 1,
      name: 'Martijn Burger',
      available: 'AVAILABLE',
      planned: 'ACCEPTED',
      comment: '',
      ...member,
    });
    fixture.detectChanges();
    return fixture.componentInstance;
  }

  it('shows the name and uses the planning status as class', () => {
    render({ planned: 'REJECTED' });
    const host: HTMLElement = fixture.nativeElement;
    expect(host.textContent).toContain('Martijn Burger');
    expect(host.classList).toContain('rejected');
  });

  it('picks the icon from the availability', () => {
    expect(render({ available: 'AVAILABLE' }).status()).toEqual({ icon: 'done', color: 'primary' });
    expect(render({ available: 'NOT_AVAILABLE' }).status()).toEqual({ icon: 'clear', color: 'warn' });
    expect(render({ available: 'WAITING' }).status()).toEqual({ icon: 'schedule', color: 'accent' });
  });

  it('toggles between available and not available', () => {
    const row = render({ available: 'WAITING' });
    row.toggleAvailability();
    expect(row.available()).toBe('AVAILABLE');
    row.toggleAvailability();
    expect(row.available()).toBe('NOT_AVAILABLE');
  });

  it('shortens long comments for the tooltip', () => {
    expect(render({ comment: 'Sturen?' }).shortComment()).toBe('Sturen?');
    expect(render({ comment: 'Ik ben waarschijnlijk iets later.' }).shortComment()).toBe(
      'Ik ben waarschijn...',
    );
  });

  it('only shows the comment button when there is a comment', () => {
    render({ comment: '' });
    expect(fixture.nativeElement.querySelectorAll('button').length).toBe(1);
    render({ comment: 'Vakantie' });
    expect(fixture.nativeElement.querySelectorAll('button').length).toBe(2);
  });
});
