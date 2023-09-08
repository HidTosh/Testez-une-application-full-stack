import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { Router } from '@angular/router';
import { NgZone } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import userEvent from '@testing-library/user-event';

import { RegisterComponent } from './register.component';
import { LoginComponent } from '../login/login.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpTestingController: HttpTestingController;
  let router: Router;
  let ngZone: NgZone;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: RegisterComponent},
          { path: 'login', component: LoginComponent}
        ]),
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        HttpClientTestingModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    httpTestingController = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call register success', () => {
    const navigateSpy = jest.spyOn(router,'navigate');

    component.form.controls['email'].setValue("test@test.io");
    component.form.controls['firstName'].setValue("firstname");
    component.form.controls['lastName'].setValue("lastname");
    component.form.controls['password'].setValue("*****");
    component.submit()

    const req = httpTestingController.expectOne('api/auth/register');
    
    expect(req.request.method).toEqual('POST');
    ngZone.run(() => {
      req.flush(true);
      expect(req.request.body.email).toEqual("test@test.io");
      expect(req.request.body.firstName).toEqual("firstname");
      expect(req.request.body.lastName).toEqual("lastname");
      expect(req.request.body.password).toEqual("*****");
      httpTestingController.verify();
      expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    })
  });

  it('should call register error', () => {
    component.form.controls['email'].setValue("");
    component.form.controls['firstName'].setValue("");
    component.form.controls['lastName'].setValue("lastname");
    component.form.controls['password'].setValue("*****");
    component.submit()

    const req = httpTestingController.expectOne('api/auth/register');
    
    expect(req.request.method).toEqual('POST');
    expect(req.request.body.email).toEqual("");
    expect(req.request.body.firstName).toEqual("");
    req.error(new ErrorEvent(''));
    httpTestingController.verify();
  });

  describe('RegisterComponent integrartion suite', () => { 
    let firstName: HTMLInputElement;
    let lastName: HTMLInputElement;
    let emailInput: HTMLInputElement;
    let passwordInput: HTMLInputElement;
    let buttonSubmit: HTMLButtonElement;

    beforeEach(async () => {
      firstName = fixture.nativeElement.querySelector('[data-placeholder="First name"]');
      lastName = fixture.nativeElement.querySelector('[data-placeholder="Last name"]');
      emailInput = fixture.nativeElement.querySelector('[data-placeholder="Email"]');
      passwordInput = fixture.nativeElement.querySelector('[data-placeholder="Password"]');
      buttonSubmit = fixture.nativeElement.querySelector('[type="submit"]');
    })

    it('should button submit be disable when only email provided', async() => {
      await userEvent.type(emailInput, 'thoas@yahoo.fr');
      fixture.detectChanges();
      expect(buttonSubmit.disabled).toBeTruthy(); 
    })

    it('should button submit be disable when only password and email provided', async() => {
      await userEvent.type(emailInput, 'thoas@yahoo.fr');
      await userEvent.type(passwordInput, '*******');
      fixture.detectChanges();
      expect(buttonSubmit.disabled).toBeTruthy(); 
    })

    it('should button submit be disable when only password and email and firstName provided', async() => {
      await userEvent.type(firstName, 'thomas');
      await userEvent.type(emailInput, 'thoas@yahoo.fr');
      await userEvent.type(passwordInput, '*******');
      fixture.detectChanges();
      expect(buttonSubmit.disabled).toBeTruthy(); 
    })

    it('should button submit be disable when only password and email and lastName provided', async() => {
      await userEvent.type(lastName, 'bst');
      await userEvent.type(emailInput, 'thoas@yahoo.fr');
      await userEvent.type(passwordInput, '*******');
      fixture.detectChanges();
      expect(buttonSubmit.disabled).toBeTruthy(); 
    })

    it('should button submit be enabled when valid form information', async() => {
      expect(buttonSubmit.disabled).toBeTruthy();
      await userEvent.type(firstName, 'thomas');
      await userEvent.type(lastName, 'bst');
      await userEvent.type(emailInput, 'thoas@yahoo.fr');
      await userEvent.type(passwordInput, '******');
      fixture.detectChanges()
      
      expect(buttonSubmit.disabled).toBeFalsy();
      const mockSubmit = jest.spyOn(component as any, 'submit');
      await userEvent.click(buttonSubmit);
      expect(mockSubmit).toHaveBeenCalledTimes(1);
    });

  })
});
