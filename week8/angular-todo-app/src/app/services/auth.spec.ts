import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth'; // ✅ Correct import

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService); // ✅ Correct injection
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});