import { Host } from '@angular/core';

export class ServerConstants {
    // Dev Env
    public host = 'http://localhost:8080';

    public client = 'http://localhost:4200';
    
    public userPicture: string = `${this.host}/image/user`;

    public postPicture: string = `${this.host}/image/post`;
    
}
