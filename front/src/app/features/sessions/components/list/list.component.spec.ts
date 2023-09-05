import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { 
  HttpClientTestingModule, 
  HttpTestingController 
} from '@angular/common/http/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { getByText } from '@testing-library/dom';
import { ListComponent } from './list.component';

describe('ListComponent', () => {
  let component: ListComponent;
  let httpTestingController: HttpTestingController;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, HttpClientTestingModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    httpTestingController = TestBed.inject(HttpTestingController);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ListComponent integrartion suite', () => { 
    it('should render the list session', () => {
      expect(getByText(document.body, ('Rentals available'))).toBeTruthy();

      expect(getByText(document.body, ('Create'))).toBeTruthy();

      expect(getByText(document.body, ('add'))).toBeTruthy();    
    });
  })
});
