import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmSubscribeComponent } from './confirm-subscribe.component';

describe('ConfirmSubscribeComponent', () => {
  let component: ConfirmSubscribeComponent;
  let fixture: ComponentFixture<ConfirmSubscribeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmSubscribeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmSubscribeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
