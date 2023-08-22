import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { NgZone } from '@angular/core';

import { AppComponent } from './app.component';


describe('AppComponent', () => {
  let app: AppComponent;
  let service: SessionService;
  let ngZone: NgZone;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: '', component: AppComponent}
        ]),
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
    const fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('should call isLogged', () => {
    service = TestBed.inject(SessionService);

    app.$isLogged()
    const subjectMock = new BehaviorSubject<boolean>(false)
    service.$isLogged().subscribe((res) => {
      expect(res).toEqual(subjectMock);
    });
  });

  it('should call logout', () => {
    service = TestBed.inject(SessionService);
    ngZone = TestBed.inject(NgZone);
    router = TestBed.inject(Router);
    
    const navigateSpy = jest.spyOn(router,'navigate');
    ngZone.run(() => {
      app.logout()
      expect(navigateSpy).toHaveBeenCalledWith(['']);
    })
  });
});
