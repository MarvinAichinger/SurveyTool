import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) { }

  vote(option: string) {
    this.http.post("http://localhost:8080/api/survey/vote/" + option, {}, {observe: "response"})
      .subscribe(value => console.log(value))
  }

}
