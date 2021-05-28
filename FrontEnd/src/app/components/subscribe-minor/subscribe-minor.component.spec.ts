import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscribeMinorComponent } from './subscribe-minor.component';

describe('SubscribeMinorComponent', () => {
  let component: SubscribeMinorComponent;
  let fixture: ComponentFixture<SubscribeMinorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubscribeMinorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubscribeMinorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
