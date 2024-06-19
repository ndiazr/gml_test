import { TestBed } from '@angular/core/testing';

import { ClientService } from './client.service';

import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing'
import { take } from 'rxjs';
import { Client } from '../models/client.model';

describe('ClientService', () => {
  let service: ClientService;
  let controller: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ClientService);
    controller = TestBed.inject(HttpTestingController);
  });

  beforeEach((): void => {
    controller.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle get clients', (): void => {
    service
      .getAll()
      .pipe(take(1))
      .subscribe((res: Client[]): void => {
        expect(res.length).toEqual(1);
        expect(res[0]).toEqual({});
      });
    
      const request: TestRequest = controller.expectOne({
        method: 'GET',
        url: service.baseUrl,
      });

      request.flush([{}]);
  });

  it('should handle get clients by shared key', (): void => {
    service
      .findBySharedKey("mySharedKey")
      .pipe(take(1))
      .subscribe((res: Client[]): void => {
        expect(res.length).toEqual(1);
        expect(res[0]).toEqual({});
      });
    
      const request: TestRequest = controller.expectOne({
        method: 'GET',
        url: `${service.baseUrl}/mySharedKey/sharedKey`,
      });

      request.flush([{}]);
  });

  it('should handle create a client', (): void => {
    service
      .create({})
      .pipe(take(1))
      .subscribe((res: Client[]): void => {
        expect(res).not.toBeNull();
      });
    
      const request: TestRequest = controller.expectOne({
        method: 'POST',
        url: service.baseUrl,
      });

      request.flush({});
  });
});
