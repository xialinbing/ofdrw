package org.ofdrw.pkg.dir;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ofdrw.pkg.dir.VirtualContainer;
import org.ofdrw.pkg.tool.EleCup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author 权观宇
 * @since 2020-04-02 20:00:50
 */
class VirtualContainerTest {

    /**
     * 测试虚拟容器
     */
    private VirtualContainer vc;

    final static String target = "target/VirtualContainer";

    @BeforeEach
    public void init() throws IOException {
        Path path = Paths.get(target);
        if (Files.exists(path)) {
            FileUtils.deleteDirectory(path.toFile());
        } else {
            path = Files.createDirectories(path);
        }
        vc = new VirtualContainer(path);
    }

    @Test
    void putFile() throws IOException {
        final String fileName = "testimg.png";
        Path path = Paths.get("src/test/resources", fileName);
        vc.putFile(path);
        Assertions.assertTrue(Files.exists(Paths.get(target, fileName)));
    }

    @Test
    void getFile() throws IOException {
        final String fileName = "testimg.png";
        Path path = Paths.get("src/test/resources", fileName);
        vc.putFile(path);

        Path file = vc.getFile(fileName);
        final byte[] bytes = Files.readAllBytes(file);
        final byte[] bytes1 = Files.readAllBytes(path);
        Assertions.assertArrayEquals(bytes, bytes1);
    }

    @Test
    void putObj() throws DocumentException {
        String fileName = "Content.xml";
        Path path = Paths.get("src/test/resources", fileName);
        Element inject = EleCup.inject(path);
        vc.putObj("C.xml", inject);
        // TODO 刷新到文件
//        Assertions.assertTrue(Files.exists(Paths.get(target, "C.xml")));

    }

    @Test
    void getObj() throws IOException, DocumentException {
        String fileName = "Content.xml";
        Path path = Paths.get("src/test/resources", fileName);
        vc.putFile(path);
        Element obj = vc.getObj(fileName);
        Assertions.assertEquals("ofd:Page", obj.getQualifiedName());
    }

    @Test
    void getContainerName() {
        Assertions.assertEquals("VirtualContainer", vc.getContainerName());
    }

    @Test
    void obtainContainer() throws IOException {
        // 创建一个容器
        VirtualContainer pages = vc.obtainContainer("Pages", VirtualContainer::new);
        Assertions.assertTrue(Files.exists(Paths.get(target, "Pages")));

        Path path = Paths.get(target);
        VirtualContainer vc2 = new VirtualContainer(path);
        VirtualContainer pages1 = vc2.getContainer("Pages", VirtualContainer::new);
        Assertions.assertNotNull(pages1);
    }

}