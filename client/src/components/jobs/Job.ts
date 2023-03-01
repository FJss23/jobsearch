export interface Tag {
  name: string;
}

export interface JobDescription {
  jobId: string
  title: string
  location: string
  remoteType: string
  companyName: string
  createdAt: Date
  imgUrl: string
  tags: Tag[]
};

