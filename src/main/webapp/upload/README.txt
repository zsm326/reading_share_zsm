此目录用于存放书籍封面上传后的图片（由 UUID 重命名）。
保留本文件是为了让 Maven 打包 war 时把 upload 空目录一并带上，
避免 getRealPath("/upload") 返回 null 导致上传失败。
