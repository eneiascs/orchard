import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { ProfileComponent } from './profile/profile.component';
import { NavbarComponent } from './navbar/navbar.component';
import { PostDetailComponent } from './post-detail/post-detail.component';
import { Route, Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AuthenticationGuard } from './guard/authentication.guard';
import { PostresolverService } from './service/postresolver.service';
import { AccountService } from './service/account.service';
import { LoadingService } from './service/loading.service';
import { PostService } from './service/post.service';
import { AlertService } from './service/alert.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptor/auth.interceptor';
import { CacheInterceptor } from './interceptor/cache.interceptor';

import { NgxLoadingModule } from 'ngx-loading';

const appRoutes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent},
  {path: 'resetPassword', component: ResetPasswordComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard]},
  {path: 'post/:postId', component: PostDetailComponent, resolve: {resolvedPost: PostresolverService}},
  {path: 'profile/:username', component: ProfileComponent, canActivate: [AuthenticationGuard]},
];
  
  


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    ResetPasswordComponent,
    ProfileComponent,
    NavbarComponent,
    PostDetailComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    NgxLoadingModule.forRoot({})
  ],
  providers: [
    AccountService,
    LoadingService,
    PostService,
    AlertService,
    PostresolverService,
    AuthenticationGuard,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    { provide: HTTP_INTERCEPTORS, useClass: CacheInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
