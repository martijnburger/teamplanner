import { Component, inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle,
} from '@angular/material/dialog';

import { MemberEvent } from '../model/member-event';

@Component({
  selector: 'teamplanner-comment-dialog',
  imports: [MatButton, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle],
  template: `
    <h2 mat-dialog-title>Opmerkingen</h2>
    <mat-dialog-content>{{ data.comment }}</mat-dialog-content>
    <mat-dialog-actions align="end">
      <button matButton="filled" mat-dialog-close class="reset">Reset</button>
      <button matButton mat-dialog-close cdkFocusInitial>Ok</button>
    </mat-dialog-actions>
  `,
  styles: `
    .reset {
      margin-right: auto;
      --mat-button-filled-container-color: var(--mat-sys-error);
      --mat-button-filled-label-text-color: var(--mat-sys-on-error);
    }
  `,
})
export class CommentDialog {
  readonly data = inject<MemberEvent>(MAT_DIALOG_DATA);
}
