import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'Translate';
  url = 'https://translate.yandex.net/api/v1.5/tr.json';
  key = 'trnsl.1.1.20190223T233139Z.4771960f7d56d5cf.db41d1e7a51da54306897d63cd36664e1e88712f';
  langs = [];

  fromLang;
  toLang;
  userInput;
  translated;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get(`${this.url}/getLangs` +
      '?key=' + this.key +
      '&ui=en')
      .subscribe((data: any) => {
        this.langs = [...Object.entries(data.langs)];
      });

    // Set default languages
    this.fromLang = 'en';
    this.toLang = 'es';
  }

  translate() {
    if (this.userInput) {
      this.http.get(`${this.url}/translate` +
        `?key=${this.key}` +
        `&text=${encodeURI(this.userInput)}` +
        `&lang=${this.fromLang}-${this.toLang}`)
        .subscribe((data: any) => {
          this.translated = data.text;
        });
    }
  }

  swap() {
    [this.fromLang, this.toLang] = [this.toLang, this.fromLang];
    [this.userInput, this.translated] = [this.translated, this.userInput];
  }
}
