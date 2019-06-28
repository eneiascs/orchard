import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../model/post';
import { Comment } from '../model/comment';
import { ServerConstants } from '../constant/server';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  constants: ServerConstants = new ServerConstants();
  public host: string = this.constants.host;
  public clientHost: string = this.constants.client;
  public userHost: string = this.constants.userPicture;
  public postHost: string = this.constants.postPicture;

  constructor(private http: HttpClient) { }

  save(post: Post): Observable<Post>{
    return this.http.post<Post>(`${this.host}/post/save`, post);
  }  

  getOnePostById(postId: number): Observable<Post>{
    return this.http.get<Post>(`${this.host}/post/getPostById/${postId}`);
  } 

  getPostsByUsername(username: string): Observable<Post[]>{
    return this.http.get<Post[]>(`${this.host}/post/getPostByUsername/${username}`);
  } 
  saveComment(comment: Comment): Observable<Comment>{
    return this.http.post<Comment>(`${this.host}/post/comment/add`, comment);
  }  

  delete(postId: number): Observable<Post>{
    return this.http.delete<Post>(`${this.host}/post/delete/${postId}`);
  }
  
  like(postId: number, username: string){
    
    return this.http.post(`${this.host}/post/like/`, { postId, username }, { responseType : 'text'});
  }

  unlike(postId: number, username: string){
    
    return this.http.post(`${this.host}/post/unlike/`, { postId, username }, { responseType : 'text'});
  }

  uploadPostPicture(recipePicture: File){
    const fd = new FormData();
    fd.append('image', recipePicture, recipePicture.name);
    return this.http
      .post(`${this.host}/post/photo/upload`, fd, { 
        responseType: 'text',
        reportProgress: true,
        observe: 'events'
      })      
  }

}
