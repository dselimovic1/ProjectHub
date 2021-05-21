import { PageEvent } from '@angular/material/paginator';
import { Project, ProjectService } from '../../../services/project/project.service';
import {Component, OnInit} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewProjectComponent} from "../new-project/new-project.component";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent implements OnInit {
  projectNum = 0;
  page = 0;
  size = 5;
  projects: Array<Project> = [];
  pageOptions = [5, 10, 20];

  constructor(private projectService: ProjectService, private modal: NgbModal) { }

  ngOnInit(): void {
    this.loadProjects();
  }

  paginate($event: PageEvent): any {
    if (this.page === $event.pageIndex && this.size === $event.pageSize ) {
      return;
    }
    this.page = $event.pageIndex;
    this.size = $event.pageSize;
    this.loadProjects();
  }

  onProjectsLoad(data: any): any {
    this.projectNum = data.metadata.total_elements;
    this.projects = data.data;
  }

  loadProjects(): any {
    this.projectService.getProjects('owner', this.page, this.size,
      (data: any) => this.onProjectsLoad(data),
      () => {}
    );
  }

  createProject(): any {
    const modal = this.modal.open(NewProjectComponent, { size: 'xl' });
  }
}
