import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {FileSystemFileEntry} from 'ngx-file-drop';
import {Observable} from "rxjs";
import {UploadVideoResponse} from "./upload-video/UploadVideoResponse";


@Injectable({
    providedIn: 'root'
})
export class VideoService {

    constructor(private httpClient: HttpClient) {
    }

    uploadVideo(file: File): Observable<UploadVideoResponse> {
        const formData = new FormData();
        formData.append('file', file, file.name);

        // HTTP Post call to upload the video
        return this.httpClient.post<UploadVideoResponse>("http://localhost:8081/api/video", formData); // todo change no 8080
    }
}
