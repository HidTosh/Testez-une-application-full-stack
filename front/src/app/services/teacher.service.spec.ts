import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

const date = new Date();
const mockTeacher: Teacher = {
  id: 1,
  lastName: 'test',
  firstName: 'rahani',
  createdAt: date,
  updatedAt: date
};

describe('TeacherService', () => {
  let service: TeacherService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call getAllTeachers and return an array of Teachers', () => {
    const arrayMockTeacher = [mockTeacher]; 
    service.all().subscribe((res) => {
      expect(res).toEqual(arrayMockTeacher);
    });

    const req = httpTestingController.expectOne('api/teacher');
    expect(req.request.method).toEqual('GET');
    req.flush(mockTeacher);
    httpTestingController.verify();
  });

  it('should call detail and return the appropriate Teacher', () => { 
    const id = '1';
    service.detail(id).subscribe((res) => {
      expect(res).toEqual(mockTeacher);
    });

    const req = httpTestingController.expectOne(`api/teacher/${id}`);
    expect(req.request.method).toEqual('GET');
    req.flush(mockTeacher);
    httpTestingController.verify();
  })
});
