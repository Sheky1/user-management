export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  permissions: [string];
}

export interface Token {
  token: string;
  user: User;
}
