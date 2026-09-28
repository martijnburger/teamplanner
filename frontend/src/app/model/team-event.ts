export interface TeamEvent {
  id: number;
  name: string;
  /** ISO date, e.g. 2020-01-05 */
  date: string;
  planned: boolean;
}
