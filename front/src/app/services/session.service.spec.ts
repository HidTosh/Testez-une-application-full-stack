import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { BehaviorSubject, Observable } from 'rxjs';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('check that function $isLogged is called correctly', () => {
    const subjectMock = new BehaviorSubject<boolean>(false)
    service.$isLogged().subscribe((res) => {
      expect(res).toEqual(subjectMock);
    });

  })

  it('should call logIn and modify the appropriate values', () => {
    const mockSessionInformation: SessionInformation = {
      token: 'my.token',
      type: '',
      id: 1,
      username: 'my username',
      firstName: 'my first name',
      lastName: 'my last name',
      admin: true
    };
    const myPrivateFunc = jest.spyOn(service as any, 'next');
    expect(service.logIn(mockSessionInformation)).toBe(void 0)
    service.logIn(mockSessionInformation)
    expect(service.isLogged).toEqual(true);
    expect(myPrivateFunc).toHaveBeenCalled();
  })

  it('should call logOut and modify the appropriate values', () => {
    service.isLogged = true;// set isLogged to true
    const myPrivateFunc = jest.spyOn(service as any, 'next');
    expect(service.logOut()).toBe(void 0)
    expect(service.isLogged).toEqual(false);
    expect(myPrivateFunc).toHaveBeenCalled();
  })

});
