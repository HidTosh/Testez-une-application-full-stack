import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { NgZone } from '@angular/core';
import userEvent from '@testing-library/user-event';

import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpTestingController: HttpTestingController;
  let service : AuthService;
  let router: Router;
  let ngZone: NgZone;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: LoginComponent}
        ]),
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientTestingModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should login and navigate to session when the correct credentials are sent', () => {
    const navigateSpy = jest.spyOn(router,'navigate');

    component.submit();
    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toEqual('POST');
    ngZone.run(() => {
      req.flush(true);
      httpTestingController.verify();
      expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    })
  });

  it('should fail when try to login with bad credentials', () => {
    component.submit();
    httpTestingController.expectOne('api/auth/login')
    .error(new ErrorEvent(''));
  });

  
  describe('LoginComponent integrartion suite', () => {
    let emailInput: HTMLInputElement;
    let passwordInput: HTMLInputElement;
    let buttonSubmit: HTMLButtonElement;
    const email: string = 'luca@yahoo.fr';
    const password: string = '*********';
    beforeEach(async () => {
      emailInput = fixture.nativeElement.querySelector('[data-placeholder="Email"]');
      passwordInput = fixture.nativeElement.querySelector('[data-placeholder="Password"]');
      buttonSubmit = fixture.nativeElement.querySelector('[type="submit"]');
    })

    it('should button submit be disable by default, empty form', async() => {
      expect(buttonSubmit.disabled).toBeTruthy(); 
    })

    it('should button submit be disable when only password provided', async() => {
      await userEvent.type(passwordInput, password);
      fixture.detectChanges();
      expect(buttonSubmit.disabled).toBeTruthy(); 
    })

    it('should button submit be disable when only email provided', async() => {
      await userEvent.type(emailInput, email);
      fixture.detectChanges();
      expect(buttonSubmit.disabled).toBeTruthy(); 
    })

    it('should button submit be disable when invalid email provided', async() => {
      await userEvent.type(emailInput, 'lucayahoo');
      await userEvent.type(passwordInput, password);
      fixture.detectChanges();
      expect(buttonSubmit.disabled).toBeTruthy(); 
    })

    it('should button submit be enabled when valid form information', async() => {
      const user = userEvent.setup();      
      const mockSubmit = jest.spyOn(component as any, 'submit');

      await user.type(emailInput, email);
      await user.type(passwordInput, password);
      fixture.detectChanges();
      expect(buttonSubmit.disabled).toBeFalsy();
      await user.click(buttonSubmit);
      expect(mockSubmit).toHaveBeenCalledTimes(1);
    });
  })
});
