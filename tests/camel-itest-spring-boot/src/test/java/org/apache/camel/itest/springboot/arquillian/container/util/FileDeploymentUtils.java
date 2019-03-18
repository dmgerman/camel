begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.arquillian.container.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|arquillian
operator|.
name|container
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|FileVisitResult
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|SimpleFileVisitor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|BasicFileAttributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|se
operator|.
name|api
operator|.
name|ClassPathDirectory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|spi
operator|.
name|client
operator|.
name|container
operator|.
name|DeploymentException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|asset
operator|.
name|ClassAsset
import|;
end_import

begin_class
DECL|class|FileDeploymentUtils
specifier|public
specifier|final
class|class
name|FileDeploymentUtils
block|{
DECL|field|DELIMITER_RESOURCE_PATH
specifier|private
specifier|static
specifier|final
name|char
name|DELIMITER_RESOURCE_PATH
init|=
literal|'/'
decl_stmt|;
DECL|field|DELIMITER_CLASS_NAME_PATH
specifier|private
specifier|static
specifier|final
name|char
name|DELIMITER_CLASS_NAME_PATH
init|=
literal|'.'
decl_stmt|;
DECL|field|EXTENSION_CLASS
specifier|private
specifier|static
specifier|final
name|String
name|EXTENSION_CLASS
init|=
literal|".class"
decl_stmt|;
DECL|method|FileDeploymentUtils ()
specifier|private
name|FileDeploymentUtils
parameter_list|()
block|{     }
DECL|method|materializeClass (File entryDirectory, ClassAsset classAsset)
specifier|public
specifier|static
name|void
name|materializeClass
parameter_list|(
name|File
name|entryDirectory
parameter_list|,
name|ClassAsset
name|classAsset
parameter_list|)
throws|throws
name|DeploymentException
throws|,
name|IOException
block|{
name|File
name|classDirectory
decl_stmt|;
if|if
condition|(
name|classAsset
operator|.
name|getSource
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|classDirectory
operator|=
operator|new
name|File
argument_list|(
name|entryDirectory
argument_list|,
name|classAsset
operator|.
name|getSource
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
name|DELIMITER_CLASS_NAME_PATH
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|classDirectory
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DeploymentException
argument_list|(
literal|"Could not create class package directory: "
operator|+
name|classDirectory
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|classDirectory
operator|=
name|entryDirectory
expr_stmt|;
block|}
name|File
name|classFile
init|=
operator|new
name|File
argument_list|(
name|classDirectory
argument_list|,
name|classAsset
operator|.
name|getSource
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|.
name|concat
argument_list|(
name|EXTENSION_CLASS
argument_list|)
argument_list|)
decl_stmt|;
name|classFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|classAsset
operator|.
name|openStream
argument_list|()
init|;
name|OutputStream
name|out
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|classFile
argument_list|)
init|)
block|{
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|materializeSubdirectories (File entryDirectory, Node node)
specifier|public
specifier|static
name|void
name|materializeSubdirectories
parameter_list|(
name|File
name|entryDirectory
parameter_list|,
name|Node
name|node
parameter_list|)
throws|throws
name|DeploymentException
throws|,
name|IOException
block|{
for|for
control|(
name|Node
name|child
range|:
name|node
operator|.
name|getChildren
argument_list|()
control|)
block|{
if|if
condition|(
name|child
operator|.
name|getAsset
argument_list|()
operator|==
literal|null
condition|)
block|{
name|materializeSubdirectories
argument_list|(
name|entryDirectory
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ClassPathDirectory
operator|.
name|isMarkerFileArchivePath
argument_list|(
name|child
operator|.
name|getPath
argument_list|()
argument_list|)
condition|)
block|{
comment|// Do not materialize the marker file
continue|continue;
block|}
comment|// E.g. META-INF/my-super-descriptor.xml
name|File
name|resourceFile
init|=
operator|new
name|File
argument_list|(
name|entryDirectory
argument_list|,
name|child
operator|.
name|getPath
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|replace
argument_list|(
name|DELIMITER_RESOURCE_PATH
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|resoureDirectory
init|=
name|resourceFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|resoureDirectory
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|resoureDirectory
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DeploymentException
argument_list|(
literal|"Could not create class path directory: "
operator|+
name|entryDirectory
argument_list|)
throw|;
block|}
name|resourceFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|child
operator|.
name|getAsset
argument_list|()
operator|.
name|openStream
argument_list|()
init|; OutputStream out = new FileOutputStream(resourceFile)
block|)
block|{
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|child
operator|.
name|getPath
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

begin_function
DECL|method|copy (InputStream in, OutputStream out)
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|n
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
operator|(
name|n
operator|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|deleteRecursively (Path directory)
specifier|public
specifier|static
name|void
name|deleteRecursively
parameter_list|(
name|Path
name|directory
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|walkFileTree
argument_list|(
name|directory
argument_list|,
operator|new
name|SimpleFileVisitor
argument_list|<
name|Path
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|visitFile
parameter_list|(
name|Path
name|file
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|delete
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|postVisitDirectory
parameter_list|(
name|Path
name|dir
parameter_list|,
name|IOException
name|exc
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|delete
argument_list|(
name|dir
argument_list|)
expr_stmt|;
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|deleteContent (Path directory)
specifier|public
specifier|static
name|void
name|deleteContent
parameter_list|(
name|Path
name|directory
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|walkFileTree
argument_list|(
name|directory
argument_list|,
operator|new
name|SimpleFileVisitor
argument_list|<
name|Path
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|visitFile
parameter_list|(
name|Path
name|file
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|delete
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

