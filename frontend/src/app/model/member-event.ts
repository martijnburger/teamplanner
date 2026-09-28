export type Availability = 'AVAILABLE' | 'NOT_AVAILABLE' | 'WAITING';

export type Plannability = 'ACCEPTED' | 'REJECTED' | 'SKIPPED' | 'WAITING';

export interface MemberEvent {
  id: number;
  name: string;
  available: Availability;
  planned: Plannability;
  comment: string;
}
