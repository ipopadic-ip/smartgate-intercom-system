import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IntercomEvent } from '../models/intercom-event';
import { Client } from '@stomp/stompjs';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IntercomService {
  private apiUrl = 'http://localhost:8080/api';
  private eventSubject = new Subject<IntercomEvent>();
  event$ = this.eventSubject.asObservable();
  private stompClient?: Client;
  private connected = false;

  constructor(private http: HttpClient) {}

  connectWebSocket() {
    if (this.connected) return;
    console.log('Pokušavam konekciju na WebSocket...');

    this.stompClient = new Client({
      brokerURL: 'ws://localhost:8080/ws-intercom',
      reconnectDelay: 5000,
       webSocketFactory: () => {
      console.log('WebSocketFactory pozvan');
      return new WebSocket('ws://localhost:8080/ws-intercom');
    },
    debug: (str) => console.log('STOMP DEBUG:', str), // sve STOMP poruke
  });
      // webSocketFactory: () => new WebSocket('ws://localhost:8080/ws-intercom'),
    // });

    this.stompClient.onConnect = () => {
       console.log('STOMP povezan!');
      this.connected = true;
      this.stompClient?.subscribe('/topic/intercom', (message) => {
        console.log('Raw body:', message.body); 
        const event: IntercomEvent = JSON.parse(message.body);
        console.log('Parsed event:', event);
        this.eventSubject.next(event);
      });
      console.log('Pretplaćen na /topic/intercom');
    };

    this.stompClient.onStompError = (frame) => {
      console.error('STOMP greška:', frame);
    };

    this.stompClient.onDisconnect = () => {
      this.connected = false;
    };

    this.stompClient.activate();

    this.stompClient.onWebSocketError = (error) => {
    console.error('WebSocket greška:', error);
  };
  }

  openGate() {
    // return this.http.post(`${this.apiUrl}/open`, {}, { responseType: 'text' });
    return this.http.post(`${this.apiUrl}/open`, {}, {
      observe: 'response',
      responseType: 'text'
    });
  }
}