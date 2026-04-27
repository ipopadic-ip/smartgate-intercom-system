import { Component, OnInit, PLATFORM_ID, Inject, ChangeDetectorRef } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';
import { IntercomService } from '../../services/intercom';
import { IntercomEvent } from '../../models/intercom-event';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  event?: IntercomEvent;
  stan?: number;
  visitorImageUrl = 'https://via.placeholder.com/500x350.png?text=Nema+slike';
  timestamp = new Date();
  message = '';
  isProcessing = false;

  constructor(
    private intercomService: IntercomService,
    @Inject(PLATFORM_ID) private platformId: Object,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.intercomService.connectWebSocket();
      this.intercomService.event$.subscribe(event => {
        console.log('Dashboard primio event:', event);
        this.event = event;
        this.visitorImageUrl = event.image_url;
        this.stan = event.stan;

        const now = new Date();
        const [h, m, s] = event.timestamp.split(':').map(Number);
        now.setHours(h, m, s, 0);
        this.timestamp = new Date(now);

        this.message = 'Novi posetilac na interfonu.';
        // if (!this.isProcessing) {
        //   this.message = 'Novi posetilac na interfonu.';
        // }
        
        this.cdr.detectChanges(); // forsira UI refresh
      });
    }
  }

  openDoor(): void {
    this.isProcessing = true;
    this.message = 'Kapija uspešno otvorena.';
    this.intercomService.openGate().subscribe({
      next: () => { this.message = 'Kapija je otvorena.'; },
      error: () => { this.message = 'Greška: backend nije dostupan.'; }
    });
  }

  rejectVisitor(): void {
    this.message = 'Pristup odbijen.';
  }
}