import { Component, OnInit, TemplateRef } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Client } from 'src/app/models/client.model';
import { ClientService } from 'src/app/services/client.service';
import { LoggerService } from 'src/app/shared/logger.service';

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
    private fb: FormBuilder,
    private logger: LoggerService
  ) { }

  ngOnInit(): void {
    this.retrieveClients()
  }

  downloadCSV(): void {
    this.clientService.downloadCsv()
      .subscribe((buffer) => {
        const data: Blob = new Blob([buffer], {
          type: "text/csv;charset=utf-8"
        });
        const url = window.URL.createObjectURL(data);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'exported_data.csv';
        a.click();
        window.URL.revokeObjectURL(url);
      });
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

  clearSearch() {
    this.formSearch.reset()
    this.retrieveClients()
  }

  retrieveClients(): void {
    this.logger.info('Fetching data of clients');
    this.clientService.getAll().subscribe({
      next: (data) => {
        this.clients = data;
        this.logger.info('Clients data', data);
      },
      error: (e) => this.logger.error('Error fetching clients', e)
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

        this.logger.info('Updating client', data);
        this.clientService.update(data).subscribe({
          next: (res) => {
            this.logger.info('Client updated', res);
            this.modalRef?.hide();
            this.retrieveClients()
          },
          error: (e) => this.logger.error('Error updating client', e)
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
        this.logger.info('Creating client', data);
        this.clientService.create(data).subscribe({
          next: (res) => {
            this.logger.info('Client created', res);
            this.modalRef?.hide();
            this.retrieveClients()
          },
          error: (e) => this.logger.error('Error creating client', e)
        });
      }
    }
  }

  deleteClient(id?: number) {
    if (confirm("Are you sure you want to delete this user?")) {
      this.logger.info('Deleting client with id:', { id })
      this.clientService.delete(id).subscribe({
        next: (res) => {
          this.logger.info('Client deleted correctly')
          this.retrieveClients()
        },
        error: (e) => this.logger.error('Error deleting client', e)
      });
    }
  }

  searchBySharedKey(): void {
    this.logger.info('Searching clients by sharedKey');
    this.clientService.findBySharedKey(this.formSearch.value.sharedKey!).subscribe({
      next: (data) => {
        this.clients = data;
        this.logger.info('Clients finded', data);
      },
      error: (e) => this.logger.error('Error searching clients by sharedKey', e)
    });
  }
}
