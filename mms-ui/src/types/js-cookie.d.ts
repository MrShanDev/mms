declare module 'js-cookie' {
    export function get(name: string): string | undefined;
    export function set(name: string, value: string, options?: CookieAttributes): void;
    export function remove(name: string, options?: CookieSetOptions): void;
    export function withAttributes(attr: CookieAttributes): any;
    export function withConverter(converter: CookieConverter): any;

    interface CookieAttributes {
        expires?: number | Date;
        path?: string;
        domain?: string;
        secure?: boolean;
        sameSite?: 'Strict' | 'Lax' | 'None';
    }

    interface CookieSetOptions extends CookieAttributes {
        expires?: number | Date;
    }

    interface CookieConverter {
        read?: (value: string) => any;
        write?: (value: any) => string;
    }
}
