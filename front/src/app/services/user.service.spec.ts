import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

const date = new Date();
const mockUser: User = {
  id: 1,
  email: 'test@test.io',
  lastName: 'thomas',
  firstName: 'magnet',
  admin: false,
  password: 'test!1234',
  createdAt: date,
  updatedAt: date
};

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call getById and return the appropriate User', () => { 
    const id = '1';
    service.getById(id).subscribe((res) => {
      expect(res).toEqual(mockUser);
    });

    const req = httpTestingController.expectOne(`api/user/${id}`);
    expect(req.request.method).toEqual('GET');
    req.flush(mockUser);
    httpTestingController.verify();
  })

  it('should call delete and return any', () => { 
    const id = '1';
    service.delete(id).subscribe((res) => {
      expect(res).toEqual(true);
    });

    const req = httpTestingController.expectOne(`api/user/${id}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
    httpTestingController.verify();
  })
});
