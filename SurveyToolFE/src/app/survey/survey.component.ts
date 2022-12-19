import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {webSocket, WebSocketSubject} from "rxjs/webSocket";
import {Survey} from "../model/survey";
import {HttpService} from "../services/http.service";

@Component({
  selector: 'app-survey',
  templateUrl: './survey.component.html',
  styleUrls: ['./survey.component.css']
})
export class SurveyComponent implements OnInit {

  @ViewChild("canvas", {static: false}) canvas: ElementRef<HTMLCanvasElement> | undefined

  socket: WebSocketSubject<any>
  survey = new Survey()
  options: string[] = []
  colors = ["red", "blue", "green", "yellow"]
  colorsIndex = 0

  constructor(private http: HttpService) {
    this.socket = webSocket({
      url: "ws://localhost:8080/survey",
      deserializer: msg => msg.data
    })
  }

  ngOnInit(): void {
    this.socket.subscribe(value => {
      let json = JSON.parse(value)
      this.survey.question = json.question
      this.survey.result = new Map<string, number>(Object.entries(json.result))

      let iterable = this.survey.result.keys()
      this.options = []
      for (const string of iterable) {
        this.options.push(string)
      }

      this.paint()
    })
  }

  private paint() {
    let ctx = this.canvas?.nativeElement.getContext("2d")!
    let numOfOptions = this.options.length
    let width = ctx?.canvas.width
    let height = ctx?.canvas.height
    ctx.clearRect(0, 0, width!, height!)

    let max = 0
    for (const votes of this.survey.result.values()) {
      if (votes > max) {
        max = votes
      }
    }

    let i = 0
    for (const option of this.options) {
      ctx.fillStyle = this.nextColor(i)
      let h = height! / numOfOptions
      let w = width! * (this.survey.result.get(option)! / max)
      ctx.fillRect(0, h * i, w, h)

      ctx.fillStyle = 'black'
      ctx.font = "20px Arial"
      ctx.fillText(option, 10, h * i + (h/2) + 10)

      i++
    }

  }

  private nextColor(index: number): string {
    // this.colorsIndex++
    // if (this.colorsIndex >= this.colors.length) {
    //   this.colorsIndex = 0
    // }
    // return this.colors[this.colorsIndex]
    return this.colors[index % this.colors.length]
  }

  public vote(option: string) {
    this.http.vote(option)
  }

}
