import { Component, computed, inject, input, linkedSignal } from '@angular/core';
import { MatIconButton, MatMiniFabButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatIcon } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';

import { Availability, MemberEvent } from '../model/member-event';
import { CommentDialog } from './comment-dialog';

const AVAILABILITY_ICONS: Record<Availability, { icon: string; color: string }> = {
  AVAILABLE: { icon: 'done', color: 'primary' },
  NOT_AVAILABLE: { icon: 'clear', color: 'warn' },
  WAITING: { icon: 'schedule', color: 'accent' },
};

/** One member's availability and planning status for an event. */
@Component({
  selector: 'teamplanner-member-event-row',
  imports: [MatIconButton, MatMiniFabButton, MatIcon, MatTooltip],
  templateUrl: './member-event-row.html',
  styleUrl: './member-event-row.scss',
  host: { '[class]': 'member().planned.toLowerCase()' },
})
export class MemberEventRow {
  private readonly dialog = inject(MatDialog);

  readonly member = input.required<MemberEvent>();

  /** Local availability; toggling it is not saved to the backend yet. */
  readonly available = linkedSignal(() => this.member().available);

  readonly status = computed(
    () => AVAILABILITY_ICONS[this.available()] ?? { icon: 'priority_high', color: 'warn' },
  );

  readonly shortComment = computed(() => {
    const comment = this.member().comment;
    return comment && comment.length > 20 ? comment.substring(0, 17) + '...' : comment;
  });

  toggleAvailability() {
    this.available.update((a) => (a === 'AVAILABLE' ? 'NOT_AVAILABLE' : 'AVAILABLE'));
  }

  showComment() {
    this.dialog.open(CommentDialog, { width: '250px', data: this.member() });
  }
}
