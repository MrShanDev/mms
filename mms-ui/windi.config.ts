import { defineConfig } from 'windicss/helpers'

export default defineConfig({
    darkMode: 'class', // 支持暗黑模式
    theme: {
        extend: {
            screens: {
                'sm': '640px',
                'md': '768px',
                'lg': '1024px',
                'xl': '1280px',
                '2xl': '1536px',
            },
            colors: {
                primary: '#1DA1F2',  // 自定义主色调
                secondary: '#14171A',
            },
        },
    },
    plugins: [
        require('windicss/plugin/forms'),  // WindiCSS 的表单插件
        require('windicss/plugin/typography'),  // 优化排版
    ],
})
