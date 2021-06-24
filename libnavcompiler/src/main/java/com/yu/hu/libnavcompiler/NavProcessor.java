package com.yu.hu.libnavcompiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.yu.hu.libnavannotation.ActivityDestination;
import com.yu.hu.libnavannotation.FragmentDestination;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Created by Hy on 2020/01/14 20:54
 * <p>
 * APP页面导航信息收集注解处理器
 * <p>
 * AutoService注解：就这么一标记，annotationProcessor  project()应用一下,编译时就能自动执行该类了。
 * <p>
 * SupportedSourceVersion注解:声明我们所支持的jdk版本
 * <p>
 * SupportedAnnotationTypes:声明该注解处理器想要处理那些注解
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)  //源码类型 java 1.8
@SupportedAnnotationTypes({"com.yu.hu.libnavannotation.ActivityDestination", "com.yu.hu.libnavannotation.FragmentDestination"})
public class NavProcessor extends AbstractProcessor {

    private static final String OUTPUT_FILE_NAME = "destnation.json";

    private Messager messager;
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //日志打印,在java环境下不能使用android.util.log.e()
        this.messager = processingEnvironment.getMessager();
        //文件处理工具
        this.filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        messager.printMessage(Diagnostic.Kind.NOTE, "日志开始");

        //通过处理器环境上下文roundEnv分别获取 项目中标记的FragmentDestination.class 和ActivityDestination.class注解。
        //此目的就是为了收集项目中哪些类 被注解标记了
        Set<? extends Element> fragmentElements = roundEnv.getElementsAnnotatedWith(FragmentDestination.class);
        Set<? extends Element> activityElements = roundEnv.getElementsAnnotatedWith(ActivityDestination.class);

        if (!fragmentElements.isEmpty() || !activityElements.isEmpty()) {
            HashMap<String, JSONObject> destMap = new HashMap<>();
            //分别 处理FragmentDestination  和 ActivityDestination 注解类型
            //并收集到destMap 这个map中。以此就能记录下所有的页面信息了
            handleDestination(fragmentElements, FragmentDestination.class, destMap);
            handleDestination(activityElements, ActivityDestination.class, destMap);

            // app/src/assets
            FileOutputStream fos = null;
            OutputStreamWriter writer = null;
            try {
                //filer.createResource()意思是创建源文件
                //我们可以指定为class文件输出的地方，
                //StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
                //StandardLocation.SOURCE_OUTPUT：java文件的位置，一般在/ppjoke/app/build/generated/source/apt/目录下
                //StandardLocation.CLASS_PATH 和 StandardLocation.SOURCE_PATH用的不多，指的了这个参数，就要指定生成文件的pkg包名了
                FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
                String resourcePath = resource.toUri().getPath();
                messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath: " + resourcePath);

                //由于我们想要把json文件生成在app/src/main/assets/目录下,所以这里可以对字符串做一个截取，
                //以此便能准确获取项目在每个电脑上的 /app/src/main/assets/的路径
                String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
                String assetsPath = appPath + "src/main/assets";

                File file = new File(assetsPath);
                if (!file.exists()) {
                    file.mkdir();
                }

                //写入文件
                File outputFile = new File(file, OUTPUT_FILE_NAME);
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                outputFile.createNewFile();

                //利用fastjson把收集到的所有的页面信息 转换成JSON格式的。并输出到文件中
                String content = JSON.toJSONString(destMap);
                fos = new FileOutputStream(outputFile);
                writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(content);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private void handleDestination(Set<? extends Element> elements, Class<? extends Annotation> annotationClass, HashMap<String, JSONObject> destMap) {
        messager.printMessage(Diagnostic.Kind.NOTE, "size = " + elements.size());

        for (Element element : elements) {
            //TypeElement是Element的一种。
            //如果我们的注解标记在了类名上。所以可以直接强转一下。使用它得到全类名
            TypeElement typeElement = (TypeElement) element;
            //全类名com.mooc.ppjoke.home
            String className = typeElement.getQualifiedName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "className = " + className);
            //页面的id.此处不能重复,使用页面的类名做hascode即可
            int id = Math.abs(className.hashCode());
            //是否需要登录
            boolean needLogin = false;
            //页面的pageUrl相当于隐士跳转意图中的host://schem/path格式
            String pageUrl = null;
            //是否作为首页的第一个展示的页面
            boolean asStarter = false;
            //标记该页面是fragment 还是activity类型的
            boolean isFragment = false;
            //是否是tab页面
            boolean tabPage = false;
            //icon资源
            int iconRes = 0;

            Annotation annotation = typeElement.getAnnotation(annotationClass);
            if (annotation instanceof FragmentDestination) {
                FragmentDestination dest = (FragmentDestination) annotation;
                pageUrl = dest.pageUrl();
                needLogin = dest.needLogin();
                asStarter = dest.asStarter();
                tabPage = dest.tabPage();
                iconRes = dest.iconRes();
                isFragment = true;
            } else if (annotation instanceof ActivityDestination) {
                ActivityDestination dest = (ActivityDestination) annotation;
                pageUrl = dest.pageUrl();
                needLogin = dest.needLogin();
                asStarter = dest.asStarter();
                tabPage = dest.tabPage();
                isFragment = false;
            }

            if (destMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的pageUrl" + className);
            } else {
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("needLogin", needLogin);
                object.put("asStarter", asStarter);
                object.put("pageUrl", pageUrl);
                object.put("iconRes", iconRes);
                object.put("className", className);
                object.put("isFragment", isFragment);
                object.put("tabPage", tabPage);
                destMap.put(pageUrl, object);
            }
        }

    }
}
