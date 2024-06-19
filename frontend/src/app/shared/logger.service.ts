import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoggerService {

  constructor() { }

  info(msg: string, payload?: object): void {
    console.info(msg, payload ?? '');
  }

  warm(msg: string, payload?: object): void {
    console.warn(msg, payload ?? '');
  }

  error(msg: string, exception?: object): void {
    console.error(msg, exception ?? '');
  }
}
