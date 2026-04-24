import { Component, OnInit } from '@angular/core';
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

  visitorImageUrl = 'https://via.placeholder.com/500x350.png?text=Nema+slike';
  timestamp = new Date();
  message = '';

  constructor(private intercomService: IntercomService) {}

  ngOnInit(): void {
    this.loadLatestEvent();
  }

  loadLatestEvent(): void {
    this.intercomService.getLatestEvent().subscribe({
      next: (event) => {
        this.event = event;
        this.visitorImageUrl = event.imageUrl;
        this.timestamp = new Date(event.timestamp);
      },
      error: () => {
        this.message = 'Nema dostupne slike sa backenda.';
      }
    });
  }

  openDoor(): void {
    this.message = 'Šaljem komandu za otvaranje...';

    this.intercomService.openGate().subscribe({
      next: () => {
        this.message = 'Interfon je otvoren.';
      },
      error: () => {
        this.message = 'Greška: backend nije dostupan ili endpoint nije dobar.';
      }
    });
  }

  rejectVisitor(): void {
    this.message = 'Pristup odbijen.';
  }
}