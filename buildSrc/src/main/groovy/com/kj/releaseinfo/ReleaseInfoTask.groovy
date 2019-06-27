package com.kj.releaseinfo

import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReleaseInfoTask extends DefaultTask {
    ReleaseInfoTask() {
        group = "kj"
        description = "releaseInfo"
    }

    /**
     * 位于doFirst 跟doLast之间
     */
    @TaskAction
    void doAction() {
        updateInfo()
    }

    void updateInfo() {
        //获取信息
        String versionCode = project.extensions.releaseInfo.versionCode
        String versionName = project.extensions.releaseInfo.versionName
        String versionInfo = project.extensions.releaseInfo.versionInfo
        String fileName = project.extensions.releaseInfo.fileName
        File file = project.file(fileName)
        if (!file.exists()) {
            file.createNewFile()
        }

        def sw = new StringWriter()
        def xmlBuilder = new MarkupBuilder(sw)
        //file 没有内容，直接写入文件
        if (file.text != null && file.text.size() <= 0) {
            xmlBuilder.releases {
                release {
                    vsCode(versionCode)
                    vsName(versionName)
                    vsInfo(versionInfo)
                }
            }
            file.withWriter { writer ->
                writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n")
                writer.append(sw.toString())
            }
        } else {
            //file有内容，那么判断是否包含了该version信息，如果是，那么不用新加内容，不是的话，将新的版本信息加进入
            def xmlParse = new XmlParser().parse(file)
            def hasWrited = xmlParse.release.any {
                release ->
                    release.vsCode.text().equals(versionCode) && release.vsName.text().equals(versionName)
            }
            if (hasWrited) {
                return
            }

            xmlBuilder.release {
                vsCode(versionCode)
                vsName(versionName)
                vsInfo(versionInfo)
            }
            def lines = file.readLines()
            def length = lines.size() - 1
            file.withWriter { writer ->
                lines.eachWithIndex { String entry, int index ->
                    if (index != length) {
                        writer.append(entry + "\r\n")
                    } else {
                        writer.append("\r\n" + sw.toString() + "\r\n")
                        writer.append(entry)
                    }
                }
            }
        }
    }
}