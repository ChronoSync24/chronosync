const API_BASE_URL = process.env.VITE_API_BASE_URL;

if (!API_BASE_URL) {
  throw new Error('VITE_API_BASE_URL is not defined in the environment variables.');
}

export const getApiUrl = (path: string): string => `${API_BASE_URL}${path}`;
