import { Tag } from "./Tag";

export interface Job {
  id: string;
  title: string;
  role?: string;
  salaryFrom?: number;
  salaryUpTo?: number;
  salaryCurrency?: string;
  location: string;
  workday: string;
  description: string;
  state: string;
  workModel: string;
  createdAt: Date;
  companyName: string;
  companyLogoUrl: string
  tags: Tag[];
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  first: boolean;
}

