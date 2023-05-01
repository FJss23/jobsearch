import { Tag } from "./Tag";

export interface Job {
  id: string;
  title: string;
  role?: string;
  salary: {
    from?: number;
    upTo?: number;
    currency?: string;
  };
  location: string;
  workday: string;
  description: string;
  state: string;
  workModel: string;
  createdAt: Date;
  company: {
    name: string;
    logoUrl: string;
  };
  tags: Tag[];
}

export interface Page<T> {
  content: T[];
  total: number;
  prev: number;
  next: number;
  first: boolean;
}
