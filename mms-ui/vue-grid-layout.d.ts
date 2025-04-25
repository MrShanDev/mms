// vue-grid-layout.d.ts
declare module 'vue-grid-layout' {
    import Vue from 'vue';

    // 声明插件类型
    interface PluginOptions {} // 根据实际配置填写
    const plugin: Vue.PluginObject<PluginOptions>;

    // 声明组件
    const GridLayout: Vue.ComponentOptions;
    const GridItem: Vue.ComponentOptions;

    // 声明类型（如果有）
    interface LayoutItem {
        x: number;
        y: number;
        w: number;
        h: number;
        i: string;
    }
    // export default plugin;
    // 导出声明
    export { GridLayout, GridItem, LayoutItem ,plugin};
}
