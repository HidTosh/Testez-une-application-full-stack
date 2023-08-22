import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { NgZone } from '@angular/core';

import { FormComponent } from './form.component';
import { Session } from 'src/app/features/sessions/interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let route : ActivatedRoute;
  let ngZone: NgZone;
  let httpTestingController: HttpTestingController;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 
  const mockSession: Session = {
    id: 1,
    name: 'my session',
    description: '....',
    date: new Date(),
    teacher_id: 25,
    users: [1, 5, 9],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: FormComponent}
        ]),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    ngZone = TestBed.inject(NgZone);
    httpTestingController = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('call without session admin and update in url', () => {
      const myPrivateFuncInitForm = jest.spyOn(component as any, 'initForm');
      component.ngOnInit();
      expect(myPrivateFuncInitForm).toHaveBeenCalled();
  
    })

    it('call without session admin with update in url', () => {
      component.onUpdate = false;
      jest.spyOn(router, 'url', 'get').mockReturnValue('update');
      jest.spyOn(route.snapshot.paramMap, 'get').mockReturnValue('1');
      const myPrivateFuncExitPage = jest.spyOn(component as any, 'initForm');
      const id = '1'
      
      component.ngOnInit();
      expect(component.onUpdate).toBe(true);
      const req = httpTestingController.expectOne(`api/session/${id}`);
      expect(req.request.method).toEqual('GET');
      req.flush(mockSession);
      expect(myPrivateFuncExitPage).toBeCalledWith(mockSession);

    })
  
    it('call with session admin and redirect to session', () => {
      mockSessionService.sessionInformation.admin = false;
      const navigateSpy = jest.spyOn(router,'navigate');
      ngZone.run(() => {
        component.ngOnInit();
        expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
      })
    })
  });

  describe('submit', () => {
    it('should call submit with onUpdate true', async () => {
      const myPrivateFuncExitPage = jest.spyOn(component as any, 'exitPage');
      myPrivateFuncExitPage.mockImplementation(() => {});
      
      expect(component.submit()).toBe(void 0)
      const req = httpTestingController.expectOne('api/session');
      expect(req.request.method).toEqual('POST');
      req.flush(mockSession);
      expect(myPrivateFuncExitPage).toBeCalledWith("Session created !");
    });
    
    it('should call submit with onUpdate false', () => {
      const myPrivateFuncExitPage = jest.spyOn(component as any, 'exitPage');
      myPrivateFuncExitPage.mockImplementation(() => {});
      component.onUpdate = true;
      const id = '1'
      Object.defineProperty(component, 'id', { value: '1'})

      expect(component.submit()).toBe(void 0)
      const req = httpTestingController.expectOne(`api/session/${id}`);
      expect(req.request.method).toEqual('PUT');
      req.flush(true);
      expect(myPrivateFuncExitPage).toBeCalledWith("Session updated !");
    });
  });
});
