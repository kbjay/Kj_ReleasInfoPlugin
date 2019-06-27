package com.kj.releaseinfo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class ReleaseInfoPlugin implements Plugin<Project> {

    private static final String EXT_NAME = "releaseInfo"
    private static final String TASK_NAME = "releaseInfoTask"

    //apply plugin:'com.kj.releaseInfo' 之后会调用该方法
    @Override
    void apply(Project project) {
        //1：创建扩展属相，通过扩展属相将gradle中的值传递过来
        project.extensions.create(EXT_NAME, ReleaseInfoEntity)

        //创建task
        project.tasks.create(TASK_NAME, ReleaseInfoTask)

        //将该task绑定到build命令之后
        project.afterEvaluate {
            Task releaseTask = project.tasks.findByName(TASK_NAME)

            def buildTask = project.tasks.findByName("build")
            buildTask.doLast {
                releaseTask.execute()
            }
        }
    }
}