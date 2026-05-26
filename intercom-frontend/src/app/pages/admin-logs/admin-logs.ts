import { Component, OnInit, ViewChild } from '@angular/core';
import { ActionLogService } from '../../services/action-log-service';
import { ActionLog } from '../../models/action-log';
import {  MatTableDataSource } from '@angular/material/table';
import {MatTableModule} from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { UserForm } from "../user-form/user-form";
import { UserListComponent } from "../user-list-component/user-list-component";



@Component({
  selector: 'app-admin-logs',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatCardModule,
    UserForm,
    UserListComponent
],
  templateUrl: './admin-logs.html',
  styleUrls: ['./admin-logs.css'],
})
export class AdminLogs implements OnInit {

  displayedColumns: string[] = [ 'action', 'timestamp', 'doorNumber', 'user'];
  logs = new MatTableDataSource<ActionLog>([]);

  
  @ViewChild(UserListComponent)
  userListComponent!: UserListComponent;

  constructor(private actionLogService: ActionLogService) {}
  ngOnInit(): void {
    this.actionLogService.findAll().subscribe(data => {
      this.logs.data = data.reverse();
    });
  }

  refreshUsers() {
    this.userListComponent.loadUsers();
  }

  
}
