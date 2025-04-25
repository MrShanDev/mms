import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';
import { defineConfig, loadEnv, ConfigEnv, UserConfigFnObject } from 'vite';
import vueSetupExtend from 'vite-plugin-vue-setup-extend-plus';
import viteCompression from 'vite-plugin-compression';
import { buildConfig } from './src/utils/build';
import { lazyImport, VxeResolver } from 'vite-plugin-lazy-import'
import WindiCSS from 'vite-plugin-windicss'
import AutoImport from 'unplugin-auto-import/vite'

//处理相对路径（./ , ../），生成一个规范化的绝对路径
const pathResolve = (dir: string) => {

    return resolve(__dirname, '.', dir);
};
// 路径解析函数，方便路径别名配置
const alias: Record<string, string> = {
    '/@': pathResolve('./src/'),
    'vue-i18n': 'vue-i18n/dist/vue-i18n.cjs.js',
};

const viteConfig: UserConfigFnObject = defineConfig((mode: ConfigEnv) => {
    const env: Record<string, string> = loadEnv(mode.mode, process.cwd());
    // 设置EventEmitter默认最大监听次数
    require('events').EventEmitter.defaultMaxListeners = 20;
    return {
        plugins: [
            vue(),
            WindiCSS(),
            vueSetupExtend(),
            viteCompression(),
            JSON.parse(env.VITE_OPEN_CDN) ? buildConfig.cdn() : null,
            lazyImport({
                resolvers: [
                    VxeResolver({
                        libraryName: 'vxe-table'
                    }),
                    VxeResolver({
                        libraryName: 'vxe-pc-ui'
                    })
                ]
            }),
            AutoImport({
                imports: [
                    'vue',
                    'vue-router',
                ],
                dts: resolve('./auto-imports.d.ts')
            }),
        ],
        root: process.cwd(),
        resolve: { alias },
        base: mode.command === 'serve' ? './' : env.VITE_PUBLIC_PATH,
        optimizeDeps: { exclude: ['vue-demi'] },
        server: {
            host: '0.0.0.0',
            port: env.VITE_PORT as unknown as number, // 端口号g
            hostname: "localhost", // 主机名
            open: JSON.parse(env.VITE_OPEN),// 运行自动打开浏览器
            hmr: true,
            proxy: {
                [env.VITE_APP_BASE_API]: {
                    target: env.VITE_APP_API_URL,
                    ws: true,
                    changeOrigin: true,
                    rewrite: (path) => path.replace(new RegExp('^' + env.VITE_APP_BASE_API), '')
                },
            },
        },
        build: {
            outDir: 'dist',
            chunkSizeWarningLimit: 1500,
            rollupOptions: {
                output: {
                    chunkFileNames: 'assets/js/[name]-[hash].js',
                    entryFileNames: 'assets/js/[name]-[hash].js',
                    assetFileNames: 'assets/[ext]/[name]-[hash].[ext]',
                    manualChunks(id) {
                        if (id.includes('node_modules')) {
                            return id.toString().match(/\/node_modules\/(?!.pnpm)(?<moduleName>[^\/]*)\//)?.groups!.moduleName ?? 'vender';
                        }
                    },
                },
                ...(JSON.parse(env.VITE_OPEN_CDN) ? { external: buildConfig.external } : {}),
            },
        },
        css: {
            preprocessorOptions: {
                css: { charset: false },
                scss: {
                    silenceDeprecations: ["legacy-js-api"], // 解决报错
                }
            }
        },
        define: {
            __VUE_I18N_LEGACY_API__: JSON.stringify(false),
            __VUE_I18N_FULL_INSTALL__: JSON.stringify(false),
            __INTLIFY_PROD_DEVTOOLS__: JSON.stringify(false),
            __NEXT_VERSION__: JSON.stringify(process.env.npm_package_version),
            __NEXT_NAME__: JSON.stringify(process.env.npm_package_name),
        },
        lintOnSave: false,
    };
});

export default viteConfig;
