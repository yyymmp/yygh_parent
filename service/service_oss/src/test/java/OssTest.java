import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import java.io.ByteArrayInputStream;

/**
 * @author clearlove
 * @ClassName OssTest.java
 * @Description
 * @createTime 2021年08月16日 22:36:00
 */
public class OssTest {

    public static void main(String[] args) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "LTAI5tFDTPAjYa4FhDxQwgs7";
        String accessKeySecret = "BXBpW86bN5EFrnQHXVNbBeBIgq1Gmh";
        String bucketName = "jializhong";
// <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = "yygh.txt";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
        String content = "Hello OSS";
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));

// 关闭OSSClient。
        ossClient.shutdown();
    }
}
