export interface Tag {
  id: string;
  name: string;
}

export type JobCardDescription = Pick<
  JobDescription,
  "id" | "title" | "location" | "workplaceType" | "createdAt" | "company" | "tags"
>;

export interface JobDescription {
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

export type JobsProps = {
  jobs:  JobDescription[]
}
