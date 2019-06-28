import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent} from '@angular/common/http';
import { AccountService } from '../service/account.service';
import { Observable } from 'rxjs';
import { ServerConstants } from '../constant/server';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constants: ServerConstants = new ServerConstants();
    private host: string = this.constants.host;
    
    constructor(private accountService:AccountService){}
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if(req.url.includes(`${this.host}/user/login`)){
            return next.handle(req);
        }

        if(req.url.includes(`${this.host}/user/register`)){
            return next.handle(req);
        }

        if(req.url.includes(`${this.host}/user/resetPassword`)){
            return next.handle(req);
        }
        
        
        this.accountService.loadToken();
        const token = this.accountService.getToken();
        const request = req.clone({ setHeaders: { Authorization: token}});

        return next.handle(request);
    }
}