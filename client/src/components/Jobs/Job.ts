export interface Tag {
  id: string;
  name: string;
}

export interface JobOffer {
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
  tags: Tag[];
}


export type JobOfferCard = Pick<
  JobOffer,
  "id" | "title" | "location" | "workModel" | "createdAt" | "companyName" | "tags"
>;

