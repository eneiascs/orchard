import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { User } from '../model/user';
import { PasswordChange } from '../model/password-change';
import { Post } from '../model/post';
import { ServerConstants } from '../constant/server';




@Injectable({
  providedIn: 'root'
})
export class AccountService {
  constants: ServerConstants = new ServerConstants();
  public host: string = this.constants.host;
  public token: string;
  public loggedInUsername: string | null;
  public redirectUrl: string;
  private googleMapsAPIKey = "";
  private googleMapsAPIUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
  private jwtHelper = new JwtHelperService();
  constructor(private http: HttpClient) { }

  login(user: User): Observable<HttpErrorResponse | HttpResponse<any>> {
    return this.http.post<HttpErrorResponse | HttpResponse<any>>(`${this.host}/user/login`, user, { observe: 'response' });
  }

  register(user: User): Observable<User | HttpErrorResponse> {
    return this.http.post<User>(`${this.host}/user/register`, user);
  }

  resetPassword(email: string) {
    return this.http.get(`${this.host}/user/resetPassword/${email}`, {
      responseType: 'text'
    });
  }

  logout(): void {
    this.token = null;
    localStorage.removeItem('token');
  }

  saveToken(token: string): void {
    this.token = token;
    localStorage.setItem('token', token);
  }

  loadToken(): void {
    this.token = localStorage.getItem('token');
  }

  getToken(): string {
    return this.token;
  }

  isLoggedIn(): boolean {
    this.loadToken();
    if (this.token && this.jwtHelper.decodeToken(this.token).sub && !this.jwtHelper.isTokenExpired(this.token)){
      this.loggedInUsername = this.jwtHelper.decodeToken(this.token).sub;
      return true;
    } else {
      this.logout();
      return false;
    }
  }

  getUserInformation(username: string): Observable<User> {
    return this.http.get<User>(`${this.host}/user/${username}`);    
  }
  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.host}/post/list`);    
  }

  searchUsers(username: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.host}/user/findByUSername/${username}`);    
  }

  getLocation(): string {
    return "Random location";
  }

  updateUser(updateUser: User){
    return this.http.post<User>(`${this.host}/user/update`, updateUser);
  }

  changePassword(changePassword: PasswordChange){
      return this.http.post(`${this.host}/user/changePassword`, changePassword, { responseType: 'text' });
  }  

  uploadUserProfilePicture(profilePicture: File){
    const fd = new FormData();
    fd.append('image', profilePicture);
    return this.http
      .post(`${this.host}/user/photo/upload`, fd, { responseType: 'text'})
      .subscribe(
        (response: any) => {
          console.log(response);
          console.log('User profile picture was uploaded. ' + response);
        }, 
        error => {
          console.log(error);
        }
      )
  }
}
