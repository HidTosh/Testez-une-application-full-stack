import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getByText } from '@testing-library/dom';
import userEvent from '@testing-library/user-event';

import { MeComponent } from './me.component';
import { UserService } from '../../services/user.service';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let service: UserService;
  let httpTestingController: HttpTestingController;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: '', component: MeComponent}
        ]),
        BrowserAnimationsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        HttpClientTestingModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();
    fixture = TestBed.createComponent(MeComponent);
    service = TestBed.inject(UserService);
    router = TestBed.inject(Router);
    httpTestingController = TestBed.inject(HttpTestingController);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get userById when call ngOnInit', () => {
    component.ngOnInit();
    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toEqual('GET');
    req.flush(true);
    httpTestingController.verify();
  });

  it('should call window.history.back', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toBeCalled();
  });

  it('should call userService.delete when delete called', () => {
    component.delete()
    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toEqual('DELETE');
  });

  describe('MeComponent integrartion suite', () => { 
    it('should render the me informations page', async() => {
      httpTestingController
      expect(getByText(document.body, ('User information'))).toBeTruthy();
      //const mockDelete = jest.spyOn(component as any, 'delete');
      const deleteBtn = fixture.nativeElement.querySelector('button');

      expect(deleteBtn).toBeTruthy();
      await userEvent.click(deleteBtn);    
      //expect(mockDelete).toHaveBeenCalledTimes(1);
    });
  });
});
