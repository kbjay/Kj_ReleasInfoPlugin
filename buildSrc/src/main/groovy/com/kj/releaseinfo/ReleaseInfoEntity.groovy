package com.kj.releaseinfo

class ReleaseInfoEntity {
    String versionCode
    String versionName
    String versionInfo
    String fileName

    @Override
    String toString() {
        "ReleaseInfoEntity{" +
                "versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionInfo='" + versionInfo + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}