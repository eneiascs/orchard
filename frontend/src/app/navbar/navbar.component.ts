import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from '../model/user';
import { Router } from '@angular/router';
import { AlertService } from '../service/alert.service';
import { AccountService } from '../service/account.service';
import { PostService } from '../service/post.service';
import { LoadingService } from '../service/loading.service';
import { Post } from '../model/post';
import { HttpEventType } from '@angular/common/http';
import { AlertType } from '../enum/alert-type.enum';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  user: User;
  searchedUser: User[];
  host: string;
  userHost: string;
  postHost: string;
  postPicture:  File;
  userName: string;
  userLoggedIn: boolean;
  showNavBar: boolean;
  showSuccessAlert: boolean;
  photoName: string;
  location = null;
  latitude: any;
  longitude: any;
  progress: number;
  newPostUrl: string;
  clientHost: string;
  postFail: boolean;

  constructor(
    private router: Router,
    private alertService: AlertService,
    private accountService: AccountService,
    private postService: PostService,
    private loadingService: LoadingService
  ) { }

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.host = this.postService.host;
    this.clientHost = this.postService.clientHost;
    this.userHost = this.postService.postHost;
    this.showNavBar = true;
  }

  getUserInfo(username: string): void {
    this.subscriptions.push(
      this.accountService.getUserInformation(username).subscribe(
        (response: User) => {
          this.user = response;
          this.userLoggedIn = true;
          this.showNavBar = true;
        },
        error => {
          console.error(error);
          this.userLoggedIn = false;
          
        }
      )
    );
  }

  onSearchUsers(event) {
    console.log(event);
    const username = event;
    this.subscriptions.push(
      this.accountService.searchUsers(username).subscribe(
        (response: User[]) => {
          console.log(response);
          this.searchedUser = response;
        },
        error => {
          console.log(error);
          return this.searchedUser = [];
        }
      )
    );
  }
  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
  }

  getSearchUserProfile(username: string): void {
    const element: HTMLElement = document.getElementById('closeSearchMOdal') as HTMLHtmlElement;
    element.click();
    this.router.navigate(['/profile', username]);
    setTimeout(() => {
      location.reload();
    }, 100);
  }

  onFileSelected(event: any): void {
    console.log('file was selected');
    this.postPicture = event.target.files[0];
    this.photoName = this.postPicture.name;
  }

  onNewPost(post: Post): void {
    const element: HTMLElement = document.getElementById('dismissOnSubmitPost') as HTMLElement;
    element.click();
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.postService.save(post).subscribe(
        (response: Post) => {
          console.log(response);
          let postId: number = response.id;
          this.savePicture(this.postPicture);
          this.loadingService.isLoading.next(false);
          this.newPostUrl = `${this.clientHost}/post/${postId}`;
        },
        error => {
          console.log(error);
          this.postFail = true;
          this.loadingService.isLoading.next(false);
        }
      )
    );
  }

  savePicture(picture: File): void {
    this.subscriptions.push(
      this.postService.uploadPostPicture(picture).subscribe(
        response => {
          if (response.type === HttpEventType.UploadProgress) {
            this.progress = (response.loaded / response.total) * 100;            
          }else {
            console.log(response);
            this.onNewPostSuccess(8);
          }
          
        },
        error => {
          console.log(error);
          
        }
      )
    );
  }

  onNewPostSuccess(second: number): void {
    this.showSuccessAlert = true;
    setTimeout(() => {
      this.showSuccessAlert = false;
      this.newPostUrl = null;
    }, second * 1000);
  }

  logOut(): void {
    this.loadingService.isLoading.next(true);
    this.accountService.logout();
    this.router.navigateByUrl('/login');
    this.loadingService.isLoading.next(false);
    this.alertService.showAlert('You have been successfully logged out.', AlertType.SUCCESS);
  }

  getLonAndLat(): void {
    if (window.navigator && window.navigator.geolocation) {
      window.navigator.geolocation.getCurrentPosition(
        position => {
          this.latitude = position.coords.latitude;
          this.longitude = position.coords.longitute;
          this.getUserLocation(this.latitude, this.longitude);
        },
        error => {
          switch (error.code){
            case 1:
              console.log('Permission location denied');
              break;
            case 2:
              console.log('Position unavailable');
              break;
            case 3:
              console.log('Timeout');
              break;    
          }
        }
      );
    }
  }

  getUserLocation(latitude, longitute){
    this.location = latitude + ' ' + longitute
  }

  ngOnDestroy(){
    this.subscriptions.forEach(sub => sub.unsubscribe);
  }
}
