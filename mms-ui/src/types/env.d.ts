/// <reference types="vite/client" />
interface ImportMetaEnv {
    VITE_APP_BASE_API: string;
    VITE_APP_API_KEY: string;
    [key: string]: string | undefined;
}
interface ImportMeta {
    readonly env: ImportMetaEnv;
}
declare global {
    namespace NodeJS {
        interface ImportMeta {
            env: ImportMetaEnv;
        }
    }
}
