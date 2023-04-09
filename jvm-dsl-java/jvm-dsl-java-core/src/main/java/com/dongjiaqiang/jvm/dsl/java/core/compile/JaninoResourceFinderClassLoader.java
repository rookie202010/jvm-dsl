package com.dongjiaqiang.jvm.dsl.java.core.compile;

import org.codehaus.commons.nullanalysis.Nullable;
import org.codehaus.janino.InternalCompilerException;
import org.codehaus.janino.util.ClassFile;
import org.codehaus.janino.util.resource.Resource;
import org.codehaus.janino.util.resource.ResourceFinder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JaninoResourceFinderClassLoader extends ClassLoader {

    private final ResourceFinder resourceFinder;


    public JaninoResourceFinderClassLoader(ResourceFinder resourceFinder, ClassLoader parent) {
        super(parent);
        this.resourceFinder = resourceFinder;
    }

    /**
     * @return The underlying {@link ResourceFinder}
     */
    public ResourceFinder
    getResourceFinder() { return this.resourceFinder; }

    @Override protected Class<?>
    findClass(@Nullable String className) throws ClassNotFoundException {
        assert className != null;

        // Find the resource containing the class bytecode.
        Resource classFileResource = this.resourceFinder.findResource(ClassFile.getClassFileResourceName(className));
        if (classFileResource == null) {
            return super.findClass(className);
        }

        // Open the class file resource.
        InputStream is;
        try {
            is = classFileResource.open();
        } catch (IOException ex) {
            throw new InternalCompilerException((
                    "Opening class file resource \""
                            + classFileResource.getFileName()
                            + "\": "
                            + ex.getMessage()
            ), ex);
        }

        // Read bytecode from the resource into a byte array.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            for (;;) {
                int bytesRead = is.read(buffer);
                if (bytesRead == -1) break;
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw new ClassNotFoundException("Reading class file from \"" + classFileResource + "\"", ex);
        } finally {
            try { is.close(); } catch (IOException ignored) {}
        }
        byte[] ba = outputStream.toByteArray();

        // Define the class in this ClassLoader.
        Class<?> clazz = super.defineClass(null, ba, 0, ba.length);

        if (!clazz.getName().equals(className)) {

            // This is a really complicated case: We may find a class file on
            // the class path that seemingly defines the class we are looking
            // for, but doesn't. This is possible if the underlying file system
            // has case-insensitive file names and/or file names that are
            // limited in length (e.g. DOS 8.3).
            throw new ClassNotFoundException(className);
        }

        return clazz;
    }
}
