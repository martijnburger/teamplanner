import { Component, OnInit, Input, Inject, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { IMember } from '../model/member';

@Component({
  selector: 'teamplanner-member-event',
  templateUrl: 'member-event.component.html',
  styleUrls: ['member-event.component.scss']
})
export class MemberEventComponent implements OnInit {

  @Input()
  public eventMember: IMember;

  public shortComment: string;

  constructor(public dialog: MatDialog) { }

  ngOnInit() {
    if (this.eventMember.comment && this.eventMember.comment.length > 20) {
      this.shortComment = this.eventMember.comment.substring(0, 17) + '...';
    } else {
      this.shortComment = this.eventMember.comment;
    }
  }

  showComment() {
    const dialogRef = this.dialog.open(CommentDialogComponent, {
      width: '250px',
      data: this.eventMember
    });
  }

  getBackgroundColor() {
    switch (this.eventMember.planned) {
      case 'REJECTED': return 'red';
      case 'WAITING': return 'yellow';
      case 'ACCEPTED': return 'green';
      case 'SKIPPED': return 'grey';
      default: return 'blue';
    }
  }

  getColor() {
    switch (this.eventMember.planned) {
      case 'REJECTED': return '#fbfbfb';
      case 'WAITING': return '#f00';
      case 'ACCEPTED': return '#fbfbfb';
      case 'SKIPPED': return 'purple';
      default: return 'white';
    }
  }

  getIcon() {
    switch (this.eventMember.available) {
      case 'AVAILABLE': return 'done';
      case 'NOT_AVAILABLE': return 'clear';
      case 'WAITING': return 'schedule';
      default: return 'priority_high';
    }
  }

}

@Component({
  template: `
  <h1 mat-dialog-title>Opmerkingen</h1>
  <div mat-dialog-content>
    {{data.comment}}
  </div>
  <div mat-dialog-actions>
     <button mat-button mat-dialog-close cdkFocusInitial>Ok</button>
  </div>
  `
})
export class CommentDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<CommentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: IMember) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

}