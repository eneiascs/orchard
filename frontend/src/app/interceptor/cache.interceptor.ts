import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse } from '@angular/common/http';
import { ServerConstants } from '../constant/server';
import { AccountService } from '../service/account.service';
import { CacheService } from '../service/cache.service';
import { Observable } from 'rxjs';

@Injectable()
export class CacheInterceptor implements HttpInterceptor {
    constants: ServerConstants = new ServerConstants();
    private host: string = this.constants.host;

    constructor(private accountService: AccountService, private cacheService: CacheService){}

    intercept (req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        if (req.method !== 'GET') {
            this.cacheService.clearCache();
            return next.handle(req);
        }

        if(req.url.includes(`${this.host}/user/resetPassword`)){
            return next.handle(req);
        }

        if(req.url.includes(`${this.host}/user/login`)){
            return next.handle(req);
        }

        if(req.url.includes(`${this.host}/user/findByUsername`)){
            return next.handle(req);
        }

        const cachedResponse: HttpResponse<any> = this.cacheService.getCache(req.url);
        
        //TODO Fix
        
        /*
        if(cachedResponse){
            return of (cachedResponse);
        }

        return next.handle(req).pipe(tap(event => {
            if (event instanceof HttpResponse){
                this.cacheService.cacheRequest(req.url, event);
            }
        }));
        */

       return next.handle(req)
    }
}