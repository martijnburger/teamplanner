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

  color(): string[] {
    switch (this.eventMember.planned) {
      case 'REJECTED': return ['rgba(244, 67, 54,.87)', 'rgba(255,255,255,.87)'];
      case 'WAITING': return ['rgba(255, 215, 64,.87)', 'rgba(0,0,0,.87)'];
      case 'ACCEPTED': return ['rgba(103, 58, 183,.87)', 'rgba(255,255,255,.87)'];
      case 'SKIPPED': return ['rgba(244, 67, 54, 0.26)', 'rgba(255,255,255,.87)'];
      default: return ['rgba(244, 67, 54,.87)', 'rgba(255,255,255,.87)'];
    }
  }

  icon(): string[] {
    switch (this.eventMember.available) {
      case 'AVAILABLE': return ['done', 'primary'];
      case 'NOT_AVAILABLE': return ['clear', 'warn'];
      case 'WAITING': return ['schedule', 'accent'];
      default: return ['priority_high', 'warn'];
    }
  }

  changeState() {
    if (this.eventMember.available === 'AVAILABLE') {
      this.eventMember.available = 'NOT_AVAILABLE';
    } else {
      this.eventMember.available = 'AVAILABLE';
    }
  }

}

@Component({
  templateUrl: 'comment-dialog.component.html'
})
export class CommentDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<CommentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: IMember) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

}