import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {FileSystemFileEntry} from 'ngx-file-drop';
import {Observable} from "rxjs";
import {UploadVideoResponse} from "./upload-video/UploadVideoResponse";
import {VideoDto} from "./video-dto";


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
        return this.httpClient.post<UploadVideoResponse>("http://localhost:8080/api/video", formData); // todo change no 8080
    }

    uploadThumbnail(file: File, videoId: string): Observable<string> {
        const formData = new FormData();
        formData.append('file', file, file.name);
        formData.append('videoId', videoId);

        // HTTP Post call to upload the thumbnail
        return this.httpClient.post("http://localhost:8080/api/video/thumbnail", formData, {
            responseType: 'text'
        }); // todo change no 8080
    }

    getVideo(videoId: string): Observable<VideoDto> {
        return this.httpClient.get<VideoDto>("http://localhost:8080/api/video/" + videoId);
    }

    saveVideo(videoMetaData: VideoDto): Observable<VideoDto> {
        // const headers = new HttpHeaders()
        //     .set('Content-Type', 'application/json')
        //     .set('Accept', 'application/json')
        // ;
        return this.httpClient.post<VideoDto>("http://localhost:8080/api/video/edit", videoMetaData);
    }
}
