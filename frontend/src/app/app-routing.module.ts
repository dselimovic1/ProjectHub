import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/pages/login/login.component';
import { ResetPasswordEmailComponent } from './components/pages/reset-password/reset-password-email/reset-password-email.component'
import { ResetPasswordCodeComponent } from './components/pages/reset-password/reset-password-code/reset-password-code.component'
import { ResetPasswordNewComponent } from './components/pages/reset-password/reset-password-new/reset-password-new.component'
import { NotFoundComponent } from './components/pages/not-found/not-found.component';
import { RegisterComponent } from './components/pages/register/register.component';
import { NewTaskComponent } from './components/pages/new-task/new-task.component';
import { NewProjectComponent } from './components/pages/new-project/new-project.component';
import { NewIssueComponent } from './components/pages/new-issue/new-issue.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: LoginComponent
  },
  {
    path: 'reset-password-email',
    pathMatch: 'full',
    component: ResetPasswordEmailComponent
  },
  {
    path: 'reset-password-code',
    pathMatch: 'full',
    component: ResetPasswordCodeComponent
  },
  {
    path: 'reset-password-new',
    pathMatch: 'full',
    component: ResetPasswordNewComponent
  },
  {
    path: 'register',
    pathMatch: 'full',
    component: RegisterComponent
  },
  {
    path: 'new-task',
    component: NewTaskComponent
  },
  {
    path: 'new-project',
    component: NewProjectComponent
  },
  {
    path: 'new-issue', 
    component: NewIssueComponent
  }, 
  {
    path: 'not-found',
    pathMatch: 'full',
    component: NotFoundComponent
  }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
