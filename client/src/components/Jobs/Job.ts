export interface Tag {
  id: string;
  defaultName: string;
  code: string;
}

export interface JobOffer {
  id: string;
  title: string;
  industry?: string;
  salaryFrom: number;
  salaryUpTo: number;
  coin: string;
  location: string;
  workday: string;
  description: string;
  state: string;
  workplaceSystem: string;
  howToApply?: string;
  createdAt: Date;
  company: Company;
  tags: Tag[];
}

export interface Company {
  id: string;
  name: string;
  description: string;
  logoUrl?: string;
  twitter?: string;
  facebook?: string;
  instagram?: string;
  website?: string;
}

export type JobOfferCard = Pick<
  JobOffer,
  "id" | "title" | "location" | "workplaceSystem" | "createdAt" | "company" | "tags"
>;

