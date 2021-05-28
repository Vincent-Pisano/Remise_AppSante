import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmSubscribeMinorComponent } from './confirm-subscribe-minor.component';

describe('ConfirmSubscribeMinorComponent', () => {
  let component: ConfirmSubscribeMinorComponent;
  let fixture: ComponentFixture<ConfirmSubscribeMinorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmSubscribeMinorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmSubscribeMinorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
