import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedemandeComponent } from './savedemande.component';

describe('SavedemandeComponent', () => {
  let component: SavedemandeComponent;
  let fixture: ComponentFixture<SavedemandeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SavedemandeComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SavedemandeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
