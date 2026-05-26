import { HttpEventType, HttpHandlerFn, HttpRequest, HttpResponse } from "@angular/common/http";
import { inject } from "@angular/core";
import { tap } from "rxjs";
import { AuthService } from "../../services/auth-service";

export function authInterceptor(req: HttpRequest<any>, next: HttpHandlerFn) {

  console.log('AuthInterceptor called');

  const authService = inject(AuthService);

  const token = authService.getToken();
  console.log('Token from AuthService:', token);
  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  return next(req);
}