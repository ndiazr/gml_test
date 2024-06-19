import { Component, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Client } from 'src/app/models/client.model';
import { ClientService } from 'src/app/services/client.service';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {
  clients: Client[] = []
  modalRef?: BsModalRef
  form = this.fb.group({
    id: new FormControl<number | null>(null),
    sharedKey: new FormControl<string>('', [Validators.required]),
    name: new FormControl<string>('', [Validators.required]),
    email: new FormControl<string>('', [Validators.required, Validators.email]),
    phone: new FormControl<string>('', [Validators.required]),
    startDate: new FormControl<string>('', [Validators.required]),
    endDate: new FormControl<string>('', [Validators.required])
  })
  formSearch = this.fb.group({
    sharedKey: new FormControl<string>('', [Validators.required]),
  })

  constructor(
    private clientService: ClientService,
    private modalService: BsModalService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.retrieveClients()
  }

  openModal(template: TemplateRef<any>, client?: Client) {
    if (client != null) {
      this.form.patchValue({
        id: client!.id,
        sharedKey: client.sharedKey,
        name: client.name,
        email: client.email,
        phone: client.phone,
        startDate: client.startDate,
        endDate: client.endDate,
      })
    } else {
      this.form.reset()
    }
    this.modalRef = this.modalService.show(template);
  }

  retrieveClients(): void {
    this.clientService.getAll().subscribe({
      next: (data) => {
        this.clients = data;
        console.log(data);
      },
      error: (e) => console.error(e)
    });
  }

  handleClient() {
    if (this.form.value.id != null) {
      if (this.form.valid) {
        const data: Client = {
          id: this.form.value.id,
          sharedKey: this.form.value.sharedKey || '',
          name: this.form.value.name || '',
          email: this.form.value.email || '',
          phone: this.form.value.phone || '',
          startDate: this.form.value.startDate || '',
          endDate: this.form.value.endDate || '',
        };

        this.clientService.update(data).subscribe({
          next: (res) => {
            console.log(res);
            this.modalRef?.hide();
            this.retrieveClients()
          },
          error: (e) => console.error(e)
        });
      }
    } else {
      if (this.form.valid) {
        const data: Client = {
          sharedKey: this.form.value.sharedKey || '',
          name: this.form.value.name || '',
          email: this.form.value.email || '',
          phone: this.form.value.phone || '',
          startDate: this.form.value.startDate || '',
          endDate: this.form.value.endDate || '',
        };

        this.clientService.create(data).subscribe({
          next: (res) => {
            console.log(res);
            this.modalRef?.hide();
            this.retrieveClients()
          },
          error: (e) => console.error(e)
        });
      }
    }
  }

  deleteClient(id?: number) {
    if(confirm("Are you sure you want to delete this user?")) { 
      this.clientService.delete(id).subscribe({
        next: (res) => {
          console.log(res);
          this.retrieveClients()
        },
        error: (e) => console.error(e)
      });
    }
  }

  searchBySharedKey(): void {
    this.clientService.findBySharedKey(this.formSearch.value.sharedKey!).subscribe({
      next: (data) => {
        this.clients = data;
        console.log(data);
      },
      error: (e) => console.error(e)
    });
  }
}
