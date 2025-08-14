import { UserRole } from '../models/user/UserRole';

type DecodedJwtPayload = {
  roles?: string[] | string;
  authorities?: string[] | string;
  role?: string;
  exp?: number;
  [key: string]: unknown;
};

const RAW_ROLE_TO_USER_ROLE: Record<string, UserRole> = {
  ROLE_EMPLOYEE: UserRole.EMPLOYEE,
  ROLE_ADMINISTRATOR: UserRole.ADMIN,
  ROLE_MANAGER: UserRole.MANAGER,
};

/**
 * Decode a JWT payload safely without external dependencies.
 *
 * @param {string} token - The JWT string (three-part base64url format).
 * @returns {DecodedJwtPayload | null} The decoded payload if valid, otherwise null.
 */
function decodeJwtPayload(token: string): DecodedJwtPayload | null {
  try {
    const parts = token.split('.');
    if (parts.length < 2) return null;
    const base64Url = parts[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const padding = '='.repeat((4 - (base64.length % 4)) % 4);
    const json = atob(base64 + padding);
    return JSON.parse(json) as DecodedJwtPayload;
  } catch {
    return null;
  }
}

/**
 * Extract raw role claims from a decoded JWT payload, supporting common claim names.
 *
 * @param {DecodedJwtPayload | null} payload - Decoded JWT payload.
 * @returns {string[]} Array of raw role strings (may be empty).
 */
function extractRawRoles(payload: DecodedJwtPayload | null): string[] {
  if (!payload) return [];
  const raw = payload.roles ?? payload.authorities ?? payload.role ?? [];
  if (Array.isArray(raw)) return raw.map(String);
  if (typeof raw === 'string') return [raw];
  return [];
}

/**
 * Extract user roles (mapped to UserRole) from a provided JWT string.
 *
 * Recognizes role claims under "roles", "authorities", or "role".
 * Raw values (e.g., "ROLE_ADMINISTRATOR") are mapped to UserRole.
 * Unrecognized roles are omitted.
 *
 * @param {string} token - The JWT string.
 * @returns {UserRole[]} Array of mapped user roles.
 */
export function getRolesFromToken(token: string): UserRole[] {
  const payload = decodeJwtPayload(token);
  const rawRoles = extractRawRoles(payload);

  return rawRoles
    .map((r) => RAW_ROLE_TO_USER_ROLE[r])
    .filter((r): r is UserRole => !!r);
}

/**
 * Check if the user (represented by the provided token) has a specific role.
 *
 * @param {string} token - The JWT string.
 * @param {UserRole} role - The role to check for.
 * @returns {boolean} True if the user has the role, otherwise false.
 */
export function hasRole(token: string, role: UserRole): boolean {
  const roles = getRolesFromToken(token);
  return roles.includes(role);
}

const ROLE_ORDER: Record<UserRole, number> = {
  [UserRole.EMPLOYEE]: 1,
  [UserRole.MANAGER]: 2,
  [UserRole.ADMIN]: 3,
};

/**
 * Check if the user has at least the given role, honoring role inheritance
 * (EMPLOYEE < MANAGER < ADMIN).
 *
 * @param {string} token - The JWT string.
 * @param {UserRole} minRole - Minimum role required to access a resource.
 * @returns {boolean} True if user meets or exceeds the role, otherwise false.
 */
export function hasAtLeastRole(token: string, minRole: UserRole): boolean {
  const roles = getRolesFromToken(token);
  if (roles.length === 0) return false;
  const highest = Math.max(...roles.map((r) => ROLE_ORDER[r] ?? 0));
  return highest >= ROLE_ORDER[minRole];
}

/**
 * Determine whether a JWT is expired using its `exp` claim.
 *
 * If the token is empty or invalid, this returns true (treated as expired).
 * If the `exp` claim is missing, the token is treated as non-expired.
 *
 * @param {string} token - The JWT string.
 * @returns {boolean} True if expired or invalid/empty, otherwise false.
 */
export function isTokenExpired(token: string): boolean {
  if (!token) return true;

  const payload = decodeJwtPayload(token);
  if (!payload) return true;

  const exp = payload.exp;
  if (typeof exp !== 'number') return false;

  const nowSeconds = Math.floor(Date.now() / 1000);

  return exp < nowSeconds;
}
