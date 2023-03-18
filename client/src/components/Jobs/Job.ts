export interface Tag {
  id: string;
  name: string;
}

export interface JobOffer {
  id: string;
  title: string;
  industry?: string;
  salaryFrom: number;
  salaryUpTo: number;
  coin: string;
  location: string;
  workdayType: string;
  description: string;
  workplaceType: string;
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
  "id" | "title" | "location" | "workplaceType" | "createdAt" | "company" | "tags"
>;

