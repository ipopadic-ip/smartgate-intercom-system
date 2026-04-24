import { TestBed } from '@angular/core/testing';

import { Intercom } from './intercom';

describe('Intercom', () => {
  let service: Intercom;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Intercom);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
