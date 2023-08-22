import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

const date = new Date();
const mockSession: Session = {
  id: 1,
  name: 'my session',
  description: '....',
  date: date,
  teacher_id: 25,
  users: [1, 5, 9],
  createdAt: date,
  updatedAt: date,
};

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call all and return an array of Session', () => {
    const arrayMockSession = [mockSession]; 
    service.all().subscribe((res) => {
      expect(res).toEqual(arrayMockSession);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toEqual('GET');
    req.flush(mockSession);
    httpTestingController.verify();
  });

  
  it('should call detail and return the appropriate Session', () => { 
    const id = '1';
    service.detail(id).subscribe((res) => {
      expect(res).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne(`api/session/${id}`);
    expect(req.request.method).toEqual('GET');
    req.flush(mockSession);
    httpTestingController.verify();
  })

  it('should call delete and return any', () => { 
    const id = '1';
    service.delete(id).subscribe((res) => {
      expect(res).toEqual(true);
    });

    const req = httpTestingController.expectOne(`api/session/${id}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
    httpTestingController.verify();
  })

  it('should call create and return the appropriate Session', () => { 
    service.create(mockSession).subscribe((res) => {
      expect(res).toEqual(mockSession);
    });

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toEqual('POST');
    req.flush(mockSession);
    httpTestingController.verify();
  })

  it('should call update and return updated session', () => { 
    const id = '1';
    service.update(id, mockSession).subscribe((res) => {
      expect(res).toEqual(true);
    });

    const req = httpTestingController.expectOne(`api/session/${id}`);
    expect(req.request.method).toEqual('PUT');
    req.flush(true);
    httpTestingController.verify();
  })

  it('should call participate and return nothing', () => { 
    const id = '1';
    const userId = '27';
    service.participate(id, userId).subscribe((res) => {
      expect(res).toEqual(true);
    });

    const req = httpTestingController.expectOne(`api/session/${id}/participate/${userId}`);
    expect(req.request.method).toEqual('POST');
    req.flush(true);
    httpTestingController.verify();
  })

  it('should call unParticipate and return nothing', () => { 
    const id = '1';
    const userId = '27';
    service.unParticipate(id, userId).subscribe((res) => {
      expect(res).toEqual(true);
    });

    const req = httpTestingController.expectOne(`api/session/${id}/participate/${userId}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
    httpTestingController.verify();
  })
});
