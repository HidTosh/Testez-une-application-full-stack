import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { NgZone } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let serviceApi: SessionApiService;
  let httpTestingController: HttpTestingController;
  let route : ActivatedRoute;
  let ngZone: NgZone;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: DetailComponent}
        ]),
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService }, 
        SessionApiService
      ],
    })
    .compileComponents();
    service = TestBed.inject(SessionService);
    serviceApi = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(DetailComponent);
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    ngZone = TestBed.inject(NgZone);
    httpTestingController = TestBed.inject(HttpTestingController);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call ngOnInit', () => {
    const mockPrivateFetchSession = jest.spyOn(component as any, 'fetchSession')
    component.ngOnInit()
    expect(mockPrivateFetchSession).toBeCalled();
  });

  it('should call back', () => {
    jest.spyOn(window.history, 'back');
    component.back()
    expect(window.history.back).toBeCalled()
  });

  it('should call delete', () => {
    const navigateSpy = jest.spyOn(router,'navigate');
    component.sessionId = '1';
    expect(component.delete()).toBe(void 0);
    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
    //expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should call participate', () => {
    const myPrivateFuncExitPage = jest.spyOn(component as any, 'fetchSession');
    component.sessionId = '1';
    component.userId = '1';
    
    expect(component.participate()).toBe(void 0)
    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toEqual('POST');
    req.flush(true);
  });

  it('should call participate', () => {
    const myPrivateFuncExitPage = jest.spyOn(component as any, 'fetchSession');
    component.sessionId = '1';
    component.userId = '1';

    expect(component.unParticipate()).toBe(void 0)
    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
    expect(myPrivateFuncExitPage).toBeCalled();
  });

  describe('DetailComponent integrartion suite', () => { 
    it('should render the detail session', async() => {
      //session not provided mat card not shown
      expect(fixture.nativeElement.querySelector('[mat-card]')).toBeNull();
    });
  })
});

